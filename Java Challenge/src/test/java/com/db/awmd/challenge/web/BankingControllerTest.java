package com.db.awmd.challenge.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;

/**
 * @author subratkumar
 * Test class for BankingController
 * @see BankingController
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BankingControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	private AccountsService accountsService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	

	@Before
	public void prepareMockMvc() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

		// Reset the existing accounts before each test.
		accountsService.getAccountsRepository().clearAccounts();
	}
	
	
	//Test Transfer Amount
	@Test
	public void transferAmount() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account1\",\"balance\":1000}")).andExpect(status().isCreated());
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account2\",\"balance\":500}")).andExpect(status().isCreated());
		Account account1 = accountsService.getAccount("account1");
		Account account2 = accountsService.getAccount("account2");
		assertThat(account1.getAccountId()).isEqualTo("account1");
		assertThat(account1.getBalance()).isEqualByComparingTo("1000");
		assertThat(account2.getAccountId()).isEqualTo("account2");
		assertThat(account2.getBalance()).isEqualByComparingTo("500");
		
		//Transfer the amount.
		this.mockMvc.perform(post("/v1/banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromAccount\":\"account1\",\"toAccount\":\"account2\",\"balance\":300}")).andExpect(status().isOk());
		
		//Now check the balance of both the account..
		//verify the account balance after transfer
	    assertThat(account1.getBalance().equals(new BigDecimal(700)));
	    assertThat(account1.getBalance().equals(new BigDecimal(800)));
	    
	}
	
	//Test transfer to unavailable account
	@Test
	public void testTransferAmount_BothAccountNotPresent() throws Exception
	{
		this.mockMvc.perform(post("/v1/banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromAccount\":\"account1\",\"toAccount\":\"account2\",\"balance\":300}")).andExpect(status().isBadRequest());
	}
	
	//Test transfer to unavailable account
	@Test
	public void testTransferAmount_oneAccountNotPresent() throws Exception
	{
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account1\",\"balance\":1000}")).andExpect(status().isCreated());
		Account account1 = accountsService.getAccount("account1");
		assertThat(account1.getAccountId()).isEqualTo("account1");
		assertThat(account1.getBalance()).isEqualByComparingTo("1000");
		this.mockMvc.perform(post("/v1/banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromAccount\":\"account1\",\"toAccount\":\"account2\",\"balance\":300}")).andExpect(status().isBadRequest());
	}
	
	//transfer between same account
	@Test
	public void testTransferBetweenSameAccount()throws Exception
	{
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account1\",\"balance\":1000}")).andExpect(status().isCreated());
		Account account1 = accountsService.getAccount("account1");
		assertThat(account1.getAccountId()).isEqualTo("account1");
		assertThat(account1.getBalance()).isEqualByComparingTo("1000");
		
		this.mockMvc.perform(post("/v1/banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromAccount\":\"account1\",\"toAccount\":\"account1\",\"balance\":300}")).andExpect(status().isBadRequest());
	}
	
	
	//transfer with Insufficient balance
	@Test
	public void transferWithInsufficientBalance() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account1\",\"balance\":1000}")).andExpect(status().isCreated());
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account2\",\"balance\":500}")).andExpect(status().isCreated());
		Account account1 = accountsService.getAccount("account1");
		Account account2 = accountsService.getAccount("account2");
		assertThat(account1.getAccountId()).isEqualTo("account1");
		assertThat(account1.getBalance()).isEqualByComparingTo("1000");
		assertThat(account2.getAccountId()).isEqualTo("account2");
		assertThat(account2.getBalance()).isEqualByComparingTo("500");
		
		//Transfer the amount.
		this.mockMvc.perform(post("/v1/banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromAccount\":\"account1\",\"toAccount\":\"account2\",\"balance\":5000}")).andExpect(status().isBadRequest());
		
		//Now check the balance of both the account..
		//verify the account balance after transfer
	    assertThat(account1.getBalance().equals(new BigDecimal(1000)));
	    assertThat(account1.getBalance().equals(new BigDecimal(500)));
	    
	}
	
	//transfer with zero balance
	@Test
	public void transferZeroBalance() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account1\",\"balance\":1000}")).andExpect(status().isCreated());
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account2\",\"balance\":500}")).andExpect(status().isCreated());
		Account account1 = accountsService.getAccount("account1");
		Account account2 = accountsService.getAccount("account2");
		assertThat(account1.getAccountId()).isEqualTo("account1");
		assertThat(account1.getBalance()).isEqualByComparingTo("1000");
		assertThat(account2.getAccountId()).isEqualTo("account2");
		assertThat(account2.getBalance()).isEqualByComparingTo("500");
		
		//Transfer the amount.
		this.mockMvc.perform(post("/v1/banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromAccount\":\"account1\",\"toAccount\":\"account2\",\"balance\":0}")).andExpect(status().isBadRequest());
		
		//Now check the balance of both the account..
		//verify the account balance after transfer
	    assertThat(account1.getBalance().equals(new BigDecimal(1000)));
	    assertThat(account1.getBalance().equals(new BigDecimal(500)));
	    
	}
	
	//transfer with Negative value
	@Test
	public void transferNegative() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account1\",\"balance\":1000}")).andExpect(status().isCreated());
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"account2\",\"balance\":500}")).andExpect(status().isCreated());
		Account account1 = accountsService.getAccount("account1");
		Account account2 = accountsService.getAccount("account2");
		assertThat(account1.getAccountId()).isEqualTo("account1");
		assertThat(account1.getBalance()).isEqualByComparingTo("1000");
		assertThat(account2.getAccountId()).isEqualTo("account2");
		assertThat(account2.getBalance()).isEqualByComparingTo("500");
		
		//Transfer the amount.
		this.mockMvc.perform(post("/v1/banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fromAccount\":\"account1\",\"toAccount\":\"account2\",\"balance\":-1}")).andExpect(status().isBadRequest());
		
		//Now check the balance of both the account..
		//verify the account balance after transfer
	    assertThat(account1.getBalance().equals(new BigDecimal(1000)));
	    assertThat(account1.getBalance().equals(new BigDecimal(500)));
	    
	}
	
	//transfer with bad request body.
	@Test
	public void transferWithBadBody() throws Exception {
		
		//Transfer the amount.
		this.mockMvc.perform(post("/v1/banking/transfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"garbage\":\"tes\",\"anything\":\"garbage\",\"garbage\":somthing}")).andExpect(status().isBadRequest());
		
	    
	}
}
