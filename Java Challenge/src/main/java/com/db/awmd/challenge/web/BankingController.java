package com.db.awmd.challenge.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.config.BankingTestConfiguration;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotAvailableException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.exception.TransferException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.IBankingService;
import com.db.awmd.challenge.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author subratkumar
 * controller to enable transfer the amount from one account to another account
 */
@RestController
@RequestMapping("/v1/banking")
@Slf4j
public class BankingController {

	private final IBankingService bankingService;

	private final AccountsService accountsService;

	// Do not use implemented class but use interface so that we can inject mock
	// bean for test profile (see BankingTestConfiguration)
	
	/**
	 * @see BankingTestConfiguration #notificationService()
	 */
	private final NotificationService notificationService;

	@Autowired
	public BankingController(IBankingService bankingService, NotificationService notificationService,
			AccountsService accountsService) {
		this.bankingService = bankingService;
		this.notificationService = notificationService;
		this.accountsService = accountsService;
	}

	/**
	 * <b>transferAmount</b>
	 * <p> api to transfer the amount from one account to another account</p>
	 * @param transfer, The object encapsulate from account, to account and balance to be transfered.
	 * @return return 200 in case of success
	 */
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public ResponseEntity<Object> transferAmount(@RequestBody Transfer transfer) {
		log.info("Transfer amounts forom " + transfer.getFromAccount() + " to " + transfer.getToAccount() + "at");
		try {
			bankingService.transferAmount(transfer);
			// get both the accounts
			Account payee = accountsService.getAccount(transfer.getToAccount());
			Account payer = accountsService.getAccount(transfer.getFromAccount());
			if (payer != null) {
				// send Notification to Payer
				this.notificationService.notifyAboutTransfer(payer,
						"Transfer of INR" + transfer.getBalance() + " to " + transfer.getToAccount() + " Successfull ");
			}
			if (payee != null) {
				// send Notification to Payee
				this.notificationService.notifyAboutTransfer(payee,
						"INR" + transfer.getBalance() + " deposited to your account from " + transfer.getToAccount());
			}
		} catch (InsufficientBalanceException | AccountNotAvailableException | TransferException ex) {
			log.error("Error in account balance transfer " + ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
