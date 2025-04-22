package com.mono.wallet.impl.service;

import com.mono.wallet.api.dto.WalletRequestDTO;
import com.mono.wallet.api.dto.WalletResponseDTO;
import com.mono.wallet.api.exception.ApiErrorType;
import com.mono.wallet.api.exception.ApiException;
import com.mono.wallet.db.repository.WalletRepository;
import com.mono.wallet.enums.OperationType;
import com.mono.wallet.impl.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class WalletService {
    private WalletRepository walletRepository;
    private WalletMapper walletMapper;

    @Autowired
    public WalletService(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    public WalletService() {
    }

    public WalletResponseDTO changeBalance(WalletRequestDTO dto) {

        var wallet = walletRepository.findByWalletId(dto.walletId()).orElseThrow(() -> new ApiException(
                ApiErrorType.NOT_FOUND, "Wallet not found"
        ));

        if (dto.operationType() == OperationType.WITHDRAW) {
            if (wallet.getBalance().compareTo(dto.amount()) < 0) {
                throw new ApiException(ApiErrorType.BAD_REQUEST, "Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance().subtract(dto.amount()));
        } else if (dto.operationType() == OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance().add(dto.amount()));
        }
        wallet.setOperationType(dto.operationType());

        walletRepository.save(wallet);
        return walletMapper.toResponse(wallet);
    }


    public WalletResponseDTO findByWalletId(UUID walletId) {
        var wallet = walletRepository.findById(walletId).orElseThrow(() -> new ApiException(
                ApiErrorType.NOT_FOUND, "Wallet not found"
        ));
        return walletMapper.toResponse(wallet);
    }

}