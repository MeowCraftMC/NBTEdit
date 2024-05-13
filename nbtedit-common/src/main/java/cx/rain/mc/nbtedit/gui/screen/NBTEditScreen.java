package cx.rain.mc.nbtedit.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.utility.Constants;
import cx.rain.mc.nbtedit.utility.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class NBTEditScreen extends Screen {
    protected UUID entityUuid;
    protected int entityId = -1;
    protected boolean isSelf;

    protected BlockPos blockPos;

    protected ItemStack itemStack;

    protected NBTEditGui gui;

    public NBTEditScreen(UUID uuid, int id, CompoundTag tag, boolean self) {
        super(new TranslatableComponent(Constants.GUI_TITLE_NBTEDIT_ENTITY, uuid));
        minecraft = Minecraft.getInstance();

        entityUuid = uuid;
        entityId = id;
        isSelf = self;

        gui = new NBTEditGui(NBTTree.root(tag), this);
    }

    public NBTEditScreen(BlockPos pos, CompoundTag tag) {
        super(new TranslatableComponent(Constants.GUI_TITLE_NBTEDIT_BLOCK_ENTITY,
                pos.getX(), pos.getY(), pos.getZ()));
        minecraft = Minecraft.getInstance();

        blockPos = pos;

        gui = new NBTEditGui(NBTTree.root(tag), this);
    }

    public NBTEditScreen(ItemStack itemStack, CompoundTag tag) {
        super(new TranslatableComponent(Constants.GUI_TITLE_NBTEDIT_ITEM_STACK, itemStack.getDisplayName().getString()));
        minecraft = Minecraft.getInstance();

        this.itemStack = itemStack;

        gui = new NBTEditGui(NBTTree.root(tag), this);
    }

    @Override
    protected void init() {
        super.init();

        children.clear();

        gui.init(width, height, height - 35);

        addButton(new Button(width / 4 - 100, height - 27, 200, 20, new TranslatableComponent(Constants.GUI_BUTTON_SAVE), this::onSaveClicked));
        addButton(new Button(width * 3 / 4 - 100, height - 27, 200, 20, new TranslatableComponent(Constants.GUI_BUTTON_QUIT), this::onQuitClicked));
    }

    @Override
    public void tick() {
        if (!getMinecraft().player.isAlive()) {
            doClose();
        } else {
            gui.tick();

        }

        super.tick();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderHelper.drawGrayBackground(poseStack);
        gui.render(poseStack, mouseX, mouseY, partialTick);
        drawCenteredString(poseStack, getMinecraft().font, title, this.width / 2, 5, 16777215);

        if (gui.hasWindow()) {
            super.render(poseStack, mouseX, mouseY, partialTick);
        } else {
            super.render(poseStack, -1, -1, partialTick);
        }
    }

    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    // <editor-fold desc="Properties and accessors.">

    public boolean isEntity() {
        return entityUuid != null || entityId != -1;
    }

    public boolean isBlockEntity() {
        return blockPos != null;
    }

    public boolean isItemStack() {
        return itemStack != null;
    }

    public Entity getEntity() {
        if (!isEntity()) {
            throw new UnsupportedOperationException("Cannot get Entity, it is not an Entity!");
        }

        return getMinecraft().level.getEntity(entityId);
    }

    public BlockPos getBlockPos() {
        if (!isBlockEntity()) {
            throw new UnsupportedOperationException("Cannot get block position, it is not a BlockEntity!");
        }

        return blockPos;
    }

    public ItemStack getItemStack() {
        if (!isItemStack()) {
            throw new UnsupportedOperationException("Cannot get ItemStack, it is not an ItemStack!");
        }

        return itemStack;
    }

    // </editor-fold>

    // <editor-fold desc="Button handler.">

    private void onSaveClicked(Button button) {
        doSave();
        doClose();
    }

    private void onQuitClicked(Button button) {
        doClose();
    }

    private void doSave() {
        if (isEntity()) {
            NBTEdit.getInstance().getNetworking().saveEditing(getEntity(), gui.getTree().toCompound(), isSelf);
        } else if (isBlockEntity()) {
            NBTEdit.getInstance().getNetworking().saveEditing(getBlockPos(), gui.getTree().toCompound());
        } else {
            NBTEdit.getInstance().getNetworking().saveEditing(getItemStack(), gui.getTree().toCompound());
        }
    }

    private void doClose() {
        getMinecraft().setScreen(null);
        getMinecraft().cursorEntered();
    }

    // </editor-fold>

    // <editor-fold desc="Input processing.">

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        super.mouseScrolled(mouseX, mouseY, delta);

        int offset = (int) delta;
        if (offset != 0) {
            gui.shiftY((offset >= 1) ? 6 : -6);
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean result = gui.onKeyPress(keyCode, scanCode, modifiers);
        return result || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = gui.onMouseClicked(Mth.floor(mouseX), Mth.floor(mouseY), button);
        return result || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        boolean result = gui.onCharTyped(codePoint, modifiers);
        return result || super.charTyped(codePoint, modifiers);
    }

    // </editor-fold>
}
