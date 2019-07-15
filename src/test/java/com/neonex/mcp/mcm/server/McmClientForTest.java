package com.neonex.mcp.mcm.server;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class McmClientForTest {

  private static final String HOST = "127.0.0.1";
  private static final int PORT = 8081;


  public static void main(String[] args) {
    EventLoopGroup group = new NioEventLoopGroup();

    try{
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
              ChannelPipeline cp = sc.pipeline();
              cp.addLast(new StringDecoder());
              cp.addLast(new ChannelInboundHandlerAdapter() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                  ByteBuf in = (ByteBuf) msg;
                  try {
                    while (in.isReadable()) { // (1)
                      System.out.print((char) in.readByte());
                      System.out.flush();
                    }
                  } finally {
                    ReferenceCountUtil.release(msg); // (2)
                  }
                }
              });
            }
          });

      ChannelFuture cf = b.connect(McmClientForTest.HOST, McmClientForTest.PORT).sync();
      cf.channel().closeFuture().sync();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      group.shutdownGracefully();
    }
  }
}


