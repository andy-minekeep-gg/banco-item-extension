package ovh.mythmc.bancoextensiontemplate;

import org.bukkit.plugin.java.JavaPlugin;
import ovh.mythmc.banco.api.Banco;
import ovh.mythmc.bancoextensiontemplate.inventories.ExampleInventory;
import ovh.mythmc.bancoextensiontemplate.listeners.ExampleListener;

public class BancoExtensionTemplate extends JavaPlugin {

    private ExampleListener exampleListener;
    private ExampleInventory exampleInventory;

    @Override
    public void onEnable() {
        exampleListener = new ExampleListener();
        Banco.get().getEventManager().registerListener(exampleListener);

        exampleInventory = new ExampleInventory();
        Banco.get().getInventoryManager().registerInventory(exampleInventory);
    }

    @Override
    public void onDisable() {
        Banco.get().getEventManager().unregisterListener(exampleListener);
        Banco.get().getInventoryManager().unregisterInventory(exampleInventory);
    }

}
