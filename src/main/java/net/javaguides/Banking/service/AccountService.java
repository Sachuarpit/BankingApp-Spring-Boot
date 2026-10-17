package net.javaguides.Banking.service;

import java.util.List;

import net.javaguides.Banking.dto.AccountDto;

public interface AccountService {

	AccountDto createAccount(AccountDto accountDto); 
	
	AccountDto getAccountById(Long id);
	
	AccountDto deposit(Long id, double amount);
	
	AccountDto withdraw(Long id, double amount);
	
	List<AccountDto> getAllAccounts();
	
	Void deleteAccount(Long id);
}
