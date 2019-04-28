package org.lcn.core.bean;

public class LubanTransaction {

	private String groupId;
	private String transactionId;
	private TransactionType transactionType;
	private Task task;
	
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public LubanTransaction() {
		super();
	}
	public LubanTransaction(String groupId, String transactionId) {
		super();
		this.groupId = groupId;
		this.transactionId = transactionId;
		this.task=new Task();
	}
	public LubanTransaction(String groupId, String transactionId, TransactionType transactionType) {
		super();
		this.groupId = groupId;
		this.transactionId = transactionId;
		this.transactionType = transactionType;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	
}
