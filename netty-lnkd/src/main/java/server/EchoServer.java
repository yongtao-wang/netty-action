package server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
// import io.netty.handler.codec.LineBasedFrameDecoder;
// import io.netty.handler.codec.string.StringDecoder;
// import io.netty.handler.codec.string.StringEncoder;
// import io.netty.util.CharsetUtil;

public class EchoServer {
    private final int port;
    // private final int max_length = 2048;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }
        new EchoServer(Integer.parseInt(args[0])).start();
    }

    public void start() throws Exception {
        System.out.println("Server started: port " + port + "\n");
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            final EchoServerHandler serverHandler = new EchoServerHandler();
            // create event loop
            bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                            // new StringEncoder(CharsetUtil.UTF_8),
                            // new LineBasedFrameDecoder(max_length),
                            // new StringDecoder(CharsetUtil.UTF_8),
                            serverHandler
                        );
                    }
                });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

}
