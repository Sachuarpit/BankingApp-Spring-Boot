
package net.javaguides.Banking.controller;

import java.util.List;

import net.javaguides.Banking.dto.AccountDto;
import net.javaguides.Banking.dto.TransactionRequest;
import net.javaguides.Banking.service.AccountService;
import static org.mockito.Mockito.verifyNoInteractions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import net.javaguides.Banking.exception.AccountNotFoundException;
import net.javaguides.Banking.exception.GlobalExceptionHandler;
import net.javaguides.Banking.exception.InsufficientBalanceException;
import net.javaguides.Banking.exception.InvalidAmountException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;


@BeforeEach
void setUp() {
    mockMvc = MockMvcBuilders
            .standaloneSetup(accountController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
}


    @Test
    void testCreateAccount() throws Exception {

        AccountDto dto =
                new AccountDto(1L, "Arpit", 1000.0);

        when(accountService.createAccount(
                org.mockito.ArgumentMatchers.any(AccountDto.class)))
                .thenReturn(dto);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "accountHolderName": "Arpit",
                        "balance": 1000.0
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountHolderName").value("Arpit"))
                .andExpect(jsonPath("$.balance").value(1000.0));

        verify(accountService).createAccount(
                org.mockito.ArgumentMatchers.any(AccountDto.class));
    }

    @Test
    void testGetAccountById() throws Exception {

        AccountDto dto =
                new AccountDto(1L, "Arpit", 1000.0);

        when(accountService.getAccountById(1L))
                .thenReturn(dto);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountHolderName").value("Arpit"))
                .andExpect(jsonPath("$.balance").value(1000.0));

        verify(accountService).getAccountById(1L);
    }

    @Test
    void testDeposit() throws Exception {

        AccountDto dto =
                new AccountDto(1L, "Arpit", 1500.0);

        when(accountService.deposit(1L, 500.0))
                .thenReturn(dto);

        mockMvc.perform(put("/api/accounts/1/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "amount": 500.0
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.0));

        verify(accountService).deposit(1L, 500.0);
    }

    @Test
    void testWithdraw() throws Exception {

        AccountDto dto =
                new AccountDto(1L, "Arpit", 700.0);

        when(accountService.withdraw(1L, 300.0))
                .thenReturn(dto);

        mockMvc.perform(put("/api/accounts/1/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "amount": 300.0
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(700.0));

        verify(accountService).withdraw(1L, 300.0);
    }

    @Test
    void testGetAllAccounts() throws Exception {

        List<AccountDto> accounts = List.of(
                new AccountDto(1L, "Arpit", 1000.0),
                new AccountDto(2L, "Rahul", 2000.0)
        );

        when(accountService.getAllAccounts())
                .thenReturn(accounts);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].accountHolderName").value("Arpit"))
                .andExpect(jsonPath("$[1].accountHolderName").value("Rahul"));

        verify(accountService).getAllAccounts();
    }

    @Test
    void testDeleteAccount() throws Exception {

        mockMvc.perform(delete("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Account is deleted successfully"));

        verify(accountService).deleteAccount(1L);
    }
    

@Test
void testCreateAccountRejectsBlankName() throws Exception {

    mockMvc.perform(post("/api/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "accountHolderName": "   ",
                    "balance": 1000.0
                }
                """))
            .andExpect(status().isBadRequest());

    verifyNoInteractions(accountService);
}

@Test
void testCreateAccountRejectsNegativeBalance() throws Exception {

    mockMvc.perform(post("/api/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "accountHolderName": "Arpit",
                    "balance": -100.0
                }
                """))
            .andExpect(status().isBadRequest());

    verifyNoInteractions(accountService);
}

@Test
void testDepositRejectsZeroAmount() throws Exception {

    mockMvc.perform(put("/api/accounts/1/deposit")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "amount": 0
                }
                """))
            .andExpect(status().isBadRequest());

    verifyNoInteractions(accountService);
}

@Test
void testWithdrawRejectsNegativeAmount() throws Exception {

    mockMvc.perform(put("/api/accounts/1/withdraw")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "amount": -100
                }
                """))
            .andExpect(status().isBadRequest());

    verifyNoInteractions(accountService);
}

@Test
void testDepositRejectsMissingAmount() throws Exception {

    mockMvc.perform(put("/api/accounts/1/deposit")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {}
                """))
            .andExpect(status().isBadRequest());

    verifyNoInteractions(accountService);
}


@Test
void testAccountNotFoundReturns404() throws Exception {

    doThrow(new AccountNotFoundException(
            "Account not found with ID: 99"))
            .when(accountService).getAccountById(99L);

    mockMvc.perform(get("/api/accounts/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message")
                    .value("Account not found with ID: 99"))
            .andExpect(jsonPath("$.path").value("/api/accounts/99"))
            .andExpect(jsonPath("$.timestamp").exists());
}

@Test
void testInsufficientBalanceReturns409() throws Exception {

    doThrow(new InsufficientBalanceException(
            "Insufficient balance for account ID: 1"))
            .when(accountService).withdraw(1L, 300.0);

    mockMvc.perform(put("/api/accounts/1/withdraw")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "amount": 300.0
                }
                """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.error").value("Conflict"))
            .andExpect(jsonPath("$.message")
                    .value("Insufficient balance for account ID: 1"))
            .andExpect(jsonPath("$.path").value("/api/accounts/1/withdraw"));
}

@Test
void testInvalidAmountExceptionReturns400() throws Exception {

    doThrow(new InvalidAmountException(
            "Amount must be a positive, finite number"))
            .when(accountService).deposit(1L, 10.0);

    mockMvc.perform(put("/api/accounts/1/deposit")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "amount": 10.0
                }
                """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message")
                    .value("Amount must be a positive, finite number"))
            .andExpect(jsonPath("$.path").value("/api/accounts/1/deposit"));
}
}
