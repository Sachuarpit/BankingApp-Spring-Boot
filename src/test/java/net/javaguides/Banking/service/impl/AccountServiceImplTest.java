
package net.javaguides.Banking.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.javaguides.Banking.dto.AccountDto;
import net.javaguides.Banking.entity.Account;
import net.javaguides.Banking.exception.AccountNotFoundException;
import net.javaguides.Banking.exception.InsufficientBalanceException;
import net.javaguides.Banking.exception.InvalidAmountException;
import net.javaguides.Banking.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountHolderName("Arpit");
        account.setBalance(1000.0);
    }

    @Test
    void createAccount_shouldCreateAccount() {
        AccountDto dto = new AccountDto(null, "Arpit", 1000.0);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> {
                    Account saved = invocation.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        AccountDto result = accountService.createAccount(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Arpit", result.getAccountHolderName());
        assertEquals(1000.0, result.getBalance());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getAccountById_shouldReturnAccount() {
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        AccountDto result = accountService.getAccountById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Arpit", result.getAccountHolderName());
        assertEquals(1000.0, result.getBalance());
    }

    @Test
    void getAccountById_shouldThrowWhenAccountNotFound() {
        when(accountRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getAccountById(99L)
        );
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AccountDto result = accountService.deposit(1L, 500.0);

        assertEquals(1500.0, result.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AccountDto result = accountService.withdraw(1L, 300.0);

        assertEquals(700.0, result.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_shouldThrowWhenBalanceIsInsufficient() {
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        assertThrows(
                InsufficientBalanceException.class,
                () -> accountService.withdraw(1L, 2000.0)
        );

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void deposit_shouldRejectZeroAmount() {
        assertThrows(
                InvalidAmountException.class,
                () -> accountService.deposit(1L, 0.0)
        );

        verifyNoInteractions(accountRepository);
    }

    @Test
    void deposit_shouldRejectNegativeAmount() {
        assertThrows(
                InvalidAmountException.class,
                () -> accountService.deposit(1L, -100.0)
        );

        verifyNoInteractions(accountRepository);
    }

    @Test
    void deposit_shouldRejectNaNAmount() {
        assertThrows(
                InvalidAmountException.class,
                () -> accountService.deposit(1L, Double.NaN)
        );

        verifyNoInteractions(accountRepository);
    }

    @Test
    void deposit_shouldRejectInfiniteAmount() {
        assertThrows(
                InvalidAmountException.class,
                () -> accountService.deposit(1L, Double.POSITIVE_INFINITY)
        );

        verifyNoInteractions(accountRepository);
    }

    @Test
    void getAllAccounts_shouldReturnAllAccounts() {
        Account secondAccount = new Account();
        secondAccount.setId(2L);
        secondAccount.setAccountHolderName("Rahul");
        secondAccount.setBalance(2000.0);

        when(accountRepository.findAll())
                .thenReturn(List.of(account, secondAccount));

        List<AccountDto> result = accountService.getAllAccounts();

        assertEquals(2, result.size());
        assertEquals("Arpit", result.get(0).getAccountHolderName());
        assertEquals("Rahul", result.get(1).getAccountHolderName());
    }

    @Test
    void deleteAccount_shouldDeleteExistingAccount() {
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        verify(accountRepository).delete(account);
    }

    @Test
    void deleteAccount_shouldThrowWhenAccountNotFound() {
        when(accountRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.deleteAccount(99L)
        );

        verify(accountRepository, never()).delete(any(Account.class));
    }
}
