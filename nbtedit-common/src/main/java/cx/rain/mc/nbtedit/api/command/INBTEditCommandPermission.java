package cx.rain.mc.nbtedit.api.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public interface INBTEditCommandPermission {
    boolean hasPermission(CommandSourceStack source);

    boolean hasPermission(ServerPlayer player);
}
