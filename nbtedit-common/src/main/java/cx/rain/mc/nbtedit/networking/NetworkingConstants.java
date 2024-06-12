package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class NetworkingConstants {
    // TAG Codec, 128M is the maximum size of NBT Tag that I can imagine.
    public static final StreamCodec<ByteBuf, CompoundTag> TAG = ByteBufCodecs.compoundTagCodec(() -> new NbtAccounter(128_000_000L, 512));

    // Common
    public static final ResourceLocation BLOCK_ENTITY_EDITING_ID = new ResourceLocation(NBTEdit.MODID, "block_entity_editing");
    public static final ResourceLocation ENTITY_EDITING_ID = new ResourceLocation(NBTEdit.MODID, "entity_editing");
    public static final ResourceLocation ITEM_STACK_EDITING_ID = new ResourceLocation(NBTEdit.MODID, "item_stack_editing");

    // C2S
    public static final ResourceLocation BLOCK_ENTITY_RAYTRACE_RESULT_ID = new ResourceLocation(NBTEdit.MODID, "block_entity_raytrace_result");
    public static final ResourceLocation ENTITY_RAYTRACE_RESULT_ID = new ResourceLocation(NBTEdit.MODID, "entity_raytrace_result");
    public static final ResourceLocation ITEM_STACK_RAYTRACE_RESULT_ID = new ResourceLocation(NBTEdit.MODID, "item_stack_raytrace_result");

    // S2C
    public static final ResourceLocation RAYTRACE_REQUEST_ID = new ResourceLocation(NBTEdit.MODID, "raytrace_request");
}
