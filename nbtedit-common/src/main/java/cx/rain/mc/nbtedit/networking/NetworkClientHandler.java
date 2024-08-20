package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import cx.rain.mc.nbtedit.utility.ModConstants;
import cx.rain.mc.nbtedit.utility.RayTraceHelper;
import cx.rain.mc.nbtedit.utility.ScreenHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class NetworkClientHandler {

    private static LocalPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void handleRaytrace(RaytracePacket packet) {
        RayTraceHelper.doRayTrace();
    }

    public static void handleBlockEntityEditing(BlockEntityEditingPacket packet) {
        var pos = packet.pos();
        ScreenHelper.showNBTEditScreen(pos, packet.tag(), packet.readOnly());
    }

    public static void handleEntityEditing(EntityEditingPacket packet) {
        ScreenHelper.showNBTEditScreen(packet.uuid(), packet.id(), packet.tag(), packet.readOnly());
    }

    public static void handleItemStackEditing(ItemStackEditingPacket packet) {
        var stack = packet.itemStack();
        ScreenHelper.showNBTEditScreen(stack, packet.tag(), packet.readOnly());
    }
}
