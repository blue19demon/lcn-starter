package org.lcn.core.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.lcn.core.core.NettyClient;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class LubanTransactionManager {
	private static Map<String, LubanTransaction> txLubanTransactionMap = new HashMap<>();
	private static ThreadLocal<LubanTransaction> threadLocal = new ThreadLocal<>();
	private static ThreadLocal<Integer> threadLocaltransactionCount = new ThreadLocal<>();
	private static ThreadLocal<String> threadLocalGroupId = new ThreadLocal<>();

	private static NettyClient nettyClient=new NettyClient();
	
	public static LubanTransaction getTxLubanTransaction(String groupId) {
		return txLubanTransactionMap.get(groupId);
	}

	public static String createLubanTransactionGroup() {
		JSONObject jsonObject = new JSONObject();
		String groupId = UUID.randomUUID().toString();
		jsonObject.put("groupId", groupId);
		jsonObject.put("command", "create");
		nettyClient.send(jsonObject);
		return groupId;
	}

	public static LubanTransaction createLubanTransaction(String groupId) {
		String transactionId = UUID.randomUUID().toString();
		LubanTransaction LubanTransaction = new LubanTransaction(groupId, transactionId);
		txLubanTransactionMap.put(groupId, LubanTransaction);
		threadLocal.set(LubanTransaction);
		threadLocaltransactionCount.set(1);
		setThreadLocalGroupId(groupId);
		return LubanTransaction;
	}

	public static LubanTransaction getLubanTransaction() {
		return threadLocal.get();
	}

	public static LubanTransaction addLubanTransaction(LubanTransaction lubanTransaction, Boolean isEnd,
			TransactionType transactionType) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("groupId", lubanTransaction.getGroupId());
		jsonObject.put("transactionId", lubanTransaction.getTransactionId());
		jsonObject.put("transactionType", transactionType);
		jsonObject.put("isEnd", isEnd);
		jsonObject.put("transactionCount", addThreadLocaltransactionCount());
		jsonObject.put("command", "add");
		log.info("添加事务---------->"+JSONObject.toJSONString(jsonObject));
		nettyClient.send(jsonObject);
		return lubanTransaction;
	}

	public static Map<String, LubanTransaction> getTxLubanTransactionMap() {
		return txLubanTransactionMap;
	}

	public static Integer getThreadLocaltransactionCount() {
		return threadLocaltransactionCount.get();
	}

	public static String getThreadLocalGroupId() {
		return threadLocalGroupId.get();
	}

	public static void setThreadLocalGroupId(String groupId) {
		threadLocalGroupId.set(groupId);
	}

	public static void setThreadLocaltransactionCount(Integer c) {
		threadLocaltransactionCount.set(c);
	}

	public static Integer addThreadLocaltransactionCount() {
		Integer i = threadLocaltransactionCount.get() == null ? 0 : threadLocaltransactionCount.get() + 1;
		threadLocaltransactionCount.set(i);
		return i;
	}

}
