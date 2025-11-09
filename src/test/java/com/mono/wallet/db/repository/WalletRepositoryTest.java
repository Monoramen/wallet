package com.mono.wallet.db.repository;

import com.mono.wallet.db.entity.WalletEntity;
import com.mono.wallet.enums.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class WalletRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private WalletRepository walletRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

@BeforeEach
    void setUp() {
        walletRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        UUID walletId = UUID.randomUUID();
        WalletEntity wallet = new WalletEntity();
        wallet.setWalletId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setOperationType(OperationType.DEPOSIT);

        walletRepository.save(wallet);

        Optional<WalletEntity> foundWallet = walletRepository.findById(walletId);
        assertTrue(foundWallet.isPresent(), "Wallet should be found by ID");

        WalletEntity entity = foundWallet.get();
        assertEquals(walletId, entity.getWalletId());
        assertEquals(BigDecimal.valueOf(100), entity.getBalance());
        assertEquals(OperationType.DEPOSIT, entity.getOperationType());
    }

    @Test
    void testFindForUpdate() {
        UUID walletId = UUID.randomUUID();
        WalletEntity wallet = new WalletEntity();
        wallet.setWalletId(walletId);
        wallet.setBalance(BigDecimal.valueOf(200));
        wallet.setOperationType(OperationType.DEPOSIT);

        walletRepository.save(wallet);

        WalletEntity lockedWallet = walletRepository.findForUpdate(walletId);
        assertNotNull(lockedWallet, "Wallet should be found for update");
        assertEquals(walletId, lockedWallet.getWalletId());
        assertEquals(BigDecimal.valueOf(200), lockedWallet.getBalance());
        assertEquals(OperationType.DEPOSIT, lockedWallet.getOperationType());
    }

    @Test
    void testFindForUpdateNotFound() {
        UUID walletId = UUID.randomUUID();
        WalletEntity lockedWallet = walletRepository.findForUpdate(walletId);
        assertNull(lockedWallet, "findForUpdate should return null if wallet does not exist");
    }
}
