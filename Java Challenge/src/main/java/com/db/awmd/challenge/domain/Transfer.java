package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Transfer {
	
	
	@NotNull
	@Min(value = 0, message = "Transfer Amount must be positive.")
	private BigDecimal balance;
	
	@NotNull
	private String fromAccount;
	
	@NotNull
	private String toAccount;

	@JsonCreator
	public Transfer(@JsonProperty("balance") BigDecimal balance, 
			@JsonProperty("fromAccount") String fromAccount, 
			@JsonProperty("toAccount") String toAccount) {
		super();
		this.balance = balance;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
	
	

}
