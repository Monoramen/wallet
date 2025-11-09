package com.mono.wallet.api.controller;

import com.mono.wallet.api.dto.WalletRequestDto;
import com.mono.wallet.api.dto.WalletResponseDto;
import com.mono.wallet.api.exception.ApiException;
import com.mono.wallet.api.exception.ErrorType;
import com.mono.wallet.api.exception.ExceptionHandlerController;
import com.mono.wallet.enums.OperationType;
import com.mono.wallet.impl.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private UUID walletId;
    private WalletRequestDto depositRequest;
    private WalletResponseDto walletResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ExceptionHandlerController exceptionHandler = new ExceptionHandlerController();

        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(exceptionHandler)
                .build();

        walletId = UUID.randomUUID();
        depositRequest = new WalletRequestDto(walletId, OperationType.DEPOSIT, BigDecimal.TEN);
        walletResponseDto = new WalletResponseDto(walletId, OperationType.DEPOSIT, BigDecimal.TEN);
    }


    @Test
    void changeBalance_success() throws Exception {
        when(walletService.changeBalance(depositRequest)).thenReturn(null); // Контроллер не возвращает WalletBalanceResponseDto

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\",\"operationType\":\"DEPOSIT\",\"amount\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Balance changed"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getBalance_success() throws Exception {
        when(walletService.findWalletId(walletId)).thenReturn(walletResponseDto);

        mockMvc.perform(get("/api/v1/wallet/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(10));
    }

    @Test
    void changeBalance_insufficientFunds() throws Exception {
        UUID id = UUID.randomUUID();
        WalletRequestDto withdrawRequest = new WalletRequestDto(id, OperationType.WITHDRAW, BigDecimal.valueOf(1000));

        when(walletService.changeBalance(withdrawRequest))
                .thenThrow(new ApiException(ErrorType.BAD_REQUEST, "Insufficient funds"));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + id + "\",\"operationType\":\"WITHDRAW\",\"amount\":1000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient funds"))
                .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    void getBalance_walletNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(walletService.findWalletId(id))
                .thenThrow(new ApiException(ErrorType.NOT_FOUND, "Wallet not found"));

        mockMvc.perform(get("/api/v1/wallet/{walletId}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wallet not found"))
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    void changeBalance_invalidOperationType() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\",\"operationType\":\"INVALID\",\"amount\":10}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBalance_invalidUUID() throws Exception {
        mockMvc.perform(get("/api/v1/wallet/{walletId}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }
}
