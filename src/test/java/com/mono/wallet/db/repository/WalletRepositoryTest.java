package com.mono.wallet.db.repository;

import com.mono.wallet.db.entity.WalletEntity;
import com.mono.wallet.enums.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WalletRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(WalletRepositoryTest.class);

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        log.debug("Clearing the database before the test");
        walletRepository.deleteAll();
    }

    @Test
    void testFindByWalletIdSuccess() {
        // Arrange
        UUID walletId = UUID.randomUUID();
        WalletEntity wallet = new WalletEntity();
        wallet.setWalletId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100.00));
        wallet.setOperationType(OperationType.DEPOSIT);

        log.info("Saving wallet with ID: {}", walletId);
        walletRepository.save(wallet);

        // Act
        log.info("Searching for wallet with ID: {}", walletId);
        Optional<WalletEntity> foundWallet = walletRepository.findByWalletId(walletId);

        // Compare the fields inside the Optional
        WalletEntity foundEntity = foundWallet.get();
        assertEquals(walletId, foundEntity.getWalletId(), "Wallet ID should match");
        assertEquals(BigDecimal.valueOf(100.00), foundEntity.getBalance(), "Balance should match");
        assertEquals(OperationType.DEPOSIT, foundEntity.getOperationType(), "Operation type should match");
    }

    @Test
    void testFindByWalletIdNotFound() {
        // Arrange
        UUID walletId = UUID.randomUUID();
        log.info("Searching for a non-existing wallet with ID: {}", walletId);

        // Act
        Optional<WalletEntity> foundWallet = walletRepository.findByWalletId(walletId);

        // Assert
        assertFalse(foundWallet.isPresent(), "Wallet should not be found");
    }
}
