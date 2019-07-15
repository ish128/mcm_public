package com.neonex.mcp.mcm.server;

import com.neonex.mcp.mcm.server.handler.TestServiceHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class McmChannelInitializer extends ChannelInitializer<SocketChannel> {
  private final TestServiceHandler testServiceHandler;
  @Override
  protected void initChannel(SocketChannel socketChannel) {
    ChannelPipeline pipeline = socketChannel.pipeline();
    pipeline.addLast(testServiceHandler);
  }
}
