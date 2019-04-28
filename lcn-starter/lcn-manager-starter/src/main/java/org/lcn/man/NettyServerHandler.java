package org.lcn.man;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	private static Map<String, List<String>> transactionTypeMap = new HashMap<String, List<String>>();
	private static Map<String, Boolean> isEndMap = new HashMap<String, Boolean>();
	private static Map<String, Integer> transactionCountMap = new HashMap<String, Integer>();
	private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel=ctx.channel();
		channelGroup.add(channel);
	}
	@Override
	public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info(String.valueOf(msg));
		JSONObject jsonObject = JSONObject.parseObject((String) msg);
		String command = jsonObject.getString("command");// create ���������� add ���������
		String groupId = jsonObject.getString("groupId");// ������id
		String transactionType = jsonObject.getString("transactionType");// ����������--commit���ύ rollback���ع�
		Integer transactionCount = jsonObject.getInteger("transactionCount");// ��������
		Boolean isEnd = jsonObject.getBoolean("isEnd");// �Ƿ�Ҫ��������
		if ("create".equals(command)) {
			transactionTypeMap.put(groupId, new ArrayList<String>());
		} else if ("add".equals(command)) {
			transactionTypeMap.get(groupId).add(transactionType);
			if (isEnd) {
				isEndMap.put(groupId, true);
				transactionCountMap.put(groupId, transactionCount);
			}

			JSONObject result = new JSONObject();
			result.put("groupId", groupId);
			if (isEndMap.get(groupId)
					&& transactionCountMap.get(groupId).equals(transactionTypeMap.get(groupId).size())) {
                 if(transactionTypeMap.get(groupId).contains("rollback")) {
                	 result.put("command", "rollback");
                	 sendMessageResult(result);
                 }else {
                	 result.put("command", "commit");
                	 sendMessageResult(result);
                 }
			}
		}
	}

	private void sendMessageResult(JSONObject result) {
		for (Channel channel : channelGroup) {
			log.info(JSONObject.toJSONString(result));
			channel.writeAndFlush(result);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}