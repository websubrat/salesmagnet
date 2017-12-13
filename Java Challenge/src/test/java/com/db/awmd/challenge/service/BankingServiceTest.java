package com.db.awmd.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotAvailableException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.exception.TransferException;

/**
 * @author subratkumar
 * @see BankingService
 * {@link BankingService}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BankingServiceTest {

	@Autowired
	private IBankingService bankingService;
	@Autowired
	private AccountsService accountsService;

	// Verify the normal working condition..
	@Test
	public void testTransferAmount() {
		Account account1 = new Account("account1");
		account1.setBalance(new BigDecimal(1000));

		Account account2 = new Account("account2");
		account2.setBalance(new BigDecimal(100));

		this.accountsService.createAccount(account1);
		this.accountsService.createAccount(account2);

		assertThat(this.accountsService.getAccount("account1")).isEqualTo(account1);
		assertThat(this.accountsService.getAccount("account2")).isEqualTo(account2);

		// test transfer of balance from one account to another account
		Transfer transfer = new Transfer(new BigDecimal(500), "account1", "account2");
		bankingService.transferAmount(transfer);

		// verify the account balance after transfer
		assertThat(account1.getBalance()).isEqualTo(new BigDecimal(500));
		assertThat(account2.getBalance()).isEqualTo(new BigDecimal(600));

	}
	
	//very multiple transfer on two account at same time.
	@Test
	public void testTransferInMultiThreadedMode() throws InterruptedException
	{
		Account account1 = new Account("account1");
		account1.setBalance(new BigDecimal(1000));

		Account account2 = new Account("account2");
		account2.setBalance(new BigDecimal(100));

		this.accountsService.createAccount(account1);
		this.accountsService.createAccount(account2);
		
		Thread t1 = new Thread(()->{
			this.bankingService.transferAmount(new Transfer(new BigDecimal(200), "account1", "account2"));
		});
		Thread t2 = new Thread(()->{
			this.bankingService.transferAmount(new Transfer(new BigDecimal(50), "account2", "account1"));
		});
		Thread t3 = new Thread(()->{
			this.bankingService.transferAmount(new Transfer(new BigDecimal(150), "account1", "account2"));
		});
		Thread t4 = new Thread(()->{
			this.bankingService.transferAmount(new Transfer(new BigDecimal(300), "account1", "account2"));
		});
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		//the main thread should only execute after all 4 transfer have been completed
		//joining these thread only to check output at the last otherwise it is not required to perform parallel execution.
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		//Now check and verify
		//account 1 has given 200+150+300 = 650 Rs to Account 2 So 
		//at the same time account 2 has given 50 Rs to account 2
		//So account balance of account1 should be (1000+50)-650 = 400
		//So account balance of account1 should be (100+650)-50 = 700
		assertThat(account1.getBalance()).isEqualTo(new BigDecimal(400));
		assertThat(account2.getBalance()).isEqualTo(new BigDecimal(700));
	}
	
		//very multiple transfer on multiple account at same time.
		@Test
		public void testTransferInMultiAccountThreadedMode() throws InterruptedException
		{
			Account account1 = new Account("account1");
			account1.setBalance(new BigDecimal(1000));

			Account account2 = new Account("account2");
			account2.setBalance(new BigDecimal(800));
			
			Account account3 = new Account("account3");
			account3.setBalance(new BigDecimal(1000));
			
			Account account4 = new Account("account4");
			account4.setBalance(new BigDecimal(700));

			this.accountsService.createAccount(account1);
			this.accountsService.createAccount(account2);
			this.accountsService.createAccount(account3);
			this.accountsService.createAccount(account4);
			
			Thread t1 = new Thread(()->{
				this.bankingService.transferAmount(new Transfer(new BigDecimal(200), "account1", "account2"));
				this.bankingService.transferAmount(new Transfer(new BigDecimal(100), "account2", "account1"));
				this.bankingService.transferAmount(new Transfer(new BigDecimal(200), "account4", "account3"));
			});
			Thread t2 = new Thread(()->{
				this.bankingService.transferAmount(new Transfer(new BigDecimal(50), "account4", "account1"));
				this.bankingService.transferAmount(new Transfer(new BigDecimal(150), "account2", "account1"));
				this.bankingService.transferAmount(new Transfer(new BigDecimal(50), "account1", "account2"));
			});
			Thread t3 = new Thread(()->{
				this.bankingService.transferAmount(new Transfer(new BigDecimal(150), "account1", "account2"));
				this.bankingService.transferAmount(new Transfer(new BigDecimal(150), "account3", "account4"));
				this.bankingService.transferAmount(new Transfer(new BigDecimal(50), "account3", "account4"));
			});
			Thread t4 = new Thread(()->{
				this.bankingService.transferAmount(new Transfer(new BigDecimal(50), "account3", "account4"));
				this.bankingService.transferAmount(new Transfer(new BigDecimal(300), "account1", "account2"));
				this.bankingService.transferAmount(new Transfer(new BigDecimal(100), "account4", "account3"));
			});
			t1.start();
			t2.start();
			t3.start();
			t4.start();
			//the main thread should only execute after all 4 transfer have been completed
			//joining these thread only to check output at the last otherwise it is not required to perform parallel execution.
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			//Now check and verify
			//account1: initial=1000, deposit:100+50+150 = 300, withdraw:200+50+150+300 = 700, current: 600
			assertThat(account1.getBalance()).isEqualTo(new BigDecimal(600));
			//account2: initial=800, deposit:200+50+150+300 = 700, withdraw:100+150 = 250 current: 1250
			assertThat(account2.getBalance()).isEqualTo(new BigDecimal(1250));
			//account3: initial=1000, deposit:200+100= 300 withdraw:150+50+50 = 250 current: 1050
			assertThat(account3.getBalance()).isEqualTo(new BigDecimal(1050));
			//account4: initial=700, deposit:150+50+50 = 250 withdraw:200+50+100 = 350 current: 600
			assertThat(account4.getBalance()).isEqualTo(new BigDecimal(600));
		}

	// Test if both the account does not exist
	@Test
	public void testTransferAmount_BothAccountNotPresent() {

		try {
			Transfer transfer = new Transfer(new BigDecimal(500), "account1", "account2");
			bankingService.transferAmount(transfer);
			fail("Should fail if account does not exist");
		} catch (AccountNotAvailableException ex) {
			assertThat(ex.getMessage()).isEqualTo("Either payee or payer account is not available");
		}

	}

	// Test if both the account does not exist
	@Test
	public void testTransferAmount_EitherAccountNotPresent() {
		Account account1 = new Account("account1");
		account1.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account1);
		assertThat(this.accountsService.getAccount("account1")).isEqualTo(account1);
		try {
			Transfer transfer = new Transfer(new BigDecimal(500), "account1", "account2");
			bankingService.transferAmount(transfer);
			fail("Should fail if account does not exist");
		} catch (AccountNotAvailableException ex) {
			assertThat(ex.getMessage()).isEqualTo("Either payee or payer account is not available");
		}

	}

	// transfer between same account
	@Test
	public void testTransferBetweenSameAccount() {
		Account account1 = new Account("account1");
		account1.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account1);
		assertThat(this.accountsService.getAccount("account1")).isEqualTo(account1);
		try {
			// test transfer of balance between same account
			Transfer transfer = new Transfer(new BigDecimal(500), "account1", "account1");
			bankingService.transferAmount(transfer);
			fail("Should fail if payee and payer is same");
		} catch (TransferException ex) {
			assertThat(ex.getMessage())
					.isEqualTo("Transaction denied, error: Payer and Payee account can not be the same");
		}
	}

	// transfer overDraft
	@Test
	public void testTransferInSufficientBalance() {
		Account account1 = new Account("account1");
		account1.setBalance(new BigDecimal(1000));

		Account account2 = new Account("account2");
		account2.setBalance(new BigDecimal(100));

		this.accountsService.createAccount(account1);
		this.accountsService.createAccount(account2);

		assertThat(this.accountsService.getAccount("account1")).isEqualTo(account1);
		assertThat(this.accountsService.getAccount("account2")).isEqualTo(account2);

		try {
			// test transfer of balance between same account
			Transfer transfer = new Transfer(new BigDecimal(1500), "account1", "account2");
			bankingService.transferAmount(transfer);
			fail("Should fail if payer has insufficient balance");
		} catch (InsufficientBalanceException ex) {
			assertThat(ex.getMessage()).isEqualTo("Your Account Does not have sufficient balance to transfer");
		}
	}

	// transfer no amount
	@Test
	public void testTransferZeroAmount() {
		Account account1 = new Account("account1");
		account1.setBalance(new BigDecimal(1000));

		Account account2 = new Account("account2");
		account2.setBalance(new BigDecimal(100));

		this.accountsService.createAccount(account1);
		this.accountsService.createAccount(account2);

		assertThat(this.accountsService.getAccount("account1")).isEqualTo(account1);
		assertThat(this.accountsService.getAccount("account2")).isEqualTo(account2);

		try {
			// test transfer of balance between same account
			Transfer transfer = new Transfer(new BigDecimal(0), "account1", "account2");
			bankingService.transferAmount(transfer);
			fail("Should fail if payer has insufficient balance");
		} catch (TransferException ex) {
			assertThat(ex.getMessage())
					.isEqualTo("Transaction denied, Please verify the amount been transfer, Non supported amount");
		}
	}

	// transfer negative amount
	@Test
	public void testTransferNegativeAmount() {
		Account account1 = new Account("account1");
		account1.setBalance(new BigDecimal(1000));

		Account account2 = new Account("account2");
		account2.setBalance(new BigDecimal(100));

		this.accountsService.createAccount(account1);
		this.accountsService.createAccount(account2);

		assertThat(this.accountsService.getAccount("account1")).isEqualTo(account1);
		assertThat(this.accountsService.getAccount("account2")).isEqualTo(account2);

		try {
			// test transfer of balance between same account
			Transfer transfer = new Transfer(new BigDecimal(-1), "account1", "account2");
			bankingService.transferAmount(transfer);
			fail("Should fail if payer has insufficient balance");
		} catch (TransferException ex) {
			assertThat(ex.getMessage())
					.isEqualTo("Transaction denied, Please verify the amount been transfer, Non supported amount");
		}
	}

	// transfer with no value provided
	@Test
	public void testTransferAmount_NoData() {

		try {
			bankingService.transferAmount(null);
			fail("Should fail if junk value provided");
		} catch (TransferException ex) {
			assertThat(ex.getMessage()).isEqualTo("Transaction denied, error: Data inconsistent");
		}

	}

	// transfer with wrong data
	@Test
	public void testTransferWrongData() {
		try {
			// test transfer of balance between same account
			Transfer transfer = new Transfer(new BigDecimal(1500), null, "account2");
			bankingService.transferAmount(transfer);
			fail("Should fail if junk value provided");
		} catch (TransferException ex) {
			assertThat(ex.getMessage())
					.isEqualTo("Transaction denied, error: Payer or Payee data not been provided correctly");
		}
	}

	@After
	public void after() {
		// Reset the existing accounts after each test.
		accountsService.getAccountsRepository().clearAccounts();
	}

}
