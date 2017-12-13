package com.db.awmd.challenge.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotAvailableException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public void createAccount(Account account) throws DuplicateAccountIdException {
		Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public Account getAccount(String accountId) {
		return accounts.get(accountId);
	}

	@Override
	public void clearAccounts() {
		accounts.clear();
	}

	/* (non-Javadoc)
	 * @see com.db.awmd.challenge.repository.AccountsRepository#transferAmount(com.db.awmd.challenge.domain.Transfer)
	 */
	@Override
	public void transferAmount(Transfer transfer) throws InsufficientBalanceException, AccountNotAvailableException {
		Account payer = accounts.get(transfer.getFromAccount());
		Account payee = accounts.get(transfer.getToAccount());

		// check if payer or payee account is available
		if (payer == null || payee == null) {
			// throw exception of either account not available...
			throw new AccountNotAvailableException("Either payee or payer account is not available");
		}

		// Keeping object level (in this case payer) lock so that no other operation
		// (deposit/withdraw) allowed over that payer, however other accounts are free
		// for any other operation like deposit/withdraw/transfer
		// Note: do not kept lock on AccountsRepositoryInMemory (this) as that will
		// block the operation on other account and that would have been a blocking
		// solution.
		synchronized (payer) {
			// check the balance and withdraw the amount from payer account
			if (payer.getBalance().compareTo(transfer.getBalance()) >= 0) {
				payer.withdraw(transfer.getBalance());
			} else {
				// in case of insufficient balance throw exception
				throw new InsufficientBalanceException("Your Account Does not have sufficient balance to transfer");
			}
		}
		// Keeping object level (in this case payee) lock so that no other operation
		// (deposit/withdraw) allowed over that payer, however other accounts are free
		// for any other operation like deposit/withdraw/transfer
		synchronized (payee) {
			payee.deposit(transfer.getBalance());
		}

	}

}
