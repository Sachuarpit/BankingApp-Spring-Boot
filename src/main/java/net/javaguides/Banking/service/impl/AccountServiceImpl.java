
package net.javaguides.Banking.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.javaguides.Banking.dto.AccountDto;
import net.javaguides.Banking.entity.Account;
import net.javaguides.Banking.exception.AccountNotFoundException;
import net.javaguides.Banking.exception.InsufficientBalanceException;
import net.javaguides.Banking.exception.InvalidAmountException;
import net.javaguides.Banking.mapper.AccountMapper;
import net.javaguides.Banking.repository.AccountRepository;
import net.javaguides.Banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = findAccountById(id);

        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    @Transactional
    public AccountDto deposit(Long id, double amount) {
        validateAmount(amount);

        Account account = findAccountById(id);

        account.setBalance(account.getBalance() + amount);

        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDto withdraw(Long id, double amount) {
        validateAmount(amount);

        Account account = findAccountById(id);

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException(
                "Insufficient balance for account ID: " + id
            );
        }

        account.setBalance(account.getBalance() - amount);

        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::mapToAccountDto)
                .toList();
    }

    @Override
    public Void deleteAccount(Long id) {
        Account account = findAccountById(id);
        accountRepository.delete(account);

        return null;
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() ->
                    new AccountNotFoundException(
                        "Account not found with ID: " + id
                    )
                );
    }

    private void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException(
                "Amount must be a positive, finite number"
            );
        }
    }
}
