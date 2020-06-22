package io.springboot.netty.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.springboot.netty.service.DiscardService;
/**
 * 
 * @author Administrator
 *
 */
@Sharable
@Component
public class WebsocketMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketMessageHandler.class);
	
	@Autowired
	DiscardService discardService;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		if (msg instanceof TextWebSocketFrame) {
			TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
			// 业务层处理数据
			this.discardService.discard(textWebSocketFrame.text());
			// 响应客户端
			ctx.channel().writeAndFlush(new TextWebSocketFrame("我收到了你的消息：" + System.currentTimeMillis()));
		} else {
			// 不接受文本以外的数据帧类型
			ctx.channel().writeAndFlush(WebSocketCloseStatus.INVALID_MESSAGE_TYPE).addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		LOGGER.info("链接断开：{}", ctx.channel().remoteAddress());
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		LOGGER.info("链接创建：{}", ctx.channel().remoteAddress());
	}
}
