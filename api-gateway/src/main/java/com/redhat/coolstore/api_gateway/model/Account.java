package com.redhat.coolstore.api_gateway.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Account implements Serializable{

	private static final long serialVersionUID = -7715307687635725866L;
	private long accountNo;
	private BigDecimal balance;
	private String status;
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
