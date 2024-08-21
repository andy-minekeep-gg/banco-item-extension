package ovh.mythmc.bancoextensiontemplate.listeners;

import ovh.mythmc.banco.api.Banco;
import ovh.mythmc.banco.api.accounts.Account;
import ovh.mythmc.banco.api.accounts.AccountManager;
import ovh.mythmc.banco.api.event.BancoEvent;
import ovh.mythmc.banco.api.event.BancoEventListener;
import ovh.mythmc.banco.api.event.impl.BancoAccountRegisterEvent;

import java.math.BigDecimal;

public final class ExampleListener implements BancoEventListener {

    private final AccountManager accountManager = Banco.get().getAccountManager();

    @Override
    public void handle(BancoEvent event) {
        if (event instanceof BancoAccountRegisterEvent accountRegisterEvent) {
            Account account = accountRegisterEvent.account();

            accountManager.deposit(account, BigDecimal.valueOf(50));
        }
    }

}
