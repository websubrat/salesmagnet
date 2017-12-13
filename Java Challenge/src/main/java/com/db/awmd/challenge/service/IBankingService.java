package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotAvailableException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.exception.TransferException;

/**
 * @author subratkumar
 *
 */
public interface IBankingService {
	
	
	/**
	 * @param transfer
	 * @throws AccountNotAvailableException
	 * @throws InsufficientBalanceException
	 */
	void transferAmount(Transfer transfer)throws AccountNotAvailableException, InsufficientBalanceException, TransferException;

}
