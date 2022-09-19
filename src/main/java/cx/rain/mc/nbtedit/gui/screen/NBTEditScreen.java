package cx.rain.mc.nbtedit.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.networking.NBTEditNetworking;
import cx.rain.mc.nbtedit.networking.packet.C2SEntitySavingPacket;
import cx.rain.mc.nbtedit.networking.packet.C2SBlockEntitySavingPacket;
import cx.rain.mc.nbtedit.utility.Constants;
import cx.rain.mc.nbtedit.utility.nbt.NBTTree;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class NBTEditScreen extends Screen {
    protected final boolean isEntity;

    protected UUID entityUuid;
    protected int entityId;
    protected boolean isSelf;

    protected BlockPos blockPos;

    protected NBTEditGui gui;

    public NBTEditScreen(UUID uuid, int id, CompoundTag tag, boolean self) {
        super(Component.translatable(Constants.GUI_TITLE_NBTEDIT_ENTITY, uuid));
        minecraft = Minecraft.getInstance();

        isEntity = true;
        entityUuid = uuid;
        entityId = id;
        isSelf = self;

        gui = new NBTEditGui(new NBTTree(tag));
    }

    public NBTEditScreen(BlockPos pos, CompoundTag tag) {
        super(Component.translatable(Constants.GUI_TITLE_NBTEDIT_BLOCK_ENTITY,
                pos.getX(), pos.getY(), pos.getZ()));
        minecraft = Minecraft.getInstance();

        isEntity = false;
        blockPos = pos;

        gui = new NBTEditGui(new NBTTree(tag));
    }

    public boolean isEntity() {
        return isEntity;
    }

    public boolean isBlockEntity() {
        return !isEntity;
    }

    public Entity getEntity() {
        if (!isEntity()) {
            throw new UnsupportedOperationException("Cannot get Entity by an BlockEntity!");
        }

        return getMinecraft().level.getEntity(entityId);
    }

    public BlockPos getBlockPos() {
        if (!isBlockEntity()) {
            throw new UnsupportedOperationException("Cannot get block position of an Entity!");
        }

        return blockPos;
    }

    @Override
    protected void init() {
        super.init();

        getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        clearWidgets();

        gui.init(width, height, height - 35);

        addRenderableWidget(new Button(width / 4 - 100, height - 27, 200, 20, Component.translatable(Constants.GUI_BUTTON_SAVE), this::onSaveClicked));
        addRenderableWidget(new Button(width * 3 / 4 - 100, height - 27, 200, 20, Component.translatable(Constants.GUI_BUTTON_QUIT), this::onQuitClicked));
    }

    private void onSaveClicked(Button button) {
        doSave();
        doClose();
    }

    private void onQuitClicked(Button button) {
        doClose();
    }

    private void doSave() {
        if (isEntity) {
            NBTEdit.getInstance().getNetworkManager().saveEditing(getEntity(), gui.getTree().toCompound(), isSelf);
        } else {
            NBTEdit.getInstance().getNetworkManager().saveEditing(getBlockPos(), gui.getTree().toCompound());
        }
    }

    private void doClose() {
        getMinecraft().setScreen(null);
        getMinecraft().cursorEntered();
    }

    @Override
    public void onClose() {
        super.onClose();

        getMinecraft().keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public boolean charTyped(char character, int keyId) {
        var subWindow = gui.getSubWindow();
        if (subWindow != null)
            subWindow.charTyped(character, keyId);
        else {
            if (keyId == 1) {
                if (gui.isEditingSlot()) {
                    gui.stopEditingSlot();
                } else {
                    doClose();
                }
            } else if (keyId == InputConstants.KEY_DELETE) {
                gui.doDeleteSelected();
            } else if (keyId == InputConstants.KEY_RETURN) {
                gui.doEditSelected();
            } else if (keyId == InputConstants.KEY_UP) {
                gui.arrowKeyPressed(true);
            } else if (keyId == InputConstants.KEY_DOWN) {
                gui.arrowKeyPressed(false);
            } else {
                gui.charTyped(character, keyId);
            }
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        super.mouseScrolled(mouseX, mouseY, delta);

        var ofs = (int) delta;
        if (ofs != 0) {
            gui.shiftY((ofs >= 1) ? 6 : -6);
        }
        return true;
    }

    @Override
    public void tick() {
        if (!getMinecraft().player.isAlive()) {
            doClose();
        } else {
            gui.update();
        }
    }

    @Override
    public boolean keyPressed(int mouseX, int mouseY, int delta) {
        gui.keyPressed(mouseX, mouseY, delta);
        return true;
    }

    @Override
    public boolean mouseClicked(double par1, double par2, int par3) {
        gui.onMouseClicked(Mth.floor(par1), Mth.floor(par2), par3);
        return super.mouseClicked(par1, par2, par3);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        renderBackground(stack);
        gui.render(stack, mouseX, mouseY, partialTick);
        drawCenteredString(stack, getMinecraft().font, title, this.width / 2, 5, 16777215);

        if (gui.getSubWindow() == null) {
            super.render(stack, mouseX, mouseY, partialTick);
        } else {
            super.render(stack, -1, -1, partialTick);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
