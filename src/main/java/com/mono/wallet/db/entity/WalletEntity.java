package com.mono.wallet.db.entity;

import com.mono.wallet.enums.OperationType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name ="wallet")
public class WalletEntity {

    public WalletEntity(UUID walletId, OperationType operationType, BigDecimal balance) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.balance = balance;
    }

    public WalletEntity() {}



    @Id
    @Column(name = "wallet_id", unique = true, nullable = false)
    private UUID walletId;


    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;


    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;


    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
