package cx.rain.mc.nbtedit.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.component.EditSubWindowComponent;
import cx.rain.mc.nbtedit.gui.component.NBTNodeComponent;
import cx.rain.mc.nbtedit.gui.component.button.NBTOperatorButton;
import cx.rain.mc.nbtedit.gui.component.button.SaveSlotButton;
import cx.rain.mc.nbtedit.utility.NBTHelper;
import cx.rain.mc.nbtedit.utility.NBTIOHelper;
import cx.rain.mc.nbtedit.utility.nbt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;
import java.util.*;
import java.util.List;

public class NBTEditGui extends Gui {
    private final int START_X = 10;
    private final int START_Y = 30;

    private final int X_GAP = 10;
    private final int Y_GAP = getMinecraft().font.lineHeight + 2;

    protected NBTTree tree;

    protected NBTNode<NamedNBT> focused;
    protected int focusedSaveSlotIndex = -1;

    protected SaveSlotButton[] saves = new SaveSlotButton[7];
    protected List<NBTNodeComponent> nodes = new ArrayList<>();

    protected NBTOperatorButton[] nbtButtons = new NBTOperatorButton[11];

    protected NBTOperatorButton nbtEditButton;
    protected NBTOperatorButton nbtDeleteButton;
    protected NBTOperatorButton nbtCopyButton;
    protected NBTOperatorButton nbtCutButton;
    protected NBTOperatorButton nbtPasteButton;

    protected int width;
    protected int height;
    protected int bottom;

    protected int x;
    protected int y;

    protected int heightDiff;
    protected int heightOffset;

    protected int yClick = -1;

    protected EditSubWindowComponent subWindow = null;

    public NBTEditGui(NBTTree treeIn) {
        super(Minecraft.getInstance());

        tree = treeIn;

        addButtons();
        addSaveSlotButtons();
    }

    // Getter start.

    protected Minecraft getMinecraft() {
        return minecraft;
    }

    public NBTTree getTree() {
        return tree;
    }

    public SaveSlotButton getFocusedSaveSlotIndex() {
        return (focusedSaveSlotIndex != -1) ? saves[focusedSaveSlotIndex] : null;
    }

    public NBTNode<NamedNBT> getFocused() {
        return focused;
    }

    private int getHeightDifference() {
        return getContentHeight() - (bottom - START_Y + 2);
    }

    private int getContentHeight() {
        return Y_GAP * nodes.size();
    }

    // Getter end.

    // Init start.

    public void init(int widthIn, int heightIn, int bottomIn) {
        width = widthIn;
        height = heightIn;
        bottom = bottomIn;

        yClick = -1;
        init(false);

        if (subWindow != null) {
            subWindow.init((width - EditSubWindowComponent.WIDTH) / 2,
                    (height - EditSubWindowComponent.HEIGHT) / 2);
        }
    }

    public void init(boolean isShiftToFocused) {
        y = START_Y;
        nodes.clear();
        addNodes(tree.getRoot(), START_X);
        if (focused != null) {
            if (!checkValidFocus(focused)) {
                setFocused(null);
            }
        }
        if (focusedSaveSlotIndex != -1) {
            saves[focusedSaveSlotIndex].startEditing();
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
    }

    // Init end.

    // Focus start.

    private void addNodes(NBTNode<NamedNBT> root, int start_x) {
        nodes.add(new NBTNodeComponent(start_x, y,
                new TextComponent(NBTHelper.getNBTNameSpecial(root.get())), this, root));

        start_x += X_GAP;
        y += Y_GAP;

        if (root.shouldShowChildren()) {
            for (var child : root.getChildren()) {
                addNodes(child, start_x);
            }
        }
    }

    private void setFocused(NBTNode<NamedNBT> nodeToFocus) {
        updateButtons(nodeToFocus);

        focused = nodeToFocus;
        if (focused != null && focusedSaveSlotIndex != -1) {
            stopEditingSlot();
        }
    }

    public void stopEditingSlot() {
        saves[focusedSaveSlotIndex].stopEditing();
        NBTEdit.getClipboardSaves().save();
        focusedSaveSlotIndex = -1;
    }

    private void shiftToFocus(NBTNode<NamedNBT> focused) {
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

    private int getIndexOf(NBTNode<NamedNBT> focused) {
        for (var i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNode() == focused) {
                return i;
            }
        }
        return -1;
    }

    public void shiftY(int offsetY) {
        if (heightDiff <= 0 || subWindow != null) {
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

    private boolean checkValidFocus(NBTNode<NamedNBT> focused) {
        for (var node : nodes) { // Check all nodes.
            if (node.getNode() == focused) {
                setFocused(focused);
                return true;
            }
        }
        return focused.hasParent() && checkValidFocus(focused.getParent());
    }

    // Focus end.

    // Buttons start.

    private void addButtons() {
        int xLoc = 18;
        int yLoc = 4;

        nbtCopyButton = new NBTOperatorButton(14, xLoc, yLoc, this, this::onCopyButtonClick); // Copy Button.

        xLoc += 15;
        nbtCutButton = new NBTOperatorButton(15, xLoc, yLoc, this, this::onCutButtonClick); // Cut Button.

        xLoc += 15;
        nbtPasteButton = new NBTOperatorButton(16, xLoc, yLoc, this, this::onPasteButtonClick); // Paste Button.

        xLoc += 45;
        nbtEditButton = new NBTOperatorButton(12, xLoc, yLoc, this, this::onEditButtonClick); // Edit Button.

        xLoc += 15;
        nbtDeleteButton = new NBTOperatorButton(13, xLoc, yLoc, this, this::onDeleteButtonClick); // Delete Button.

        // Add nbt buttons.
        xLoc = 18;
        yLoc = 17;
        for (var i = 1; i < 12; i++) {
            nbtButtons[i - 1] = new NBTOperatorButton(i, xLoc, yLoc, this, this::onAddNBTButtonsClick);
            xLoc += 9;
        }
    }

    protected void onEditButtonClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() == 12) {  // 但愿人没事。
                doEditSelected();
            }
        }
    }

    protected void onDeleteButtonClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() == 13) {
                doDeleteSelected();
            }
        }
    }

    protected void onCopyButtonClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() == 14) {
                doCopySelected();
            }
        }
    }

    protected void onCutButtonClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() == 15) {
                doCutSelected();
            }
        }
    }

    protected void onPasteButtonClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() == 16) {
                doPaste();
            }
        }
    }

    protected void onAddNBTButtonsClick(Button button) {
        if (button instanceof NBTOperatorButton nbtOperator) {
            if (nbtOperator.getButtonId() >= 0 && nbtOperator.getButtonId() <= 11) {
                if (focused != null) {
                    focused.setShowChildren(true);
                    var children = focused.getChildren();
                    String type = button.getMessage().getString();

                    if (focused.get().getTag() instanceof ListTag) {
                        var nbt = NBTHelper.newTag((nbtOperator.getButtonId()));
                        if (nbt != null) {
                            var newNode = new NBTNode<>(focused, new NamedNBT("", nbt));
                            children.add(newNode);
                            setFocused(newNode);
                        }
                    } else if (children.size() == 0) {
                        setFocused(insertNode(type + "1", nbtOperator.getButtonId()));
                    } else {
                        for (int i = 1; i <= children.size() + 1; ++i) {
                            String name = type + i;
                            if (isNameValid(name, children)) {
                                setFocused(insertNode(name, nbtOperator.getButtonId()));
                                break;
                            }
                        }
                    }
                    init(true);
                }
            }
        }
    }

    public void doEditSelected() {
        var base = focused.get().getTag();
        var parent = focused.getParent().get().getTag();
        subWindow = new EditSubWindowComponent(this, focused, !(parent instanceof ListTag),
                !(base instanceof CompoundTag || base instanceof ListTag));
        subWindow.init((width - EditSubWindowComponent.WIDTH) / 2, (height - EditSubWindowComponent.HEIGHT) / 2);
    }

    private void doCopySelected() {
        if (focused != null) {
            var named = focused.get();
            if (named.getTag() instanceof ListTag) {
                var list = new ListTag();
                tree.addChild(focused, list);
                NBTEdit.CLIPBOARD = new NamedNBT(named.getName(), list);
            } else if (named.getTag() instanceof CompoundTag) {
                CompoundTag compound = new CompoundTag();
                tree.addChild(focused, compound);
                NBTEdit.CLIPBOARD = new NamedNBT(named.getName(), compound);
            } else {
                NBTEdit.CLIPBOARD = focused.get().copy();
            }

            setFocused(focused);
        }
    }

    private void doCutSelected() {
        doCopySelected();
        doDeleteSelected();
    }

    private void doPaste() {
        if (focused != null) {
            if (NBTEdit.CLIPBOARD != null) {
                focused.setShowChildren(true);

                var namedNBT = NBTEdit.CLIPBOARD.copy();
                if (focused.get().getTag() instanceof ListTag) {
                    namedNBT.setName("");
                    var node = new NBTNode<>(focused, namedNBT);
                    focused.addChild(node);
                    tree.addChildrenToTree(node);
                    tree.sort(node);
                    setFocused(node);
                } else {
                    String name = namedNBT.getName();
                    List<NBTNode<NamedNBT>> children = focused.getChildren();
                    if (!isNameValid(name, children)) {
                        for (int i = 1; i <= children.size() + 1; ++i) {
                            String n = name + "(" + i + ")";
                            if (isNameValid(n, children)) {
                                namedNBT.setName(n);
                                break;
                            }
                        }
                    }
                    NBTNode<NamedNBT> node = insertNode(namedNBT);
                    tree.addChildrenToTree(node);
                    tree.sort(node);
                    setFocused(node);
                }

                init(true);
            }
        }
    }

    public void doDeleteSelected() {
        if (focused != null) {
            if (tree.delete(focused)) {
                NBTNode<NamedNBT> oldFocused = focused;
                shiftFocus(true);
                if (focused == oldFocused)
                    setFocused(null);
                init(false);
            }
        }
    }

    private void updateButtons(NBTNode<NamedNBT> nodeToFocus) {
        if (nodeToFocus == null) {
            activeAllButtons();
        } else if (nodeToFocus.get().getTag() instanceof CompoundTag) {
            activeAllButtons();
            nbtEditButton.active = nodeToFocus.hasParent() && !(nodeToFocus.getParent().get().getTag() instanceof ListTag);
            nbtDeleteButton.active = nodeToFocus != tree.getRoot();
            nbtCutButton.active = nodeToFocus != tree.getRoot();
            nbtPasteButton.active = NBTEdit.CLIPBOARD != null;
        } else if (nodeToFocus.get().getTag() instanceof ListTag) {
            if (nodeToFocus.hasChildren()) {
                var elementType = nodeToFocus.getChildren().get(0).get().getTag().getId();
                inactiveAllButtons();

                nbtButtons[elementType - 1].active = true;

                nbtEditButton.active = !(nodeToFocus.getParent().get().getTag() instanceof ListTag);
                nbtDeleteButton.active = true;
                nbtCopyButton.active = true;
                nbtCutButton.active = true;
                nbtPasteButton.active = NBTEdit.CLIPBOARD != null && NBTEdit.CLIPBOARD.getTag().getId() == elementType;
            } else {
                activeAllButtons();

                nbtEditButton.active = !(nodeToFocus.getParent().get().getTag() instanceof ListTag);
                nbtPasteButton.active = NBTEdit.CLIPBOARD != null;
            }
        } else {
            inactiveAllButtons();

            nbtEditButton.active = true;
            nbtDeleteButton.active = true;
            nbtCopyButton.active = true;
            nbtCutButton.active = true;
        }
    }

    private void activeAllButtons() {
        for (var button : nbtButtons) {
            button.active = true;
        }

        nbtEditButton.active = true;
        nbtDeleteButton.active = true;
        nbtCopyButton.active = true;
        nbtCutButton.active = true;
        nbtPasteButton.active = true;
    }

    private void inactiveAllButtons() {
        for (var button : nbtButtons) {
            button.active = true;
        }

        nbtEditButton.active = true;
        nbtDeleteButton.active = true;
        nbtCopyButton.active = true;
        nbtCutButton.active = true;
        nbtPasteButton.active = true;
    }

    private void addSaveSlotButtons() {
        var saveStates = NBTEdit.getClipboardSaves();
        for (int i = 0; i < 7; ++i) {
            saves[i] = new SaveSlotButton(saveStates.getClipboard(i), width - 24, 31 + i * 25, i + 1, this::onSaveSlotClicked);
        }
    }

    private void onSaveSlotClicked(Button button) {
        if (button instanceof SaveSlotButton saveButton) {
            if (saveButton.getSave().tag.isEmpty()) { //Copy into save slot
                var nbt = (focused == null) ? tree.getRoot() : focused;
                var base = nbt.get().getTag();
                var name = nbt.get().getName();
                if (base instanceof ListTag) {
                    var list = new ListTag();
                    tree.addChild(nbt, list);
                    saveButton.getSave().tag.put(name, list);
                } else if (base instanceof CompoundTag) {
                    var compound = new CompoundTag();
                    tree.addChild(nbt, compound);
                    saveButton.getSave().tag.put(name, compound);
                } else {
                    saveButton.getSave().tag.put(name, base.copy());
                }
                saveButton.saved();
                NBTEdit.getClipboardSaves().save();
                getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            } else { //Paste into
                var nbtMap = NBTIOHelper.getMap(saveButton.getSave().tag);
                if (nbtMap.isEmpty()) {
                    // Todo: AS: Logging.
                } else {
                    if (focused == null) {
                        setFocused(tree.getRoot());
                    }
                    var firstEntry = nbtMap.entrySet().iterator().next();
                    assert firstEntry != null;
                    var name = firstEntry.getKey();
                    var nbt = firstEntry.getValue().copy();
                    if (focused == tree.getRoot() && nbt instanceof CompoundTag && name.equals("ROOT")) {
                        setFocused(null);
                        tree = new NBTTree((CompoundTag) nbt);
                        init(false);
                        getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    } else if (canAddToParent(focused.get().getTag(), nbt)) {
                        focused.setShowChildren(true);
                        for (var it = focused.getChildren().iterator(); it.hasNext(); ) { //Replace object with same name
                            if (it.next().get().getName().equals(name)) {
                                it.remove();
                                break;
                            }
                        }
                        var node = insertNode(new NamedNBT(name, nbt));
                        tree.addChildrenToTree(node);
                        tree.sort(node);
                        setFocused(node);
                        init(true);
                        getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                }
            }
        }
    }

    // Buttons end.

    // Renderer start.

    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        int prevMouseX = mouseX, prevMouseY = mouseY;
        if (subWindow != null) {
            prevMouseX = -1;
            prevMouseY = -1;
        }

        for (var node : nodes) {
            if (node.shouldRender(START_Y - 1, bottom)) {
                node.render(stack, prevMouseX, prevMouseY, partialTick);
            }
        }

        renderBackground(stack, 0, START_Y - 1, 255, 255);
        renderBackground(stack, bottom, height, 255, 255);
        for (var button : nbtButtons) {
            button.render(stack, prevMouseX, prevMouseY, partialTick);
        }

        for (var button : saves) {
            button.render(stack, prevMouseX, prevMouseY, partialTick);
        }

        nbtEditButton.render(stack, prevMouseX, prevMouseY, partialTick);
        nbtDeleteButton.render(stack, prevMouseX, prevMouseY, partialTick);
        nbtCopyButton.render(stack, prevMouseX, prevMouseY, partialTick);
        nbtCutButton.render(stack, prevMouseX, prevMouseY, partialTick);
        nbtPasteButton.render(stack, prevMouseX, prevMouseY, partialTick);

        renderScrollBar(stack, prevMouseX, prevMouseY);

        if (subWindow != null) {
            subWindow.render(stack, mouseX, mouseY, partialTick);
        }
    }

    private void renderScrollBar(PoseStack stack, int mouseX, int mouseY) {
        if (heightDiff > 0) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.MOUSE_BUTTON_LEFT)) {
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
                    if (length < 32)
                        length = 32;
                    if (length > bottom - (START_Y - 1) - 8)
                        length = bottom - (START_Y - 1) - 8;

                    scrollMultiplier /= (float) (this.bottom - (START_Y - 1) - length) / (float) height;


                    shiftY((int) ((yClick - mouseY) * scrollMultiplier));
                    yClick = mouseY;
                }
            } else {
                yClick = -1;
            }

            fill(stack, width - 20, START_Y - 1, width, bottom, Integer.MIN_VALUE);

            int length = (bottom - (START_Y - 1)) * (bottom - (START_Y - 1)) / getContentHeight();
            if (length < 32)
                length = 32;
            if (length > bottom - (START_Y - 1) - 8)
                length = bottom - (START_Y - 1) - 8;
            int y = -heightOffset * (this.bottom - (START_Y - 1) - length) / heightDiff + (START_Y - 1);

            if (y < START_Y - 1) {
                y = START_Y - 1;
            }

            fillGradient(stack, width - 20, y, width, y + length, 0x80ffffff, 0x80333333);
        }
    }

    private void renderBackground(PoseStack stack, int bottom, int height, int alpha1, int alpha2) {
        var tesselator = Tesselator.getInstance();
        var builder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        var f = 32.0F;
        var color = new Color(4210752);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        builder.vertex(0.0D, height, 0.0D).uv(0.0f, (float) height / f).color(color.getRed(), color.getGreen(), color.getBlue(), alpha2).endVertex();
        builder.vertex(width, height, 0.0D).uv((float) width / f, (float) height / f).color(color.getRed(), color.getGreen(), color.getBlue(), alpha2).endVertex();
        builder.vertex(width, bottom, 0.0D).uv((float) width / f, (float) bottom / f).color(color.getRed(), color.getGreen(), color.getBlue(), alpha2).endVertex();
        builder.vertex(0.0D, bottom, 0.0D).uv(0.0f, (float) bottom / f).color(color.getRed(), color.getGreen(), color.getBlue(), alpha2).endVertex();
        tesselator.end();
    }

    // Renderer end.

    // Interact start.

    public void onMouseClicked(int mouseX, int mouseY, int partialTick) {
        if (subWindow == null) {
            boolean reInit = false;

            for (var node : nodes) {
                if (node.spoilerClicked(mouseX, mouseY)) { // Check hide/show children buttons
                    reInit = true;
                    if (node.shouldShowChildren()) {
                        heightOffset = (START_Y + 1) - (node.y) + heightOffset;
                    }
                    break;
                }
            }

            if (!reInit) {
                for (var button : nbtButtons) { //Check top buttons
                    if (button.isMouseInside(mouseX, mouseY)) {
                        onAddNBTButtonsClick(button);
                        return;
                    }
                }

                if (nbtCopyButton.isMouseInside(mouseX, mouseY)) {
                    onCopyButtonClick(nbtCopyButton);
                }

                if (nbtCutButton.isMouseInside(mouseX, mouseY)) {
                    onCutButtonClick(nbtCutButton);
                }

                if (nbtPasteButton.isMouseInside(mouseX, mouseY)) {
                    onPasteButtonClick(nbtPasteButton);
                }

                if (nbtEditButton.isMouseInside(mouseX, mouseY)) {
                    onEditButtonClick(nbtEditButton);
                }

                if (nbtDeleteButton.isMouseInside(mouseX, mouseY)) {
                    onDeleteButtonClick(nbtDeleteButton);
                }

                for (var button : saves) {
                    if (button.isMouseInside(mouseX, mouseY)) {
                        button.reset();
                        NBTEdit.getClipboardSaves().save();
                        getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        return;
                    }

                    if (button.isMouseInside(mouseX, mouseY)) {
                        onSaveSlotClicked(button);
                        return;
                    }
                }
                if (mouseY >= START_Y && mouseX <= width - 175) { //Check actual nodes, remove focus if nothing clicked
                    NBTNode<NamedNBT> newFocus = null;
                    for (var node : nodes) {
                        if (node.isTextClicked(mouseX, mouseY)) {
                            newFocus = node.getNode();
                            break;
                        }
                    }
                    if (focusedSaveSlotIndex != -1) {
                        stopEditingSlot();
                    }

                    setFocused(newFocus);
                }
            } else {
                init(false);
            }
        } else {
            subWindow.onMouseClicked(mouseX, mouseY, partialTick);
        }
    }

    public void closeSubWindow() {
        subWindow = null;
    }

    public void update() {
        if (subWindow != null) {
            subWindow.update();
        }

        if (focusedSaveSlotIndex != -1) {
            saves[focusedSaveSlotIndex].update();
        }
    }

    public EditSubWindowComponent getSubWindow() {
        return subWindow;
    }

    // Interact end.

    // Keyboard interact start.

    public void keyPressed(int mouseX, int mouseY, int delta) {
        if (subWindow != null) {
            subWindow.keyPressed(mouseX, mouseY, delta);
        }
    }

    public void arrowKeyPressed(boolean isUp) {
        if (focused == null) {
            shiftY((isUp) ? Y_GAP : -Y_GAP);
        } else {
            shiftFocus(isUp);
        }
    }

    public void charTyped(char character, int keyId) {
        if (subWindow != null) {
            subWindow.charTyped(character, keyId);
        } else if (focusedSaveSlotIndex != -1) {
            saves[focusedSaveSlotIndex].charTyped(character, keyId);
        } else {
            if (keyId == InputConstants.KEY_C && Screen.hasControlDown()) {
                doCopySelected();
            }
            if (keyId == InputConstants.KEY_V && Screen.hasControlDown() && canPaste()) {
                doPaste();
            }
            if (keyId == InputConstants.KEY_X && Screen.hasControlDown()) {
                doCutSelected();
            }
        }
    }

    // Keyboard interact end.

    // Nodes start.

    public void updateNode(NBTNode<NamedNBT> node) {
        var parent = node.getParent();
        Collections.sort(parent.getChildren(), NBTSortHelper.get());
        init(true);
    }

    private NBTNode<NamedNBT> insertNode(NamedNBT nbt) {
        var newNode = new NBTNode<>(focused, nbt);

        if (focused.hasChildren()) {
            var children = focused.getChildren();

            var added = false;
            for (int i = 0; i < children.size(); ++i) {
                if (NBTSortHelper.get().compare(newNode, children.get(i)) < 0) {
                    children.add(i, newNode);
                    added = true;
                    break;
                }
            }
            if (!added) {
                children.add(newNode);
            }
        } else {
            focused.addChild(newNode);
        }
        return newNode;
    }

    private NBTNode<NamedNBT> insertNode(String name, byte type) {
        var nbt = NBTHelper.newTag(type);
        if (nbt != null) {
            return insertNode(new NamedNBT(name, nbt));
        }
        return null;
    }

    public boolean isEditingSlot() {
        return focusedSaveSlotIndex != -1;
    }

    // Nodes end.

    // Misc start.

    private boolean isNameValid(String name, List<NBTNode<NamedNBT>> children) {
        for (var node : children) {
            if (node.get().getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean canPaste() {
        return NBTEdit.CLIPBOARD != null && focused != null
                && canAddToParent(focused.get().getTag(), NBTEdit.CLIPBOARD.getTag());
    }

    private boolean canAddToParent(Tag parent, Tag child) {
        if (parent instanceof CompoundTag) {
            return true;
        }

        if (parent instanceof ListTag) {
            var list = (ListTag) parent;
            return list.size() == 0 || list.getElementType() == child.getId();
        }
        return false;
    }

    // Misc end.
}
