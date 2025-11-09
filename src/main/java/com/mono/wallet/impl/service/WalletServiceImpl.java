package com.mono.wallet.impl.service;

import com.mono.wallet.api.dto.WalletBalanceResponseDto;
import com.mono.wallet.api.dto.WalletRequestDto;
import com.mono.wallet.api.dto.WalletResponseDto;
import com.mono.wallet.api.exception.ApiException;
import com.mono.wallet.api.exception.ErrorType;
import com.mono.wallet.db.entity.WalletEntity;
import com.mono.wallet.db.repository.WalletRepository;
import com.mono.wallet.enums.OperationType;
import com.mono.wallet.impl.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private WalletRepository walletRepository;
    private WalletMapper walletMapper;


    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }


    @Override
    @Transactional
    public WalletBalanceResponseDto changeBalance(WalletRequestDto dto) {

        WalletEntity wallet = walletRepository.findForUpdate(dto.walletId());
        if (wallet == null) {
            throw new ApiException(ErrorType.NOT_FOUND, "Wallet not found");
        }

        BigDecimal newBalance = wallet.getBalance().add(
                dto.operationType() == OperationType.WITHDRAW ? dto.amount().negate() : dto.amount()
        );

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiException(ErrorType.BAD_REQUEST, "Insufficient funds");
        }

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        return new WalletBalanceResponseDto(dto.walletId(), newBalance);
    }


    @Override
    @Transactional(readOnly = true)
    public WalletResponseDto findWalletId(UUID walletId) {
        var wallet = walletRepository.findById(walletId).orElseThrow(() -> new ApiException(
                ErrorType.NOT_FOUND, "Wallet not found"
        ));
        return walletMapper.toResponse(wallet);
    }
}
