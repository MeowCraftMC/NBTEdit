package cx.rain.mc.nbtedit.gui;

import cx.rain.mc.nbtedit.editor.EditorHelper;
import cx.rain.mc.nbtedit.editor.EditorButton;
import cx.rain.mc.nbtedit.gui.component.AbstractScreen;
import cx.rain.mc.nbtedit.gui.component.ButtonComponent;
import cx.rain.mc.nbtedit.gui.editor.EditorButtonComponent;
import cx.rain.mc.nbtedit.gui.editor.NbtTreeView;
import cx.rain.mc.nbtedit.gui.component.ScrollableViewport;
import cx.rain.mc.nbtedit.gui.editor.EditingWindow;
import cx.rain.mc.nbtedit.editor.NbtTree;
import cx.rain.mc.nbtedit.editor.ClipboardHelper;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Consumer;

public class EditorScreen extends AbstractScreen {

    private final NbtTree tree;
    private final Consumer<CompoundTag> onSave;
    private final boolean readOnly;

    private ScrollableViewport treeViewport;
    private NbtTreeView treeView;

    public EditorScreen(CompoundTag tag, Component title, Consumer<CompoundTag> onSave, boolean readOnly) {
        super(readOnly ? Component.translatable(ModConstants.GUI_TITLE_EDITOR_READ_ONLY).append(title) : title);

        this.onSave = onSave;
        this.readOnly = readOnly;

        tree = NbtTree.root(tag);
    }

    @Override
    protected void createChildren() {
        width = minecraft.getWindow().getGuiScaledWidth();
        height = minecraft.getWindow().getGuiScaledHeight();

        treeViewport = new ScrollableViewport(0, 29, width, height - 65, 15);
        treeView = new NbtTreeView(tree, 0, 29, v -> updateButtons());
        treeViewport.addChild(treeView);
        addChild(treeViewport);

        ButtonComponent saveButton;
        if (!readOnly) {
            saveButton = ButtonComponent.getBuilder(Component.translatable(ModConstants.GUI_BUTTON_SAVE), b -> onSave())
                    .pos(width / 4 - 100, height - 27)
                    .size(200, 20)
                    .createNarration(c -> c.get().append(Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_SAVE)))
                    .tooltip(Tooltip.create(Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_SAVE)))
                    .build();
        } else {
            saveButton = ButtonComponent.getBuilder(Component.translatable(ModConstants.GUI_BUTTON_SAVE), b -> {})
                    .pos(width / 4 - 100, height - 27)
                    .size(200, 20)
                    .createNarration(c -> c.get().append(Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_SAVE_DISABLED)))
                    .tooltip(Tooltip.create(Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_SAVE_DISABLED)))
                    .build();
            saveButton.active = false;
        }
        addChild(saveButton);

        var quitButton = ButtonComponent.getBuilder(Component.translatable(ModConstants.GUI_BUTTON_QUIT), b -> onQuit())
                .pos(width * 3 / 4 - 100, height - 27)
                .size(200, 20)
                .createNarration(c -> c.get().append(Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_QUIT)))
                .tooltip(Tooltip.create(Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_QUIT)))
                .build();
        addChild(quitButton);

        initButtons();
        update(false);
    }

    @Override
    public void update() {
        treeViewport.update();
        updateButtons();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderTransparentBackground(guiGraphics);
        renderDirtBackground(guiGraphics, 0, 0, width, 29);
        renderDirtBackground(guiGraphics, 0, height - 35, width, height);
    }

    public void renderDirtBackground(GuiGraphics guiGraphics, int xLoc, int yLoc, int width, int height) {
        guiGraphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        guiGraphics.blit(Screen.BACKGROUND_LOCATION, xLoc, yLoc, width, height, 0.0F, 0.0F, width, height, 32, 32);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /// <editor-fold desc="Buttons.">

    private final EditorButtonComponent[] editorButtons = new EditorButtonComponent[17];

    private void initButtons() {
        int xLoc = 18;
        int yLoc = 4;

        buildButton(EditorButton.COPY, xLoc, yLoc, b -> onCopy());

        xLoc += 15;
        buildButton(EditorButton.CUT, xLoc, yLoc, b -> onCut());

        xLoc += 15;
        buildButton(EditorButton.PASTE, xLoc, yLoc, b -> onPaste());

        xLoc += 15;
        buildButton(EditorButton.DELETE, xLoc, yLoc, b -> onDelete());

        xLoc += 54;
        buildButton(EditorButton.EDIT, xLoc, yLoc, b -> onEdit());

        xLoc = 18;
        yLoc = 17;
        for (var i = 0; i < 12; i++) {
            int id = i;
            buildAddButton(EditorButton.of(id), xLoc, yLoc, b -> onAddButtonsClick(id));
            xLoc += 9;
        }

        updateButtons();
    }

    private void buildButton(EditorButton button, int x, int y, Button.OnPress onPress) {
        var b = new EditorButtonComponent(button, x, y, button.getName(), onPress);
        addButton(button.getId(), b);
    }

    private void buildAddButton(EditorButton button, int x, int y, Button.OnPress onPress) {
        var b = new EditorButtonComponent(button, x, y, Component.translatable(ModConstants.GUI_BUTTON_ADD, button.getName().getString()), onPress);
        addButton(button.getId(), b);
    }

    private void addButton(int id, EditorButtonComponent button) {
        editorButtons[id] = button;
        addChild(button);
    }

    protected void onEdit() {
        doEdit();
        update(false);
    }

    protected void onDelete() {
        doDelete();
        update(true);
    }

    protected void onPaste() {
        doPaste();
        update(true);
    }

    protected void onCut() {
        doCopy();
        doDelete();
        update(true);
    }

    protected void onCopy() {
        doCopy();
        update(false);
    }

    protected void onAddButtonsClick(int id) {
        if (id >= 0 && id <= 12) {
            var focused = treeView.getFocusedNode();
            if (focused != null) {
                focused.setShowChildren(true);
                var tag = EditorHelper.newTag(id);

                if (focused.getTag() instanceof ListTag) {
                    if (tag != null) {
                        var newChild = focused.newChild("", tag);
                        treeView.setFocusedNode(newChild);
                    }
                } else {
                    if (tag != null) {
                        var newChild = focused.newChild(EditorHelper.newTagName(id, focused), tag);
                        treeView.setFocusedNode(newChild);
                    }
                }
            }
        }

        update(true);
    }

    private void onSave() {
        doSave();
        doClose();
    }

    private void onQuit() {
        doClose();
    }

    public void updateButtons() {
        if (readOnly) {
            inactiveAllButtons();
            return;
        }

        var editButton = editorButtons[12];
        var deleteButton = editorButtons[13];
        var pasteButton = editorButtons[14];
        var cutButton = editorButtons[15];
        var copyButton = editorButtons[16];

        var nodeToFocus = treeView.getFocusedNode();
        if (nodeToFocus == null) {
            inactiveAllButtons();
        } else if (nodeToFocus.getTag() instanceof CompoundTag) {
            activeAllButtons();
            editButton.active = nodeToFocus.hasParent() && !(nodeToFocus.getParent().getTag() instanceof ListTag);
            deleteButton.active = nodeToFocus != tree.getRoot();
            cutButton.active = nodeToFocus != tree.getRoot();
            pasteButton.active = ClipboardHelper.getNode() != null;
        } else if (nodeToFocus.getTag() instanceof ListTag) {
            if (nodeToFocus.hasChild()) {
                var elementType = nodeToFocus.getChildren().get(0).getTag().getId();
                inactiveAllButtons();

                editorButtons[elementType - 1].setActive(true);
                editButton.active = !(nodeToFocus.getParent().getTag() instanceof ListTag);
                deleteButton.setActive(true);
                copyButton.setActive(true);
                cutButton.setActive(true);
                pasteButton.active = ClipboardHelper.getNode() != null
                        && ClipboardHelper.getNode().getTag().getId() == elementType;
            } else {
                activeAllButtons();
                editButton.active = !(nodeToFocus.getParent().getTag() instanceof ListTag);
                pasteButton.active = ClipboardHelper.getNode() != null;
            }
        } else {
            inactiveAllButtons();

            editButton.setActive(true);
            deleteButton.setActive(true);
            copyButton.setActive(true);
            cutButton.setActive(true);
        }
    }

    private void activeAllButtons() {
        for (var i = 0; i < 17; i++) {
            editorButtons[i].setActive(true);
        }
    }

    private void inactiveAllButtons() {
        for (var i = 0; i < 17; i++) {
            editorButtons[i].setActive(false);
        }
    }

    /// </editor-fold>

    /// <editor-fold desc="Editor logic.">

    private void update(boolean centerFocused) {
        if (centerFocused && treeView.getFocusedChild() != null) {
            if (treeViewport.shouldShowVerticalBar()) {
                var yOffset = treeViewport.getHeight() / 2 + treeView.getFocusedChild().getY();
                treeViewport.setScrollYOffset(yOffset);
            }
            if (treeViewport.shouldShowHorizontalBar()) {
                var xOffset = treeViewport.getWidth() / 2 + treeView.getFocusedChild().getX();
                treeViewport.setScrollXOffset(xOffset);
            }
        }

        update();
    }

    public void doEdit() {
        if (hasMutexWindow()) {
            return;
        }

        var focused = treeView.getFocusedNode();
        var tag = focused.getTag();
        var parent = focused.getParent().getTag();
        var editor = new EditingWindow((width - EditingWindow.WIDTH) / 2, (height - EditingWindow.HEIGHT) / 2, focused,
                !(parent instanceof ListTag),
                !(tag instanceof CompoundTag || tag instanceof ListTag));
        addWindow(editor, true, true);
    }

    private void doCopy() {
        var node = treeView.getFocusedNode();
        if (node != null) {
            if (node.getTag() instanceof ListTag list) {
                ClipboardHelper.setNode(NbtTree.Node.root(list));
            } else if (node.getTag() instanceof CompoundTag compound) {
                ClipboardHelper.setNode(NbtTree.Node.root(compound));
            } else {
                ClipboardHelper.setNode(node);
            }
        } else {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
        }
    }

    private void doPaste() {
        var focused = treeView.getFocusedNode();

        if (focused != null) {
            var tag = focused.getTag();
            if (!(tag instanceof CompoundTag)
                    && !(tag instanceof CollectionTag<?>)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
                return;
            }

            var toPaste = ClipboardHelper.getNode();
            if (toPaste != null) {
                focused.setShowChildren(true);

                if (focused.getTag() instanceof ListTag) {
                    toPaste.setName("");
                } else {
                    String name = "Paste";
                    List<NbtTree.Node<Tag>> children = focused.getChildren();
                    for (int i = 1; i < children.size(); i++) {
                        var child = children.get(i);
                        if (!EditorHelper.isNameValidInNode(name, child)) {
                            toPaste.setName("Paste " + i);
                        }
                    }
                }

                focused.addChild((NbtTree.Node) toPaste);
                treeView.setFocusedNode(toPaste);
                update(true);
            }
        } else {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
        }
    }

    public void doDelete() {
        var focused = treeView.getFocusedNode();
        if (focused != null) {
            var parent = focused.getParent();
            parent.removeChild(focused);
            treeView.setFocusedNode(parent);
            update(true);
        }
    }

    private void doSave() {
        onSave.accept(tree.toCompound());
    }

    private void doClose() {
        minecraft.setScreen(null);
        minecraft.cursorEntered();
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (!hasWindow()) {
            if (keyCode == GLFW.GLFW_KEY_C && modifiers == GLFW.GLFW_MOD_CONTROL) {
                if (getFocused() != null) {
                    doCopy();
                    update(true);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
                } else {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
                }
            }

            if (keyCode == GLFW.GLFW_KEY_V && modifiers == GLFW.GLFW_MOD_CONTROL) {
                if (getFocused() != null) {
                    doPaste();
                    update(true);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
                } else {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
                }
            }

            if (keyCode == GLFW.GLFW_KEY_X && modifiers == GLFW.GLFW_MOD_CONTROL) {
                if (getFocused() != null) {
                    doCopy();
                    doDelete();
                    update(true);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
                } else {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
                }
            }

            if (keyCode == GLFW.GLFW_KEY_D && modifiers == GLFW.GLFW_MOD_CONTROL) {
                if (getFocused() != null) {
                    doDelete();
                    update(true);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
                } else {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
                }
            }

            return true;
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    /// </editor-fold>
}
