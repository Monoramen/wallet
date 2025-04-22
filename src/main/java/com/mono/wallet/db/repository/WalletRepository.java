package com.mono.wallet.db.repository;

import com.mono.wallet.db.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    Optional<WalletEntity> findByWalletId(UUID walletId);

}
