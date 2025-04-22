package com.mono.wallet.impl.service;

import com.mono.wallet.api.dto.WalletRequestDTO;
import com.mono.wallet.api.dto.WalletResponseDTO;
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

class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletService walletService;

    private UUID walletId;
    private WalletRequestDTO walletRequestDTO;
    private WalletResponseDTO walletResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        walletId = UUID.randomUUID();
        walletRequestDTO = new WalletRequestDTO(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(10));
        walletResponseDTO = new WalletResponseDTO(walletId, OperationType.DEPOSIT, BigDecimal.TEN);
    }

    @Test
    void changeBalance_deposit() {
        var wallet = new WalletEntity(walletId, OperationType.DEPOSIT, BigDecimal.ZERO);
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponseDTO);

        WalletResponseDTO result = walletService.changeBalance(walletRequestDTO);

        assertNotNull(result);
        assertEquals(walletId, result.walletId());
        assertEquals(BigDecimal.TEN, result.balance());

        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void changeBalance_withdraw() {
        var wallet = new WalletEntity(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100));
        walletRequestDTO = new WalletRequestDTO(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(100));

        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponseDTO);

        WalletResponseDTO result = walletService.changeBalance(walletRequestDTO);
        assertNotNull(result);
        assertEquals(walletId, result.walletId());
        assertEquals(BigDecimal.valueOf(10), result.balance());

        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void findByWalletId() {

        var wallet = new WalletEntity(walletId, OperationType.DEPOSIT, BigDecimal.TEN);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponseDTO);

        WalletResponseDTO result = walletService.findByWalletId(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.walletId());
        assertEquals(BigDecimal.TEN, result.balance());
    }

    @Test
    void findByWalletId_notFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> walletService.findByWalletId(walletId));
        assertEquals("Wallet not found", exception.getMessage());
    }
}
