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

public class StreamingFixedStepFactorClient {

    private static final Logger logger = Logger.getLogger(StreamingFixedStepFactorClient.class.getName());

    private final StreamingGrpc.StreamingBlockingStub blockingStub;

    /**
     * Construct client for accessing HelloWorld server using the existing channel.
     */
    public StreamingFixedStepFactorClient(Channel channel) {
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
        String target = "10.0.0.123:50051";

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
            int threshold = 2000000;
            double stepFactor = 0.1;
            long timerPeriodMs = 20000;
            long timerInitialDelayMs = 20000;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    int totalBytesSent = (count.intValue() - previousCount.intValue()) * byteSize.intValue();
                    int throughputInSec = totalBytesSent / 20;
                    if (throughputInSec > threshold) {
                        int byteS = (int) (byteSize.intValue() * (1 + stepFactor));
                        if(byteS>2000000){
                            byteS = 2000000;
                        }else if(byteS<1024){
                            byteS = 1024;
                        }
                        byteSize.set(byteS);

                        System.out.println("Over threshold, New byteSize is: " + byteSize.intValue());
                    } else {
                        int byteS = (int) (byteSize.intValue() * (1 - stepFactor));
                        if(byteS>2000000){
                            byteS = 2000000;
                        }else if(byteS<1024){
                            byteS = 1024;
                        }
                        byteSize.set(byteS);
                        byteSize.set(byteS);
                        System.out.println("Below threshold, New byteSize is: " + byteSize.intValue());

                    }
                    previousCount.set(count.intValue());
                }
            }, timerInitialDelayMs, timerPeriodMs);


            String fullFileName = "C:\\Users\\AbulHasan\\Videos\\MOVIES\\Bollywood\\Dil Chahta Hai.mp4";
            String[] fileArray = fullFileName.split("\\\\");
            String fileName = fileArray[fileArray.length - 1];
            FileInputStream fileInputStream = new FileInputStream(fullFileName);
            ByteString fileBytes = ByteString.readFrom(fileInputStream);
            StreamingFixedStepFactorClient client = new StreamingFixedStepFactorClient(channel);

            while (byteStart < fileBytes.size()) {
                ByteString tempBuffer = fileBytes.substring(byteStart, Math.min((byteStart + byteSize.intValue()), fileBytes.size()));
                client.uploadFile(tempBuffer, null, count.intValue(), null);
                count.incrementAndGet();
                byteStart += byteSize.intValue();
            }
            client.uploadFile(null, fileName, -1, count.decrementAndGet());
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
    public void uploadFile(ByteString fileBytes, String name, Integer index, Integer totalSize) {

        try {
            File request;
            if (fileBytes != null) {
                request = File.newBuilder().setPayload(fileBytes).setIndex(index).build();
            } else {
                request = File.newBuilder().setFilename(name).setIndex(index).setTotalsize(totalSize).build();
            }
            blockingStub.uploadFile(request);

        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: {0}" + e.getStatus());
        }
    }
}

