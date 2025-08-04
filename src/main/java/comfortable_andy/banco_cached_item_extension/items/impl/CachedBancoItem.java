package comfortable_andy.banco_cached_item_extension.items.impl;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ovh.mythmc.banco.api.items.BancoItem;

import java.math.BigDecimal;
import java.util.Map;

public record CachedBancoItem(BigDecimal value, ItemStack item) implements BancoItem, ConfigurationSerializable {



    @Override
    public ItemStack asItemStack(int i) {
        ItemStack clone = item.clone();
        clone.setAmount(i);
        return clone;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of("value", value.toString(), "item", item);
    }

    public static CachedBancoItem valueOf(Map<String, Object> map) {
        return new CachedBancoItem(
                new BigDecimal(String.valueOf(map.get("value"))),
                (ItemStack) map.get("item")
        );
    }

}
