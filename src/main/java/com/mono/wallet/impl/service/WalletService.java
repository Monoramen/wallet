package com.mono.wallet.impl.service;

import com.mono.wallet.api.dto.WalletBalanceResponseDto;
import com.mono.wallet.api.dto.WalletRequestDto;
import com.mono.wallet.api.dto.WalletResponseDto;

import java.util.UUID;

public interface WalletService {

    WalletBalanceResponseDto changeBalance(WalletRequestDto dto);

    WalletResponseDto findWalletId(UUID walletId);

}
