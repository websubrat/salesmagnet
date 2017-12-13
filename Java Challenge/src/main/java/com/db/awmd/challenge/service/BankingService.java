package com.db.awmd.challenge.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotAvailableException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.exception.TransferException;
import com.db.awmd.challenge.repository.AccountsRepository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author subratkumar
 * Service for Banking Operation
 */
@Service
@Slf4j
public class BankingService implements IBankingService{
	
	@Getter
	private final AccountsRepository accountsRepository;
	  
	@Autowired
	public BankingService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	/**
	 * @param transfer
	 * @throws AccountNotAvailableException
	 * @throws InsufficientBalanceException
	 */
	@Override
	public void transferAmount(Transfer transfer) throws AccountNotAvailableException, InsufficientBalanceException, TransferException {
		if(transfer != null)
		{
			//check if account information been provided correctly
			if(transfer.getFromAccount() == null || transfer.getToAccount() == null)
			{
				log.error("Error in account balance transfer, Payer or Payee data not been provided correctly");
				throw new TransferException("Transaction denied, error: Payer or Payee data not been provided correctly");
			}
			//check payee and payer can not be the same
			else if(transfer.getFromAccount().equals(transfer.getToAccount()))
			{
				log.error("Error in account balance transfer, Payer and Payee account can not be the same");
				throw new TransferException("Transaction denied, error: Payer and Payee account can not be the same");
			}
			//transfer amount must be provided and must be greater then 0
			else if(transfer.getBalance() == null || transfer.getBalance().intValue() <= 0)
			{
				log.error("Error in account balance transfer, balance provided is not valid");
				throw new TransferException("Transaction denied, Please verify the amount been transfer, Non supported amount");
			}
			this.accountsRepository.transferAmount(transfer);
			//I understand the risk of reviling the account number in log, however at this moment since account name is only tracebility for this transaction, ideally a transfer should have a transaction id. 
			log.info("Transfer Successfull for "+transfer.getFromAccount() +"at"+new Date().toString());
		}
		else
		{
			log.error("Error in account balance transfer, ata inconsistent");
			throw new TransferException("Transaction denied, error: Data inconsistent");
		}
		
	}	

}
