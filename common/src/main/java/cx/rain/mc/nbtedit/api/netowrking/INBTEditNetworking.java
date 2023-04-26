package cx.rain.mc.nbtedit.api.netowrking;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface INBTEditNetworking {
    void serverRayTraceRequest(ServerPlayer player);

    void clientOpenGuiRequest(Entity entity, boolean self);
    void clientOpenGuiRequest(BlockPos pos);
    void clientOpenGuiRequest(ItemStack stack);

    void serverOpenClientGui(ServerPlayer player, Entity entity);
    void serverOpenClientGui(ServerPlayer player, BlockPos pos);
    void serverOpenClientGui(ServerPlayer player);
    void serverOpenClientGui(ServerPlayer player, ItemStack stack);

    void saveEditing(Entity entity, CompoundTag tag, boolean self);
    void saveEditing(BlockPos pos, CompoundTag tag);
    void saveEditing(ItemStack stack, CompoundTag tag);
}
