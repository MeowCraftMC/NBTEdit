package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import net.minecraft.resources.ResourceLocation;

public class NetworkingConstants {
    public static final ResourceLocation BLOCK_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "block_entity_editing");
    public static final ResourceLocation ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "entity_editing");
    public static final ResourceLocation ITEM_STACK_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "item_stack_editing");

    public static final ResourceLocation C2S_BLOCK_ENTITY_RAYTRACE_RESULT_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_block_entity_raytrace_result");
    public static final ResourceLocation C2S_ENTITY_RAYTRACE_RESULT_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_entity_raytrace_result");
    public static final ResourceLocation C2S_ITEM_STACK_RAYTRACE_RESULT_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_item_stack_raytrace_result");

    public static final ResourceLocation S2C_RAYTRACE_REQUEST_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_raytrace_request");
}
