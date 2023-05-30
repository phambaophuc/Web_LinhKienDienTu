package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.entity.Wallet;
import DoAnJava.LinhKienDienTu.repository.IWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;


@Service
public class WalletService {
    @Autowired
    private IWalletRepository walletRepository;

    public Wallet getWalletByUserId(UUID userId) {
        return walletRepository.findWalletByUserId(userId);
    }

    public void saveWallet(Wallet wallet, User user) {
        wallet.setPrice(BigDecimal.valueOf(0));
        wallet.setUser(user);
        walletRepository.save(wallet);
    }

    public void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }
}
