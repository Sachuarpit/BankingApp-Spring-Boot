
package net.javaguides.Banking.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import net.javaguides.Banking.entity.Account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldSaveAndFindAccount() {

        Account account =
                new Account(null, "Integration Test User", 1000.0);

        Account savedAccount = accountRepository.save(account);

        assertNotNull(savedAccount.getId());

        Account foundAccount = accountRepository
                .findById(savedAccount.getId())
                .orElseThrow();

        assertEquals(
                "Integration Test User",
                foundAccount.getAccountHolderName());

        assertEquals(1000.0, foundAccount.getBalance());
    }
    

@Test
void shouldUpdateAccountBalance() {

    Account account =
            new Account(null, "Update Test User", 1000.0);

    Account savedAccount = accountRepository.save(account);

    savedAccount.setBalance(1500.0);
    accountRepository.save(savedAccount);

    Account updatedAccount = accountRepository
            .findById(savedAccount.getId())
            .orElseThrow();

    assertEquals(1500.0, updatedAccount.getBalance());
}

@Test
void shouldDeleteAccount() {

    Account account =
            new Account(null, "Delete Test User", 500.0);

    Account savedAccount = accountRepository.save(account);
    Long accountId = savedAccount.getId();

    accountRepository.deleteById(accountId);

    assertEquals(false, accountRepository.existsById(accountId));
}

}
