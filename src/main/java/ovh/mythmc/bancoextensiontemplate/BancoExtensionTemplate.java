package ovh.mythmc.bancoextensiontemplate;

import org.bukkit.plugin.java.JavaPlugin;
import ovh.mythmc.banco.api.Banco;
import ovh.mythmc.banco.api.callback.transaction.BancoTransactionProcessCallback;
import ovh.mythmc.bancoextensiontemplate.inventory.ExampleInventory;

public class BancoExtensionTemplate extends JavaPlugin {

    public static final String CALLBACK_IDENTIFIER = "banco-extension-template:example";

    private ExampleInventory exampleInventory;

    @Override
    public void onEnable() {
        // Register example listeners
        registerListeners();

        // Register example inventory
        exampleInventory = new ExampleInventory();
        Banco.get().getStorageRegistry().registerStorage(exampleInventory);
    }

    @Override
    public void onDisable() {
        // Unregister example listeners
        unregisterListeners();

        // Unregister example inventory
        Banco.get().getStorageRegistry().unregisterStorage(exampleInventory);
    }

    private void registerListeners() {
        final var instance = BancoTransactionProcessCallback.INSTANCE;

        instance.registerListener(CALLBACK_IDENTIFIER, (transaction, cancelled) -> {
            getLogger().info("Transaction " + transaction.toString() + " has been processed!");
        });
    }

    private void unregisterListeners() {
        final var instance = BancoTransactionProcessCallback.INSTANCE;

        instance.unregisterListeners(CALLBACK_IDENTIFIER);
    }

}
