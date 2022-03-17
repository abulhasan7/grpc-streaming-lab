package org.hyd.stream;

import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.hyd.protos.File;
import org.hyd.protos.StreamingGrpc;

import java.io.FileInputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class StreamingMaxCapacityClient {

    private static final Logger logger = Logger.getLogger(StreamingMaxCapacityClient.class.getName());

    private final StreamingGrpc.StreamingBlockingStub blockingStub;

    /**
     * Construct client for accessing HelloWorld server using the existing channel.
     */
    public StreamingMaxCapacityClient(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = StreamingGrpc.newBlockingStub(channel);
    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting. The second argument is the target server.
     */
    public static void main(String[] args) throws Exception {
        // Access a service running on the local machine on port 50051
        if (args.length != 2) {
            throw new Exception("Please provide target and full file path as arguments");
        }
        String target = args[0];

        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();


        try {

            AtomicInteger byteSize = new AtomicInteger(2000000);
            int byteStart = 0;
            AtomicInteger count = new AtomicInteger(1);
            AtomicInteger previousCount = new AtomicInteger(0);
            long timerPeriodMs = 20000;
            long timerInitialDelayMs = 20000;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    int totalBytesSent = (count.intValue() - previousCount.intValue()) * byteSize.intValue();
                    int throughputInSec = totalBytesSent / 20;
                    System.out.println("Current byteSize is :" + byteSize.intValue() + " , throughputInSec is: " + throughputInSec);
                    if (throughputInSec > 2000000) {
                        throughputInSec = 2000000;
                    } else if (throughputInSec < 1024) {
                        throughputInSec = 1024;
                    }
                    byteSize.set(throughputInSec);
                    System.out.println("New byteSize is: " + byteSize.intValue());
                    previousCount.set(count.intValue());
                }
            }, timerInitialDelayMs, timerPeriodMs);


            String fullFileName = args[1];
            String[] fileArray = fullFileName.split("\\\\");
            String fileName = fileArray[fileArray.length - 1];
            FileInputStream fileInputStream = new FileInputStream(fullFileName);
            ByteString fileBytes = ByteString.readFrom(fileInputStream);
            StreamingMaxCapacityClient client = new StreamingMaxCapacityClient(channel);
            int byteEnd = 1;
            while (byteStart < fileBytes.size()) {
                byteEnd = Math.min(byteStart + byteSize.intValue(), fileBytes.size());
                ByteString tempBuffer = fileBytes.substring(byteStart,byteEnd );
                byteStart += byteSize.intValue();
                //if failed retry, for set amount of time
                int noOfRetry = 0;
                boolean didRequestFail = true;
                while (didRequestFail && (noOfRetry < 4)) {
                    didRequestFail = client.uploadFile(tempBuffer, null, count.intValue(), null);

                    if(didRequestFail){
//                        System.err.println("Chunk Number: "+count.intValue()+" failed"+"No of retries: "+noOfRetry);
                    }
                    noOfRetry++;

                }
                if(didRequestFail){
                    System.err.println("File Upload Failed, Please try again!!!");
                    break;
                }

//                System.out.format("Sent Chunk number: " + count.intValue()+" | Progress: %.2f \n",(((double)byteEnd/fileBytes.size())*100));
                count.incrementAndGet();
            }
            if(byteEnd == fileBytes.size()){
                client.uploadFile(null, fileName, -1, count.decrementAndGet());
//                System.out.println("File: " + fileName + "Successfully sent ,Total File Size: " +fileBytes.size()+"Total Chunks: " +count.intValue());
            }
            timer.cancel();
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

    }

    /**
     * Say hello to server.
     */
    public boolean uploadFile(ByteString fileBytes, String name, Integer index, Integer totalSize) {

        try {
            File request;
            if (fileBytes != null) {
                request = File.newBuilder().setPayload(fileBytes).setIndex(index).build();
            } else {
                request = File.newBuilder().setFilename(name).setIndex(index).setTotalsize(totalSize).build();
            }
            blockingStub.uploadFile(request);
            return false;
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus());
            return true;
        }
    }
}

