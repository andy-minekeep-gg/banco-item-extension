package ovh.mythmc.bancoextensiontemplate.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ovh.mythmc.banco.api.storage.BancoInventory;

import java.util.UUID;

public final class ExampleInventory extends BancoInventory {

    @Override
    public boolean supportsOfflinePlayers() {
        return false;
    }

    @Override
    public Inventory inventory(UUID uniqueId) {
        final var player = Bukkit.getPlayer(uniqueId);
        final var inventory = Bukkit.createInventory(player, 9);

        inventory.addItem(ItemStack.of(Material.EMERALD, 1));
        return inventory;
    }

}
