package com.mono.wallet.impl.service;

import com.mono.wallet.api.dto.WalletBalanceResponseDto;
import com.mono.wallet.api.dto.WalletRequestDto;
import com.mono.wallet.api.dto.WalletResponseDto;
import com.mono.wallet.api.exception.ApiException;
import com.mono.wallet.db.entity.WalletEntity;
import com.mono.wallet.db.repository.WalletRepository;
import com.mono.wallet.enums.OperationType;
import com.mono.wallet.impl.mapper.WalletMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    private UUID walletId;
    private WalletRequestDto depositRequest;
    private WalletRequestDto withdrawRequest;
    private WalletResponseDto walletResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        walletId = UUID.randomUUID();
        depositRequest = new WalletRequestDto(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(10));
        withdrawRequest = new WalletRequestDto(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(10));
        walletResponseDto = new WalletResponseDto(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(10));
    }

    @Test
    void changeBalance_deposit_success() {
        WalletEntity wallet = new WalletEntity(walletId, OperationType.DEPOSIT, BigDecimal.ZERO);

        when(walletRepository.findForUpdate(walletId)).thenReturn(wallet);

        WalletBalanceResponseDto result = walletService.changeBalance(depositRequest);

        assertNotNull(result);
        assertEquals(walletId, result.walletId());
        assertEquals(BigDecimal.valueOf(10), result.balance());

        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void changeBalance_withdraw_success() {
        WalletEntity wallet = new WalletEntity(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100));
        WalletRequestDto request = new WalletRequestDto(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(50));

        when(walletRepository.findForUpdate(walletId)).thenReturn(wallet);

        WalletBalanceResponseDto result = walletService.changeBalance(request);

        assertNotNull(result);
        assertEquals(walletId, result.walletId());
        assertEquals(BigDecimal.valueOf(50), result.balance());

        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void changeBalance_withdraw_insufficientFunds() {
        WalletEntity wallet = new WalletEntity(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(10));
        WalletRequestDto request = new WalletRequestDto(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(20));

        when(walletRepository.findForUpdate(walletId)).thenReturn(wallet);

        ApiException exception = assertThrows(ApiException.class, () -> walletService.changeBalance(request));
        assertEquals("Insufficient funds", exception.getMessage());

        verify(walletRepository, never()).save(wallet);
    }

    @Test
    void changeBalance_walletNotFound() {
        when(walletRepository.findForUpdate(walletId)).thenReturn(null);

        ApiException exception = assertThrows(ApiException.class, () -> walletService.changeBalance(depositRequest));
        assertEquals("Wallet not found", exception.getMessage());
    }

    @Test
    void findWalletId_success() {
        WalletEntity wallet = new WalletEntity(walletId, OperationType.DEPOSIT, BigDecimal.TEN);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponseDto);

        WalletResponseDto result = walletService.findWalletId(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.walletId());
        assertEquals(BigDecimal.TEN, result.balance());
    }

    @Test
    void findWalletId_notFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> walletService.findWalletId(walletId));
        assertEquals("Wallet not found", exception.getMessage());
    }
}
