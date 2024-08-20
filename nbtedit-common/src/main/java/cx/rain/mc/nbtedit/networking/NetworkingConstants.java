package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import net.minecraft.resources.ResourceLocation;

public class NetworkingConstants {
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
