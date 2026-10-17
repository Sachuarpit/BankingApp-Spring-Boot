
package net.javaguides.Banking.controller;

import java.util.List;
import net.javaguides.Banking.dto.TransactionRequest;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.Banking.dto.AccountDto;
import net.javaguides.Banking.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Create a new account
    @PostMapping
    public ResponseEntity<AccountDto> addAccount(
            @Valid @RequestBody AccountDto accountDto) {

        AccountDto createdAccount =
                accountService.createAccount(accountDto);

        return new ResponseEntity<>(
                createdAccount, HttpStatus.CREATED);
    }

    // Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(
            @PathVariable Long id) {

        AccountDto accountDto =
                accountService.getAccountById(id);

        return ResponseEntity.ok(accountDto);
    }

    // Deposit money

@PutMapping("/{id}/deposit")
public ResponseEntity<AccountDto> deposit(
        @PathVariable Long id,
        @Valid @RequestBody TransactionRequest request) {

    AccountDto accountDto =
            accountService.deposit(id, request.getAmount());

    return ResponseEntity.ok(accountDto);
}

@PutMapping("/{id}/withdraw")
public ResponseEntity<AccountDto> withdraw(
        @PathVariable Long id,
        @Valid @RequestBody TransactionRequest request) {

    AccountDto accountDto =
            accountService.withdraw(id, request.getAmount());

    return ResponseEntity.ok(accountDto);
}


    // Get all accounts
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {

        List<AccountDto> accounts =
                accountService.getAllAccounts();

        return ResponseEntity.ok(accounts);
    }

    // Delete account by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(
            @PathVariable Long id) {

        accountService.deleteAccount(id);

        return ResponseEntity.ok(
                "Account is deleted successfully");
    }
}
