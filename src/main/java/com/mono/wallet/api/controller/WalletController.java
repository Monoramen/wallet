package com.mono.wallet.api.controller;

import com.mono.wallet.api.dto.BalanceChangeResponseDTO;
import com.mono.wallet.api.dto.WalletRequestDTO;
import com.mono.wallet.api.dto.WalletResponseDTO;
import com.mono.wallet.impl.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<BalanceChangeResponseDTO> changeBalance(@RequestBody WalletRequestDTO request) {
        walletService.changeBalance(request);
        BalanceChangeResponseDTO response = new BalanceChangeResponseDTO("Balance changed", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponseDTO> getBalance(@PathVariable UUID walletId) {
        walletService.findByWalletId(walletId);
        return ResponseEntity.ok(walletService.findByWalletId(walletId));
    }
}
