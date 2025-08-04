package comfortable_andy.banco_cached_item_extension;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import comfortable_andy.banco_cached_item_extension.arguments.CachedBancoItemArgument;
import comfortable_andy.banco_cached_item_extension.items.impl.CachedBancoItem;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ovh.mythmc.banco.api.items.BancoItemRegistry;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BancoCachedItemExtension extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(CachedBancoItem.class);
    }

    private File itemsFile;
    public final Map<String, CachedBancoItem> items = new HashMap<>();

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onEnable() {
        itemsFile = new File(getDataFolder(), "items.yml");
        load();
        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                e -> {
                    Commands commands = e.registrar();
                    var setItemCommand = (Command<CommandSourceStack>) c -> {
                        ItemStack item = null;
                        try {
                            item = c.getArgument("item", ItemStack.class);
                        } catch (IllegalArgumentException ignored) {
                        }
                        CommandSender source = source(c);
                        if (item == null
                                && !(source instanceof LivingEntity le && le.getEquipment() != null)) {
                            throw new SimpleCommandExceptionType(
                                    new LiteralMessage("no item specified (please do so if you are console) or held!")
                            ).create();
                        }
                        if (item == null)
                            item = Objects.requireNonNull(((LivingEntity) source).getEquipment()).getItemInMainHand();
                        String id = c.getArgument("id", String.class);
                        if (items.containsKey(id)) {
                            throw new SimpleCommandExceptionType(
                                    new LiteralMessage("item '" + id + "' already exists. please remove it first, sorry.")
                            ).create();
                        }
                        Double value = null;
                        try {
                            value = c.getArgument("value", Double.class);
                        } catch (IllegalArgumentException ignored) {
                        }
                        if (value == null) {
                            throw new SimpleCommandExceptionType(
                                    new LiteralMessage("are you serious... you need to specify how much the item is worth. dummy.")
                            ).create();
                        }
                        item = item.clone();
                        item.setAmount(1);
                        CachedBancoItem bancoItem = new CachedBancoItem(new BigDecimal(value), item);
                        items.put(id, bancoItem);
                        save();
                        load();
                        source.sendMessage("'Tis been done sire. (ur still dum)");
                        return 1;
                    };
                    commands.register(Commands
                            .literal("banco_item")
                            .requires(c -> source(c).hasPermission("banco-item.admin"))
                            .then(Commands
                                    .literal("set")
                                    .then(Commands
                                            .argument("id", StringArgumentType.string())
                                            .executes(setItemCommand)
                                            .then(Commands
                                                    .argument(
                                                            "value",
                                                            DoubleArgumentType.doubleArg(0)
                                                    )
                                                    .executes(setItemCommand)
                                                    .then(Commands
                                                            .argument("item", ArgumentTypes.itemStack())
                                                            .executes(setItemCommand)
                                                    )
                                            )
                                    )
                            )
                            .then(Commands
                                    .literal("remove")
                                    .then(Commands
                                            .argument(
                                                    "id",
                                                    new CachedBancoItemArgument(this)
                                            )
                                            .executes(c -> {
                                                String id = c.getArgument("id", String.class);
                                                if (items.containsKey(id)) {
                                                    CachedBancoItem item = items.remove(id);
                                                    BancoItemRegistry.instance.unregister(item);
                                                    source(c).sendMessage("Removed: " + item);
                                                    save();
                                                    return 1;
                                                } else {
                                                    throw new SimpleCommandExceptionType(
                                                            new LiteralMessage("no item found")
                                                    ).create();
                                                }
                                            })
                                    )
                            )
                            .then(Commands.literal("reload")
                                    .executes(c -> {
                                        load();
                                        source(c).sendMessage("Fine. It's done.");
                                        return 1;
                                    }))
                            .build()
                    );
                }
        );
    }

    @SuppressWarnings("UnstableApiUsage")
    private CommandSender source(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        return source(source);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static CommandSender source(CommandSourceStack source) {
        return source.getExecutor() == null ? source.getSender() : source.getExecutor();
    }

    @Override
    public void onDisable() {
        save();
    }

    public void load() {
        BancoItemRegistry.instance.unregister(items.values()
                .toArray(CachedBancoItem[]::new));
        items.clear();
        if (!itemsFile.exists()) saveResource("items.yml", false);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(itemsFile);
        for (String key : config.getKeys(false)) {
            items.put(key, config.getObject(key, CachedBancoItem.class));
        }
        BancoItemRegistry.instance.register(
                items.values()
                        .stream()
                        .sorted(Comparator.comparing(CachedBancoItem::value))
                        .toArray(CachedBancoItem[]::new)
        );
    }

    public void save() {
        if (!itemsFile.exists()) saveResource("items.yml", false);
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<String, CachedBancoItem> entry : items.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        try {
            config.save(itemsFile);
        } catch (IOException e) {
            getLogger().severe("\n\n" + config.saveToString() + "\n\n");
            throw new RuntimeException(e);
        }
    }

}
