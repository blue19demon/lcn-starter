package org.lcn.man;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lcn.man.util.PropertiesUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class TxmanagerStarter {
	private static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;

	private static final int BIZTHREADSIZE = 100;
	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

	public static void start(String hostname, Integer port) {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                            ClassResolvers.cacheDisabled(null)));
                    ch.pipeline().addLast(new NettyServerHandler());
				}

			});
			log.info("server started,【IP】" + hostname + "【PORT】" + port);
			ChannelFuture f = bootstrap.bind(hostname, port).sync();
			f.channel().closeFuture().sync();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Bean
	public Object starter() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String nettyServerAddress = address.getHostAddress();
		Integer nettyServerPort = (Integer) PropertiesUtils.getCommonYml("lcn.manager.port");
		start(nettyServerAddress, nettyServerPort);
		return null;
	}

}

