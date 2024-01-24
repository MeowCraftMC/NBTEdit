package cx.rain.mc.nbtedit.gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.component.window.EditValueSubWindow;
import cx.rain.mc.nbtedit.gui.component.NBTNodeComponent;
import cx.rain.mc.nbtedit.gui.component.button.NBTOperatorButton;
import cx.rain.mc.nbtedit.gui.component.window.ISubWindowHolder;
import cx.rain.mc.nbtedit.gui.component.window.SubWindowComponent;
import cx.rain.mc.nbtedit.gui.screen.NBTEditScreen;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import cx.rain.mc.nbtedit.utility.Constants;
import cx.rain.mc.nbtedit.utility.SortHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.List;

public class NBTEditGui extends Gui implements ISubWindowHolder {
    protected List<NBTNodeComponent> nodes = new ArrayList<>();

    public final NBTEditScreen screen;

    protected int width;
    protected int height;
    protected int bottom;

    protected int x;
    protected int y;

    protected int heightDiff;
    protected int heightOffset;

    protected int yClick = -1;

    public NBTEditGui(NBTTree treeIn, NBTEditScreen screen) {
        super(Minecraft.getInstance());

        this.screen = screen;
        tree = treeIn;

        addButtons();
    }

    // <editor-fold desc="Properties and accessors.">
    private final int START_X = 10;
    private final int START_Y = 30;

    private final int X_GAP = 10;
    private final int Y_GAP = getMinecraft().font.lineHeight + 2;

    protected Minecraft getMinecraft() {
        return minecraft;
    }

    private int getHeightDifference() {
        return getContentHeight() - (bottom - START_Y + 2);
    }

    private int getContentHeight() {
        return Y_GAP * nodes.size();
    }

    // </editor-fold>

    // <editor-fold desc="Initializations.">

    public void init(int widthIn, int heightIn, int bottomIn) {
        width = widthIn;
        height = heightIn;
        bottom = bottomIn;

        yClick = -1;
        update(false);
    }

    // </editor-fold>

    // <editor-fold desc="Updates.">

    public void update(NBTTree.Node<?> node) {
        var parent = node.getParent();
        Collections.sort(parent.getChildren(), SortHelper.get());
        update(true);
    }

    public void updateFromRoot(NBTTree.Node<?> node, boolean circular) {
        Collections.sort(node.getChildren(), SortHelper.get());
        if (circular) {
            for (var child : node.getChildren()) {
                updateFromRoot(child, true);
            }
        }
    }

    public void update(boolean isShiftToFocused) {
        y = START_Y;
        nodes.clear();
        addNodes(tree.getRoot(), START_X);
        if (focused != null) {
            if (!checkValidFocus(focused)) {
                setFocused(null);
            }
        }

        heightDiff = getHeightDifference();
        if (heightDiff <= 0) {
            heightOffset = 0;
        } else {
            if (heightOffset < -heightDiff) {
                heightOffset = -heightDiff;
            }
            if (heightOffset > 0) {
                heightOffset = 0;
            }

            for (var node : nodes) {
                node.shiftY(heightOffset);
            }

            if (isShiftToFocused && focused != null) {
                shiftToFocus(focused);
            }
        }

        updateFromRoot(getTree().getRoot(), true);
    }

    @Override
    public void tick() {
        super.tick();

        if (hasWindow()) {
            for (var window : getWindows()) {
                window.tick();
            }
        }

        updateButtons();
    }

    // </editor-fold>

    // <editor-fold desc="Buttons.">

    protected List<Button> buttons = new ArrayList<>();

    protected NBTOperatorButton[] addButtons = new NBTOperatorButton[12];
    protected NBTOperatorButton editButton;
    protected NBTOperatorButton deleteButton;
    protected NBTOperatorButton copyButton;
    protected NBTOperatorButton cutButton;
    protected NBTOperatorButton pasteButton;

    protected List<Button> getButtons() {
        return buttons;
    }

    protected void addButton(Button button) {
        buttons.add(button);
    }

    private void addButtons() {
        int xLoc = 18;
        int yLoc = 4;

        copyButton = new NBTOperatorButton(17, xLoc, yLoc, this, this::onCopyButtonClick,
                componentSupplier -> componentSupplier.get().append(new TranslatableComponent(Constants.GUI_NARRATION_BUTTON_COPY))); // Copy Button.
        addButton(copyButton);

        xLoc += 15;
        cutButton = new NBTOperatorButton(16, xLoc, yLoc, this, this::onCutButtonClick,
                componentSupplier -> componentSupplier.get().append(new TranslatableComponent(Constants.GUI_NARRATION_BUTTON_CUT))); // Cut Button.
        addButton(cutButton);

        xLoc += 15;
        pasteButton = new NBTOperatorButton(15, xLoc, yLoc, this, this::onPasteButtonClick,
                componentSupplier -> componentSupplier.get().append(new TranslatableComponent(Constants.GUI_NARRATION_BUTTON_PASTE))); // Paste Button.
        addButton(pasteButton);

        xLoc += 45;
        editButton = new NBTOperatorButton(13, xLoc, yLoc, this, this::onEditButtonClick,
                componentSupplier -> componentSupplier.get().append(new TranslatableComponent(Constants.GUI_NARRATION_BUTTON_EDIT))); // Edit Button.
        addButton(editButton);

        xLoc += 15;
        deleteButton = new NBTOperatorButton(14, xLoc, yLoc, this, this::onDeleteButtonClick,
                componentSupplier -> componentSupplier.get().append(new TranslatableComponent(Constants.GUI_NARRATION_BUTTON_DELETE))); // Delete Button.
        addButton(deleteButton);

        // Add nbt type buttons.
        xLoc = 18;
        yLoc = 17;
        for (var i = 1; i < 13; i++) {
            var button = new NBTOperatorButton(i, xLoc, yLoc, this, this::onAddButtonsClick,
                    componentSupplier -> componentSupplier.get().append(new TranslatableComponent(Constants.GUI_NARRATION_BUTTON_ADD)));
            addButtons[i - 1] = button;
            addButton(button);
            xLoc += 9;
        }

        updateButtons();
    }

    protected void onEditButtonClick(Button button) {
        if (button instanceof NBTOperatorButton operatorButton) {
            if (operatorButton.getButtonId() == 13) {  // 但愿人没事。
                doEditSelected();
            }
        }
        updateButtons();
    }

    protected void onDeleteButtonClick(Button button) {
        if (button instanceof NBTOperatorButton operatorButton) {
            if (operatorButton.getButtonId() == 14) {
                deleteSelected();
            }
        }
        updateButtons();
    }

    protected void onPasteButtonClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() == 15) {
                paste();
            }
        }
        updateButtons();
    }

    protected void onCutButtonClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() == 16) {
                copySelected();
                deleteSelected();
            }
        }
        updateButtons();
    }

    protected void onCopyButtonClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() == 17) {
                copySelected();
            }
        }
        updateButtons();
    }

    protected void onAddButtonsClick(Button button) {
        if (button instanceof NBTOperatorButton operatorButton) {
            if (operatorButton.getButtonId() >= 0 && operatorButton.getButtonId() <= 12) {
                if (getFocused() != null) {
                    getFocused().setShowChildren(true);

                    if (getFocused().getTag() instanceof ListTag) {
                        var tag = NBTHelper.newTag((operatorButton.getButtonId()));
                        if (tag != null) {
                            var newChild = getFocused().newChild("", tag);
                            setFocused(newChild);
                        }
                    } else {
                        var typeId = operatorButton.getButtonId();
                        setFocused(insertOnFocus(newName(getFocused(), typeId), NBTHelper.newTag(typeId)));
                    }
                    update(true);
                }
            }
        }
        updateButtons();
    }

    public void doEditSelected() {
        var base = getFocused().getTag();
        var parent = getFocused().getParent().getTag();
        var editor = new EditValueSubWindow(this, getFocused(), !(parent instanceof ListTag),
                !(base instanceof CompoundTag || base instanceof ListTag));
        editor.init((width - EditValueSubWindow.WIDTH) / 2, (height - EditValueSubWindow.HEIGHT) / 2);
        addWindow(editor);
    }

    private void copySelected() {
        var node = getFocused();
        if (node != null) {
            if (node.getTag() instanceof ListTag list) {
                NBTEdit.getInstance().getClient().setClipboard(NBTTree.Node.root(list));
            } else if (node.getTag() instanceof CompoundTag compound) {
                NBTEdit.getInstance().getClient().setClipboard(NBTTree.Node.root(compound));
            } else {
                NBTEdit.getInstance().getClient().setClipboard(focused);
            }

            setFocused(focused);
        } else {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
        }
    }

    private void paste() {
        var focused = getFocused();

        if (focused != null) {
            var tag = focused.getTag();
            if (!(tag instanceof CompoundTag)
                    && !(tag instanceof CollectionTag<?>)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
                return;
            }

            var node = NBTEdit.getInstance().getClient().getClipboard();
            if (node != null) {
                focused.setShowChildren(true);

                if (!(focused.getTag() instanceof ListTag)) {
                    String name = "Paste";
                    List<NBTTree.Node<Tag>> children = focused.getChildren();
                    for (int i = 0; i < children.size(); i++) {
                        var child = children.get(i);
                        if (!isNameValid(name, child)) {
                            child.setName("Child" + i);
                        }
                    }
                }

                focused.addChild((NBTTree.Node) node);
                setFocused(node);
                update(true);
            }
        } else {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
        }
    }

    public void deleteSelected() {
        var prevFocused = getFocused();
        if (prevFocused != null) {
            var parent = prevFocused.getParent();
            parent.removeChild(prevFocused);
            shiftFocus(true);
            if (focused == prevFocused) {
                setFocused(null);
            }
            update(false);
        }
    }

    private void updateButtons() {
        var nodeToFocus = getFocused();
        if (nodeToFocus == null) {
            inactiveAllButtons();
        } else if (nodeToFocus.getTag() instanceof CompoundTag) {
            activeAllButtons();
            editButton.active = nodeToFocus.hasParent() && !(nodeToFocus.getParent().getTag() instanceof ListTag);
            deleteButton.active = nodeToFocus != tree.getRoot();
            cutButton.active = nodeToFocus != tree.getRoot();
            pasteButton.active = NBTEdit.getInstance().getClient().getClipboard() != null;
        } else if (nodeToFocus.getTag() instanceof ListTag) {
            if (nodeToFocus.hasChild()) {
                var elementType = nodeToFocus.getChildren().get(0).getTag().getId();
                inactiveAllButtons();

                addButtons[elementType - 1].active = true;
                editButton.active = !(nodeToFocus.getParent().getTag() instanceof ListTag);
                deleteButton.active = true;
                copyButton.active = true;
                cutButton.active = true;
                pasteButton.active = NBTEdit.getInstance().getClient().getClipboard() != null
                        && NBTEdit.getInstance().getClient().getClipboard().getTag().getId() == elementType;
            } else {
                activeAllButtons();
                editButton.active = !(nodeToFocus.getParent().getTag() instanceof ListTag);
                pasteButton.active = NBTEdit.getInstance().getClient().getClipboard() != null;
            }
        } else {
            inactiveAllButtons();

            editButton.active = true;
            deleteButton.active = true;
            copyButton.active = true;
            cutButton.active = true;
        }
    }

    private void activeAllButtons() {
        for (var button : addButtons) {
            button.active = true;
        }

        editButton.active = true;
        deleteButton.active = true;
        copyButton.active = true;
        cutButton.active = true;
        pasteButton.active = true;
    }

    private void inactiveAllButtons() {
        for (var button : addButtons) {
            button.active = false;
        }

        editButton.active = false;
        deleteButton.active = false;
        copyButton.active = false;
        cutButton.active = false;
        pasteButton.active = false;
    }
    
    // </editor-fold>

    // <editor-fold desc="Focusing.">

    private void addNodes(NBTTree.Node<?> root, int startX) {
        nodes.add(new NBTNodeComponent(startX, y,
                new TextComponent(NBTHelper.getNBTNameSpecial(root)), this, root));

        startX += X_GAP;
        y += Y_GAP;

        if (root.shouldShowChildren()) {
            for (var child : root.getChildren()) {
                addNodes(child, startX);
            }
        }
    }

    private void setFocused(NBTTree.Node<?> toFocus) {
        updateButtons();

        focused = toFocus;
    }

    private void shiftToFocus(NBTTree.Node<?> focused) {
        var index = getIndexOf(focused);
        if (index != -1) {
            var component = nodes.get(index);
            shiftY((bottom + START_Y + 1) / 2 - (component.y + component.getHeight()));
        }
    }

    private void shiftFocus(boolean upward) {
        int index = getIndexOf(focused);
        if (index != -1) {
            index += (upward) ? -1 : 1;
            if (index >= 0 && index < nodes.size()) {
                setFocused(nodes.get(index).getNode());
                shiftY((upward) ? Y_GAP : -Y_GAP);
            }
        }
    }

    private int getIndexOf(NBTTree.Node<?> focused) {
        for (var i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNode() == focused) {
                return i;
            }
        }
        return -1;
    }

    public void shiftY(int offsetY) {
        if (heightDiff <= 0 || hasWindow()) {
            return;
        }

        int difference = heightOffset + offsetY;
        if (difference > 0) {
            difference = 0;
        }

        if (difference < -heightDiff) {
            difference = -heightDiff;
        }

        for (var node : nodes) {
            node.shiftY(difference - heightOffset);
        }

        heightOffset = difference;
    }

    private boolean checkValidFocus(NBTTree.Node<?> focused) {
        for (var node : nodes) { // Check all nodes.
            if (node.getNode() == focused) {
                setFocused(focused);
                return true;
            }
        }
        return focused.hasParent() && checkValidFocus(focused.getParent());
    }

    // </editor-fold>

    // <editor-fold desc="NBT tree.">

    protected NBTTree tree;
    protected NBTTree.Node<?> focused;

    public NBTTree getTree() {
        return tree;
    }

    public NBTTree.Node<?> getFocused() {
        return focused;
    }

    // </editor-fold>

    // <editor-fold desc="Helper methods.">

    private NBTTree.Node<?> insertOnFocus(String name, Tag tag) {
        // Todo: qyl27: more check for it?
        if (isNameValid(name, getFocused())) {
            return getFocused().newChild(name, tag);
        }

        return getFocused().newChild("", tag);
    }

    private boolean isNameValid(String name, NBTTree.Node<?> parent) {
        for (var node : parent.getChildren()) {
            if (node.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private String newName(NBTTree.Node<?> parent, byte typeId) {
        var typeName = NBTHelper.getNameByButton(typeId);
        if (!parent.hasChild()) {
            return typeName + "1";
        }
        for (int i = 1; i <= parent.getChildren().size() + 1; ++i) {
            String name = typeName + i;
            if (isNameValid(name, parent)) {
                return name;
            }
        }
        return typeName + "INF";
    }

    // </editor-fold>

    // <editor-fold desc="Input processing.">

    public boolean onMouseClicked(int mouseX, int mouseY, int partialTick) {
        if (hasWindow()) {
            return getActiveWindow().mouseClicked(mouseX, mouseY, partialTick);
        }

        for (var button : getButtons()) {
            if (button.isMouseOver(mouseX, mouseY)) {
                button.mouseClicked(mouseX, mouseY, partialTick);
            }
        }

        var shouldUpdate = false;

        for (var node : nodes) {
            if (node.spoilerClicked(mouseX, mouseY)) { // Check hide/show children buttons
                shouldUpdate = true;
                if (node.shouldShowChildren()) {
                    heightOffset = (START_Y + 1) - (node.y) + heightOffset;
                }
                break;
            }
        }

        if (mouseY >= START_Y && mouseX <= width - 175) { //Check actual nodes, remove focus if nothing clicked
            NBTTree.Node<?> newFocus = null;
            for (var node : nodes) {
                if (node.isTextClicked(mouseX, mouseY)) {
                    newFocus = node.getNode();
                    break;
                }
            }

            setFocused(newFocus);
        }

        if (shouldUpdate) {
            update(false);
        }

        return false;
    }

    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (hasWindow()) {
            return getActiveWindow().keyPressed(keyCode, scanCode, modifiers);
        }

        if (keyCode == GLFW.GLFW_KEY_C && modifiers == GLFW.GLFW_MOD_CONTROL) {
            if (getFocused() != null) {
                copySelected();
                updateButtons();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
            } else {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
            }
        }

        if (keyCode == GLFW.GLFW_KEY_V && modifiers == GLFW.GLFW_MOD_CONTROL) {
            if (getFocused() != null) {
                paste();
                updateButtons();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
            } else {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
            }
        }

        if (keyCode == GLFW.GLFW_KEY_X && modifiers == GLFW.GLFW_MOD_CONTROL) {
            if (getFocused() != null) {
                copySelected();
                deleteSelected();
                updateButtons();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
            } else {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
            }
        }

        if (keyCode == GLFW.GLFW_KEY_D && modifiers == GLFW.GLFW_MOD_CONTROL) {
            if (getFocused() != null) {
                deleteSelected();
                updateButtons();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
            } else {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1));
            }
        }

        return false;
    }

    public boolean onCharTyped(char character, int keyId) {
        if (hasWindow()) {
            return getActiveWindow().charTyped(character, keyId);
        }

        return false;
    }

    // </editor-fold>

    // <editor-fold desc="Render.">
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        var prevMouseX = mouseX;
        var prevMouseY = mouseY;

        if (hasWindow()) {
            prevMouseX = -1;
            prevMouseY = -1;
        }

        for (var node : nodes) {
            if (node.shouldRender(START_Y - 1, bottom)) {
                node.render(poseStack, prevMouseX, prevMouseY, partialTick);
            }
        }

        renderBackground(poseStack);
        for (var button : buttons) {
            button.render(poseStack, prevMouseX, prevMouseY, partialTick);
        }

        renderScrollBar(poseStack, prevMouseX, prevMouseY);

        if (hasWindow()) {
            getActiveWindow().render(poseStack, mouseX, mouseY, partialTick);
        }
    }

    private void renderScrollBar(PoseStack poseStack, int mouseX, int mouseY) {
        if (heightDiff > 0) {
            if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), 0) == GLFW.GLFW_PRESS) { // XXX: bad implementation, it should use AbstractScrollWidget instead.
                if (yClick == -1) {
                    if (mouseX >= width - 20 && mouseX < width && mouseY >= START_Y - 1 && mouseY < bottom) {
                        yClick = mouseY;
                    }
                } else {
                    float scrollMultiplier = 1.0F;
                    int height = getHeightDifference();

                    if (height < 1) {
                        height = 1;
                    }

                    int length = (bottom - (START_Y - 1)) * (bottom - (START_Y - 1)) / getContentHeight();
                    if (length < 32) {
                        length = 32;
                    }
                    if (length > bottom - (START_Y - 1) - 8) {
                        length = bottom - (START_Y - 1) - 8;
                    }

                    scrollMultiplier /= (float) (this.bottom - (START_Y - 1) - length) / (float) height;

                    shiftY((int) ((yClick - mouseY) * scrollMultiplier));
                    yClick = mouseY;
                }
            } else {
                yClick = -1;
            }

            fill(poseStack, width - 20, START_Y - 1, width, bottom, Integer.MIN_VALUE);

            int length = (bottom - (START_Y - 1)) * (bottom - (START_Y - 1)) / getContentHeight();
            if (length < 32) {
                length = 32;
            }
            if (length > bottom - (START_Y - 1) - 8) {
                length = bottom - (START_Y - 1) - 8;
            }
            int y = -heightOffset * (this.bottom - (START_Y - 1) - length) / heightDiff + (START_Y - 1);

            if (y < START_Y - 1) {
                y = START_Y - 1;
            }

            fillGradient(poseStack, width - 20, y, width, y + length, 0x80ffffff, 0x80333333);
        }
    }

    private void renderBackground(PoseStack poseStack) {
        renderDirtBackground(poseStack, 0, 0, width, START_Y - 1);
        renderDirtBackground(poseStack, 0, bottom, width, height);
    }

    public void renderDirtBackground(PoseStack poseStack, int xLoc, int yLoc, int width, int height) {
//        RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(Screen.BACKGROUND_LOCATION);
        blit(poseStack, xLoc, yLoc, width, height, 0.0F, 0.0F, width, height, 32, 32);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    // </editor-fold>

    // <editor-fold desc="Sub-window holder.">

    private final List<SubWindowComponent> subWindows = new ArrayList<>();
    private final BiMap<String, SubWindowComponent> mutexSubWindows = HashBiMap.create();
    private SubWindowComponent activeSubWindow = null;

    @Override
    public List<SubWindowComponent> getWindows() {
        var list = ImmutableList.<SubWindowComponent>builder();
        list.addAll(subWindows).addAll(mutexSubWindows.values());
        return list.build();
    }

    @Override
    public SubWindowComponent getActiveWindow() {
        return activeSubWindow;
    }

    @Override
    public void setActiveWindow(SubWindowComponent window) {
        activeSubWindow = window;
    }

    @Override
    public boolean hasWindow() {
        return getWindows().size() > 0;
    }

    @Override
    public boolean hasWindow(SubWindowComponent window) {
        return getWindows().contains(window);
    }

    @Override
    public boolean hasMutexWindow(String name) {
        return mutexSubWindows.containsKey(name);
    }

    @Override
    public void addWindow(SubWindowComponent window) {
        subWindows.add(window);
        setActiveWindow(window);
    }

    @Override
    public void addMutexWindow(String name, SubWindowComponent window) {
        if (hasMutexWindow(name)) {
            throw new RuntimeException("Check has mutex first.");
        }

        mutexSubWindows.put(name, window);
    }

    @Override
    public boolean isMutexWindow(SubWindowComponent window) {
        return mutexSubWindows.containsValue(window);
    }

    @Override
    public void closeWindow(SubWindowComponent window) {
        if (!hasWindow(window)) {
            throw new IllegalStateException("The window is not in this GUI.");
        }

        window.close();
        if (isMutexWindow(window)) {
            var name = mutexSubWindows.inverse().get(window);
            mutexSubWindows.remove(name);
        } else {
            subWindows.remove(window);
        }
    }

    @Override
    public void closeAll() {
        for (var window : subWindows) {
            window.close();
        }
    }

    @Override
    public void focus(SubWindowComponent window) {
        for (var w : getWindows()) {
           w.inactive();
        }

        window.onFocus();
        activeSubWindow = window;
    }

    // </editor-fold>
}
