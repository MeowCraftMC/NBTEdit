package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEdit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;

public class RayTraceHelper {
    public static void doRayTrace() {
        var mc = Minecraft.getInstance();
        var player = mc.player;
        var result = mc.hitResult;

        if (player == null) {
            return;
        }

        if (result != null) {
            if (result.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult) result).getEntity();
                NBTEdit.getInstance().getNetworking().clientOpenGuiRequest(entity, false);
            } else {
                var picked = pickEntity();
                if (picked != null) {
                    NBTEdit.getInstance().getNetworking().clientOpenGuiRequest(picked, false);
                } else {
                    if (result.getType() == HitResult.Type.BLOCK) {
                        NBTEdit.getInstance().getNetworking().clientOpenGuiRequest(((BlockHitResult) result).getBlockPos());
                        return;
                    }

                    if (!player.getMainHandItem().isEmpty()) {
                        NBTEdit.getInstance().getNetworking().clientOpenGuiRequest(player.getMainHandItem());
                    } else {
                        player.createCommandSourceStack().sendFailure(Component
                                .translatable(Constants.MESSAGE_NOTHING_TO_EDIT)
                                .withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
    }

    private static Entity pickEntity() {
        var mc = Minecraft.getInstance();
        var camera = mc.getCameraEntity();
        var distance = mc.gameMode.getPickRange();

        var startVector = camera.getEyePosition(1);
        var viewVector = camera.getViewVector(1);
        var endVector = startVector.add(viewVector.scale(distance));

        var box = camera.getBoundingBox().expandTowards(viewVector.scale(distance)).inflate(1.0, 1.0, 1.0);

        var entityHitResult = ProjectileUtil.getEntityHitResult(camera, startVector, endVector, box,
                entity -> !entity.isSpectator() && !entity.isInvisible(), distance);
        if (entityHitResult != null) {
            var target = entityHitResult.getEntity();
            var location = entityHitResult.getLocation();
            var dis = startVector.distanceToSqr(location);
            if (dis < distance) {
                return target;
            }
        }
        return null;
    }
}
