package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotAvailableException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;

/**
 * @author subratkumar
 *
 */
public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);

  void clearAccounts();
  
  
  /**
 * transfer balance from one account to another account
 * @param transfer
 * @throws InsufficientBalanceException
 * @throws AccountNotAvailableException
 */
void transferAmount(Transfer transfer)throws InsufficientBalanceException, AccountNotAvailableException;
}
