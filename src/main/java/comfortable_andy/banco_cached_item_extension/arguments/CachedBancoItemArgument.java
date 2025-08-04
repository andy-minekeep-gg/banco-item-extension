package comfortable_andy.banco_cached_item_extension.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import comfortable_andy.banco_cached_item_extension.BancoCachedItemExtension;
import comfortable_andy.banco_cached_item_extension.items.impl.CachedBancoItem;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class CachedBancoItemArgument implements CustomArgumentType.Converted<String, String> {

    private final BancoCachedItemExtension main;

    public CachedBancoItemArgument(BancoCachedItemExtension main) {
        this.main = main;
    }

    @Override
    public @NotNull String convert(@NotNull String nativeType) throws CommandSyntaxException {
        if (!main.items.containsKey(nativeType)) {
            throw new SimpleCommandExceptionType(
                    new LiteralMessage("shit's not real. you tripping bruh.")
            ).create();
        }
        return nativeType;
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for (var entry : main.items.entrySet()) {
            builder.suggest(
                    StringArgumentType.escapeIfRequired(entry.getKey()),
                    new LiteralMessage(entry.getValue().toString())
            );
        }
        return builder.buildFuture();
    }
}
