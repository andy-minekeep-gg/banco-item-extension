package ovh.mythmc.bancoextensiontemplate.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ovh.mythmc.banco.api.bukkit.inventories.BancoInventoryBukkit;

import java.util.UUID;

public final class ExampleInventory extends BancoInventoryBukkit {

    @Override
    public @NotNull Inventory get(UUID uuid) {
        Inventory inventory = Bukkit.createInventory(null, 9);
        inventory.addItem(ItemStack.of(Material.GOLD_INGOT));
        return inventory;
    }

}
