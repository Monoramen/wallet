package com.mono.wallet.api.controller;


import com.mono.wallet.api.dto.BalanceChangeResponseDTO;
import com.mono.wallet.api.dto.WalletRequestDto;
import com.mono.wallet.api.dto.WalletResponseDto;
import com.mono.wallet.impl.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<BalanceChangeResponseDTO> changeBalance(@RequestBody WalletRequestDto request) {
        walletService.changeBalance(request);
        BalanceChangeResponseDTO response = new BalanceChangeResponseDTO("Balance changed", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponseDto> getBalance(@PathVariable UUID walletId) {
        walletService.findWalletId(walletId);
        return ResponseEntity.ok(walletService.findWalletId(walletId));
    }

}
