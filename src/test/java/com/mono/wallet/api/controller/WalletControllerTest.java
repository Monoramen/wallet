package com.mono.wallet.api.controller;

import com.mono.wallet.api.dto.WalletRequestDTO;
import com.mono.wallet.api.dto.WalletResponseDTO;
import com.mono.wallet.api.exception.ApiErrorType;
import com.mono.wallet.api.exception.ApiException;
import com.mono.wallet.api.exception.ExceptionHandlerController;
import com.mono.wallet.enums.OperationType;
import com.mono.wallet.impl.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private UUID walletId;
    private WalletRequestDTO walletRequestDTO;
    private WalletResponseDTO walletResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        View mockView = Mockito.mock(View.class);
        ExceptionHandlerController exceptionHandler = new ExceptionHandlerController(mockView);

        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(exceptionHandler)
                .build();
        walletId = UUID.randomUUID();

        walletRequestDTO = new WalletRequestDTO(walletId, OperationType.DEPOSIT, BigDecimal.TEN);
        walletResponseDTO = new WalletResponseDTO(walletId, OperationType.DEPOSIT, BigDecimal.TEN);

    }

    @Test
    void changeBalance() throws Exception {

        when(walletService.changeBalance(walletRequestDTO)).thenReturn(walletResponseDTO);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\", \"operationType\":\"DEPOSIT\", \"amount\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Balance changed"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getBalance() throws Exception {

        when(walletService.findByWalletId(walletId)).thenReturn(walletResponseDTO);

        mockMvc.perform(get("/api/v1/wallet/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(10));

    }

    @Test
    void shouldReturnBadRequestForInvalidUUID() throws Exception {
        List<String> invalidUUIDs = List.of(
                "invalid-uuid",
                "a6f8a0c1-5f03-470e",
                "a6f8a0c1-5f03-470e-9bfa-8e7b3aaf98c22"
        );

        for (String invalidUUID : invalidUUIDs) {
            mockMvc.perform(get("/api/v1/wallet/{walletId}", invalidUUID))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(""));
        }
    }


    @Test
    void shouldReturnBadRequestForInvalidOperationType() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\", \"operationType\":\"INVALID_TYPE\", \"amount\":10}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenInsufficientFunds() throws Exception {

        UUID walletId = UUID.randomUUID();
        BigDecimal withdrawalAmount = BigDecimal.valueOf(1000);

        WalletRequestDTO walletRequestDTO = new WalletRequestDTO(walletId, OperationType.WITHDRAW, withdrawalAmount);

        when(walletService.changeBalance(walletRequestDTO)).thenThrow(new ApiException(ApiErrorType.BAD_REQUEST, "Insufficient funds"));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\", \"operationType\":\"WITHDRAW\", \"amount\":1000}"))
                .andExpect(status().isBadRequest())  // Ожидаем статус 400
                .andExpect(jsonPath("$.message").value("Insufficient funds"))
                .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    void shouldReturnBadRequestForInvalidUUIDFormat() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"invalid-uuid\", \"operationType\":\"DEPOSIT\", \"amount\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid UUID format"))
                .andExpect(jsonPath("$.description").value("Please use standard 36-character UUID format"));;
    }

    @Test
    void shouldReturnBadRequestForInvalidOperationTypeFormat() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\", \"operationType\":\"INVALID\", \"amount\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid operation type: 'INVALID'"))
                .andExpect(jsonPath("$.description").value("Allowed values: [DEPOSIT, WITHDRAW]"));
    }

    @Test
    void testUnrecognizedField() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .content("{\"wallet_id\":\"...\", \"operation_type\":\"DEPOSIT\", \"invalid\":10}"))
                .andExpect(status().is4xxClientError());
    }
}