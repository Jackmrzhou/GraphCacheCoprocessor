package org.gilmour.coprocessor.CacheService;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.28.1)",
    comments = "Source: CacheServer.proto")
public final class CacheServiceGrpc {

  private CacheServiceGrpc() {}

  public static final String SERVICE_NAME = "idl.CacheService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<CacheServer.PingRequest,
      CacheServer.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = CacheServer.PingRequest.class,
      responseType = CacheServer.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<CacheServer.PingRequest,
      CacheServer.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<CacheServer.PingRequest, CacheServer.PingResponse> getPingMethod;
    if ((getPingMethod = CacheServiceGrpc.getPingMethod) == null) {
      synchronized (CacheServiceGrpc.class) {
        if ((getPingMethod = CacheServiceGrpc.getPingMethod) == null) {
          CacheServiceGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<CacheServer.PingRequest, CacheServer.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CacheServer.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CacheServer.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CacheServiceMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<CacheServer.SetValuesRequest,
      CacheServer.SetValuesResponse> getSetValuesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetValues",
      requestType = CacheServer.SetValuesRequest.class,
      responseType = CacheServer.SetValuesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<CacheServer.SetValuesRequest,
      CacheServer.SetValuesResponse> getSetValuesMethod() {
    io.grpc.MethodDescriptor<CacheServer.SetValuesRequest, CacheServer.SetValuesResponse> getSetValuesMethod;
    if ((getSetValuesMethod = CacheServiceGrpc.getSetValuesMethod) == null) {
      synchronized (CacheServiceGrpc.class) {
        if ((getSetValuesMethod = CacheServiceGrpc.getSetValuesMethod) == null) {
          CacheServiceGrpc.getSetValuesMethod = getSetValuesMethod =
              io.grpc.MethodDescriptor.<CacheServer.SetValuesRequest, CacheServer.SetValuesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetValues"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CacheServer.SetValuesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CacheServer.SetValuesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CacheServiceMethodDescriptorSupplier("SetValues"))
              .build();
        }
      }
    }
    return getSetValuesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<CacheServer.GetRowRequest,
      CacheServer.GetRowResponse> getGetRowMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRow",
      requestType = CacheServer.GetRowRequest.class,
      responseType = CacheServer.GetRowResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<CacheServer.GetRowRequest,
      CacheServer.GetRowResponse> getGetRowMethod() {
    io.grpc.MethodDescriptor<CacheServer.GetRowRequest, CacheServer.GetRowResponse> getGetRowMethod;
    if ((getGetRowMethod = CacheServiceGrpc.getGetRowMethod) == null) {
      synchronized (CacheServiceGrpc.class) {
        if ((getGetRowMethod = CacheServiceGrpc.getGetRowMethod) == null) {
          CacheServiceGrpc.getGetRowMethod = getGetRowMethod =
              io.grpc.MethodDescriptor.<CacheServer.GetRowRequest, CacheServer.GetRowResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetRow"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CacheServer.GetRowRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  CacheServer.GetRowResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CacheServiceMethodDescriptorSupplier("GetRow"))
              .build();
        }
      }
    }
    return getGetRowMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CacheServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CacheServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CacheServiceStub>() {
        @Override
        public CacheServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CacheServiceStub(channel, callOptions);
        }
      };
    return CacheServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CacheServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CacheServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CacheServiceBlockingStub>() {
        @Override
        public CacheServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CacheServiceBlockingStub(channel, callOptions);
        }
      };
    return CacheServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CacheServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CacheServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CacheServiceFutureStub>() {
        @Override
        public CacheServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CacheServiceFutureStub(channel, callOptions);
        }
      };
    return CacheServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class CacheServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void ping(CacheServer.PingRequest request,
                     io.grpc.stub.StreamObserver<CacheServer.PingResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     */
    public void setValues(CacheServer.SetValuesRequest request,
                          io.grpc.stub.StreamObserver<CacheServer.SetValuesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetValuesMethod(), responseObserver);
    }

    /**
     */
    public void getRow(CacheServer.GetRowRequest request,
                       io.grpc.stub.StreamObserver<CacheServer.GetRowResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetRowMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                CacheServer.PingRequest,
                CacheServer.PingResponse>(
                  this, METHODID_PING)))
          .addMethod(
            getSetValuesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                CacheServer.SetValuesRequest,
                CacheServer.SetValuesResponse>(
                  this, METHODID_SET_VALUES)))
          .addMethod(
            getGetRowMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                CacheServer.GetRowRequest,
                CacheServer.GetRowResponse>(
                  this, METHODID_GET_ROW)))
          .build();
    }
  }

  /**
   */
  public static final class CacheServiceStub extends io.grpc.stub.AbstractAsyncStub<CacheServiceStub> {
    private CacheServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected CacheServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CacheServiceStub(channel, callOptions);
    }

    /**
     */
    public void ping(CacheServer.PingRequest request,
                     io.grpc.stub.StreamObserver<CacheServer.PingResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setValues(CacheServer.SetValuesRequest request,
                          io.grpc.stub.StreamObserver<CacheServer.SetValuesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetValuesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRow(CacheServer.GetRowRequest request,
                       io.grpc.stub.StreamObserver<CacheServer.GetRowResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetRowMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CacheServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<CacheServiceBlockingStub> {
    private CacheServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected CacheServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CacheServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public CacheServer.PingResponse ping(CacheServer.PingRequest request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public CacheServer.SetValuesResponse setValues(CacheServer.SetValuesRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetValuesMethod(), getCallOptions(), request);
    }

    /**
     */
    public CacheServer.GetRowResponse getRow(CacheServer.GetRowRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetRowMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CacheServiceFutureStub extends io.grpc.stub.AbstractFutureStub<CacheServiceFutureStub> {
    private CacheServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected CacheServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CacheServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<CacheServer.PingResponse> ping(
        CacheServer.PingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<CacheServer.SetValuesResponse> setValues(
        CacheServer.SetValuesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetValuesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<CacheServer.GetRowResponse> getRow(
        CacheServer.GetRowRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetRowMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_SET_VALUES = 1;
  private static final int METHODID_GET_ROW = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CacheServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CacheServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((CacheServer.PingRequest) request,
              (io.grpc.stub.StreamObserver<CacheServer.PingResponse>) responseObserver);
          break;
        case METHODID_SET_VALUES:
          serviceImpl.setValues((CacheServer.SetValuesRequest) request,
              (io.grpc.stub.StreamObserver<CacheServer.SetValuesResponse>) responseObserver);
          break;
        case METHODID_GET_ROW:
          serviceImpl.getRow((CacheServer.GetRowRequest) request,
              (io.grpc.stub.StreamObserver<CacheServer.GetRowResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CacheServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CacheServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return CacheServer.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CacheService");
    }
  }

  private static final class CacheServiceFileDescriptorSupplier
      extends CacheServiceBaseDescriptorSupplier {
    CacheServiceFileDescriptorSupplier() {}
  }

  private static final class CacheServiceMethodDescriptorSupplier
      extends CacheServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CacheServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CacheServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CacheServiceFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .addMethod(getSetValuesMethod())
              .addMethod(getGetRowMethod())
              .build();
        }
      }
    }
    return result;
  }
}
