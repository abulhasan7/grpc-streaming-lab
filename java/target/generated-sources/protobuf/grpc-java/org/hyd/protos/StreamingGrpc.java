package org.hyd.protos;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.45.0)",
    comments = "Source: stream.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class StreamingGrpc {

  private StreamingGrpc() {}

  public static final String SERVICE_NAME = "stream.Streaming";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.hyd.protos.File,
      org.hyd.protos.HelloReply> getUploadFileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UploadFile",
      requestType = org.hyd.protos.File.class,
      responseType = org.hyd.protos.HelloReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.hyd.protos.File,
      org.hyd.protos.HelloReply> getUploadFileMethod() {
    io.grpc.MethodDescriptor<org.hyd.protos.File, org.hyd.protos.HelloReply> getUploadFileMethod;
    if ((getUploadFileMethod = StreamingGrpc.getUploadFileMethod) == null) {
      synchronized (StreamingGrpc.class) {
        if ((getUploadFileMethod = StreamingGrpc.getUploadFileMethod) == null) {
          StreamingGrpc.getUploadFileMethod = getUploadFileMethod =
              io.grpc.MethodDescriptor.<org.hyd.protos.File, org.hyd.protos.HelloReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UploadFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyd.protos.File.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyd.protos.HelloReply.getDefaultInstance()))
              .setSchemaDescriptor(new StreamingMethodDescriptorSupplier("UploadFile"))
              .build();
        }
      }
    }
    return getUploadFileMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StreamingStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StreamingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StreamingStub>() {
        @java.lang.Override
        public StreamingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StreamingStub(channel, callOptions);
        }
      };
    return StreamingStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StreamingBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StreamingBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StreamingBlockingStub>() {
        @java.lang.Override
        public StreamingBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StreamingBlockingStub(channel, callOptions);
        }
      };
    return StreamingBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StreamingFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StreamingFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StreamingFutureStub>() {
        @java.lang.Override
        public StreamingFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StreamingFutureStub(channel, callOptions);
        }
      };
    return StreamingFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class StreamingImplBase implements io.grpc.BindableService {

    /**
     */
    public void uploadFile(org.hyd.protos.File request,
        io.grpc.stub.StreamObserver<org.hyd.protos.HelloReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUploadFileMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getUploadFileMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.hyd.protos.File,
                org.hyd.protos.HelloReply>(
                  this, METHODID_UPLOAD_FILE)))
          .build();
    }
  }

  /**
   */
  public static final class StreamingStub extends io.grpc.stub.AbstractAsyncStub<StreamingStub> {
    private StreamingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StreamingStub(channel, callOptions);
    }

    /**
     */
    public void uploadFile(org.hyd.protos.File request,
        io.grpc.stub.StreamObserver<org.hyd.protos.HelloReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUploadFileMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class StreamingBlockingStub extends io.grpc.stub.AbstractBlockingStub<StreamingBlockingStub> {
    private StreamingBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamingBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StreamingBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.hyd.protos.HelloReply uploadFile(org.hyd.protos.File request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUploadFileMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class StreamingFutureStub extends io.grpc.stub.AbstractFutureStub<StreamingFutureStub> {
    private StreamingFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamingFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StreamingFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.hyd.protos.HelloReply> uploadFile(
        org.hyd.protos.File request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUploadFileMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_UPLOAD_FILE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StreamingImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StreamingImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UPLOAD_FILE:
          serviceImpl.uploadFile((org.hyd.protos.File) request,
              (io.grpc.stub.StreamObserver<org.hyd.protos.HelloReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class StreamingBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StreamingBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.hyd.protos.Stream.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Streaming");
    }
  }

  private static final class StreamingFileDescriptorSupplier
      extends StreamingBaseDescriptorSupplier {
    StreamingFileDescriptorSupplier() {}
  }

  private static final class StreamingMethodDescriptorSupplier
      extends StreamingBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StreamingMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StreamingGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StreamingFileDescriptorSupplier())
              .addMethod(getUploadFileMethod())
              .build();
        }
      }
    }
    return result;
  }
}
