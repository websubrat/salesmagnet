package com.db.awmd.challenge.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotAvailableException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsRepositoryTest {

	@Autowired
	private AccountsRepository accountsRepository;

	private Account acPrimary;

	@Before
	public void before() {
		// Reset the existing accounts after each test.
		accountsRepository.clearAccounts();
		acPrimary = new Account("account_primary");
		accountsRepository.createAccount(acPrimary);
	}

	@After
	public void after() {
		// Reset primary account
		acPrimary = null;
	}

	@Test
	public void testGetAccount() {
		Account accountPrimary = this.accountsRepository.getAccount("account_primary");
		assertThat(accountPrimary).isEqualTo(acPrimary);
	}

	@Test
	public void testNullAccount() {
		Account accountPrimary = this.accountsRepository.getAccount("account_noExist");
		assertNull(accountPrimary);
	}

	@Test
	public void testCreateAccount() {
		Account account = new Account("account_1");
		accountsRepository.createAccount(account);
		assertThat(accountsRepository.getAccount("account_1")).isEqualTo(account);
		assertThat(accountsRepository.getAccount("account_1").getBalance()).isEqualTo(BigDecimal.ZERO);
	}

	@Test
	public void testCreateDuplicateAccount() {
		Account account = new Account("account_1");
		accountsRepository.createAccount(account);
		assertThat(accountsRepository.getAccount("account_1")).isEqualTo(account);
		assertThat(accountsRepository.getAccount("account_1").getBalance()).isEqualTo(BigDecimal.ZERO);

		// Again create one more account with same account name
		try {
			this.accountsRepository.createAccount(account);
			fail("Should fail when we add duplicate account");
		} catch (DuplicateAccountIdException ex) {
			assertThat(ex.getMessage()).isEqualTo("Account id account_1 already exists!");
		}
	}

	@Test
	public void testTransferAmount() {
		Account account1 = new Account("account_1");
		accountsRepository.createAccount(account1);
		assertThat(accountsRepository.getAccount("account_1")).isEqualTo(account1);
		assertThat(accountsRepository.getAccount("account_1").getBalance()).isEqualTo(BigDecimal.ZERO);

		Account account2 = new Account("account_2");
		account2.setBalance(new BigDecimal(1000));
		accountsRepository.createAccount(account2);
		assertThat(accountsRepository.getAccount("account_2")).isEqualTo(account2);
		assertThat(accountsRepository.getAccount("account_2").getBalance()).isEqualTo(new BigDecimal(1000));

		// transfer the amount..
		Transfer transfer = new Transfer(new BigDecimal(500), "account_2", "account_1");
		accountsRepository.transferAmount(transfer);
		assertThat(accountsRepository.getAccount("account_1").getBalance()).isEqualTo(new BigDecimal(500));
		assertThat(accountsRepository.getAccount("account_2").getBalance()).isEqualTo(new BigDecimal(500));

	}

	@Test
	public void testTransferFromWrongAccount() {

		try {
			Transfer transfer = new Transfer(new BigDecimal(500), "account_2", "account_1");
			accountsRepository.transferAmount(transfer);
			fail("Should fail when we add duplicate account");
		} catch (AccountNotAvailableException ex) {
			assertThat(ex.getMessage()).isEqualTo("Either payee or payer account is not available");
		}

	}

}
