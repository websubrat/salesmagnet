package com.db.awmd.challenge.config;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author subratkumar
 * Configuration to provide mock services for Banking
 */
@Profile("test")
@Configuration
@Slf4j
public class BankingTestConfiguration {

	/**
	 * @return mock object of notification service.
	 */
	@Bean
	@Primary
	public NotificationService notificationService() {
		NotificationService notificationService = Mockito.mock(NotificationService.class);
		Mockito.doAnswer((Answer<Void>) param -> {
			System.out.println("\n##Note:=========================>For Challange Reviewers");
			System.out.println(
					"##MOCK NOTIFICATION BEING SENT INCASE OF TEST AND NOT BEEN CALLED IF ACTUAL API BEEN CALLED##");
			Account mockac = param.getArgumentAt(0, Account.class);
			String mockDesc = param.getArgumentAt(1, String.class);
			log.info("###=====###Sending MOCK notification to owner of {}: {}", mockac.getAccountId(),
					mockDesc + "=====###\n");
			return null;
		}).when(notificationService).notifyAboutTransfer(Mockito.any(Account.class), Mockito.anyString());
		return notificationService;

	}

}
