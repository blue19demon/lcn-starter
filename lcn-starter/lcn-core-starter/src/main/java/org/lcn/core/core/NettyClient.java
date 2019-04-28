package org.lcn.core.core;

import org.lcn.core.util.PropertiesUtils;

import com.alibaba.fastjson.JSONObject;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {
    private String serverAddress="lcn.manager.server";
	
	private String serverPort="lcn.manager.port";

	private NettyClientHandler nettyClientHandler = null;

	public String start(String host, Integer port) {
		nettyClientHandler = new NettyClientHandler();
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			bootstrap.group(group);
			bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
			bootstrap.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
					ch.pipeline().addLast(nettyClientHandler);
				}
			});
			bootstrap.connect(host, port).sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void send(JSONObject jsonObject) {
		try {
			getConntection();
			log.info("---------->"+nettyClientHandler);
			nettyClientHandler.call(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getConntection() throws Exception {
		String nettyServerAddress = (String) PropertiesUtils.getCommonYml(serverAddress);
		Integer nettyServerPort = (Integer) PropertiesUtils.getCommonYml(serverPort);
		start(nettyServerAddress, nettyServerPort);
		log.info("tx-server--->【"+nettyServerAddress+":"+nettyServerPort+"】连接上了........");
	}
}
