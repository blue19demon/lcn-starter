package org.lcn.core.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.lcn.core.core.NettyClient;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class LCNTransactionManager {
	private static Map<String, LCNTransaction> txLcnTransactionMap = new HashMap<>();
	private static ThreadLocal<LCNTransaction> threadLocal = new ThreadLocal<>();
	private static ThreadLocal<Integer> threadLocaltransactionCount = new ThreadLocal<>();
	private static ThreadLocal<String> threadLocalGroupId = new ThreadLocal<>();

	private static NettyClient nettyClient=new NettyClient();
	
	public static LCNTransaction getTxLcnTransaction(String groupId) {
		return txLcnTransactionMap.get(groupId);
	}

	public static String createLcnTransactionGroup() {
		JSONObject jsonObject = new JSONObject();
		String groupId = UUID.randomUUID().toString();
		jsonObject.put("groupId", groupId);
		jsonObject.put("command", "create");
		nettyClient.send(jsonObject);
		return groupId;
	}

	public static LCNTransaction createLcnTransaction(String groupId) {
		String transactionId = UUID.randomUUID().toString();
		LCNTransaction lcnTransaction = new LCNTransaction(groupId, transactionId);
		txLcnTransactionMap.put(groupId, lcnTransaction);
		threadLocal.set(lcnTransaction);
		threadLocaltransactionCount.set(1);
		setThreadLocalGroupId(groupId);
		return lcnTransaction;
	}

	public static LCNTransaction getLcnTransaction() {
		return threadLocal.get();
	}

	public static LCNTransaction addLcnTransaction(LCNTransaction lcnTransaction, Boolean isEnd,
			TransactionType transactionType) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("groupId", lcnTransaction.getGroupId());
		jsonObject.put("transactionId", lcnTransaction.getTransactionId());
		jsonObject.put("transactionType", transactionType);
		jsonObject.put("isEnd", isEnd);
		jsonObject.put("transactionCount", addThreadLocaltransactionCount());
		jsonObject.put("command", "add");
		log.info("添加事务---------->"+JSONObject.toJSONString(jsonObject));
		nettyClient.send(jsonObject);
		return lcnTransaction;
	}

	public static Map<String, LCNTransaction> getTxLcnTransactionMap() {
		return txLcnTransactionMap;
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
		Integer counter = threadLocaltransactionCount.get() == null ? 0 : threadLocaltransactionCount.get() + 1;
		threadLocaltransactionCount.set(counter);
		return 3;//threadLocaltransactionCount.get();
	}

}
