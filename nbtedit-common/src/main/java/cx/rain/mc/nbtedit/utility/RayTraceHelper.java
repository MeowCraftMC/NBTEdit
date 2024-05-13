package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEdit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class RayTraceHelper {
    public static void doRayTrace() {
        Player player = Minecraft.getInstance().player;
        HitResult result = Minecraft.getInstance().hitResult;

        if (result != null) {
            if (result.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) result).getEntity();
                NBTEdit.getInstance().getNetworking().clientOpenGuiRequest(entity, false);
            } else if (result.getType() == HitResult.Type.BLOCK) {
                NBTEdit.getInstance().getNetworking().clientOpenGuiRequest(((BlockHitResult) result).getBlockPos());
            } else if (!player.getMainHandItem().isEmpty()) {
                NBTEdit.getInstance().getNetworking().clientOpenGuiRequest(player.getMainHandItem());
            } else {
                player.createCommandSourceStack().sendFailure(
                        new TranslatableComponent(Constants.MESSAGE_NOTHING_TO_EDIT)
                                .withStyle(ChatFormatting.RED));
            }
        }
    }
}
