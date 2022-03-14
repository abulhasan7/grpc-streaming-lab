package org.hyd.stream;

import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.hyd.protos.File;
import org.hyd.protos.Reply;
import org.hyd.protos.StreamingGrpc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StreamingRecipientServer {
    private static final Logger logger = Logger.getLogger(StreamingRecipientServer.class.getName());

    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new StreamingImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    StreamingRecipientServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final StreamingRecipientServer server = new StreamingRecipientServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class StreamingImpl extends StreamingGrpc.StreamingImplBase {
        Map<Integer, ByteString> fileBuffObj = new ConcurrentHashMap<>();
        Integer size = 0;
        String fileName = "";
        Instant startTime = null;
        boolean firstReqReceived = false;

        @Override
        public void uploadFile(File req, StreamObserver<Reply> responseObserver) {
            try {
                System.out.println("Received Request: " + req.getIndex());
                if (firstReqReceived == false) {
                    startTime = Instant.now();
                    System.out.println("startTime: " + startTime);
                    firstReqReceived = true;
                }
                if (req.getIndex() != -1) {
                    fileBuffObj.put(req.getIndex(), req.getPayload());
                }
                if (req.getIndex() == -1) {
                    size = req.getTotalsize();
                    fileName = req.getFilename();
                }
                if (size == fileBuffObj.size()) {
                    Instant endTime = Instant.now();
                    System.out.println("endTime: " + endTime);
                    Duration dur = Duration.between(startTime, endTime);
                    System.out.println("Time: " + dur.toMillis());
                    //System.out.println("Path : " + (FileSystems.getDefault().getPath("") + fileName));
                    Path path = Paths.get(FileSystems.getDefault().getPath(".") + "/" + fileName);
                    System.out.println(path.toString());
                    Files.createFile(path);

                    FileOutputStream fileOutputStream = new FileOutputStream(path.toString());
                    for (int i = 1; i <= fileBuffObj.size(); i++) {
                        ByteString tempChunk = fileBuffObj.get(i);
                        //System.out.println("Chunk : " + i + " " + tempChunk);
                        //System.out.println(fileOutputStream);
                        tempChunk.writeTo(fileOutputStream);
                        fileOutputStream.flush();
                    }
                    fileOutputStream.close();
                }
                Reply reply = Reply.newBuilder().setMessage("Received " + req.getIndex()).build();
                responseObserver.onNext(reply);
                responseObserver.onCompleted();
            }
            catch(IOException exception) {
                System.err.println(exception);
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
