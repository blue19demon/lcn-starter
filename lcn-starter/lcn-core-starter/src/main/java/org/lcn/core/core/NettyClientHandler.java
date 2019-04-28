package org.lcn.core.core;
import org.lcn.core.bean.LubanTransaction;
import org.lcn.core.bean.LubanTransactionManager;
import org.lcn.core.bean.TransactionType;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	private ChannelHandlerContext ctx;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx=ctx;
	}
	@Override
	public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("接受到数据："+msg.toString());
		JSONObject jsonObject = (JSONObject) msg;

		String command = jsonObject.getString("command");// create 创建事务组 add 添加事务组
		String groupId = jsonObject.getString("groupId");// 事务组id
		log.info("接受到command="+command);
		LubanTransaction lubanTransaction=LubanTransactionManager.getTxLubanTransaction(groupId);
		if(command.equals("commit")) {
			lubanTransaction.setTransactionType(TransactionType.commit);
		}else {
			lubanTransaction.setTransactionType(TransactionType.rollback);
		}
		lubanTransaction.getTask().signalTask();
	}

	public synchronized void call(JSONObject json) throws Exception {
		 ctx.writeAndFlush(JSONObject.toJSONString(json));
	}
}