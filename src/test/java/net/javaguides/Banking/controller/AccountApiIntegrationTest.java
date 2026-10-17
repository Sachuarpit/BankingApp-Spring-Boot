
package net.javaguides.Banking.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Helper: create account through the actual API
    private Long createAccount(String name, double balance)
            throws Exception {

        MvcResult result = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "accountHolderName": "%s",
                        "balance": %s
                    }
                    """.formatted(name, balance)))
                .andExpect(status().isCreated())
                .andReturn();

        String response =
                result.getResponse().getContentAsString();

        return Long.valueOf(
                JsonPath.read(response, "$.id").toString());
    }

    @Test
    void shouldCreateAccount() throws Exception {

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "accountHolderName": "Create Test User",
                        "balance": 1000.0
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountHolderName")
                        .value("Create Test User"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void shouldGetAccountById() throws Exception {

        Long id = createAccount("Get Test User", 1000.0);

        mockMvc.perform(get("/api/accounts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.accountHolderName")
                        .value("Get Test User"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void shouldDepositMoney() throws Exception {

        Long id = createAccount("Deposit Test User", 1000.0);

        mockMvc.perform(put("/api/accounts/" + id + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "amount": 500.0
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.0));
    }

    @Test
    void shouldWithdrawMoney() throws Exception {

        Long id = createAccount("Withdraw Test User", 1000.0);

        mockMvc.perform(put("/api/accounts/" + id + "/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "amount": 200.0
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800.0));
    }

    @Test
    void shouldGetAllAccounts() throws Exception {

        createAccount("List Test User", 1000.0);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldDeleteAccount() throws Exception {

        Long id = createAccount("Delete Test User", 500.0);

        mockMvc.perform(delete("/api/accounts/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Account is deleted successfully"));

        mockMvc.perform(get("/api/accounts/" + id))
                .andExpect(status().isNotFound());
    }
}
