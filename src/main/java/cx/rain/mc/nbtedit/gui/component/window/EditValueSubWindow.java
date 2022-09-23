package cx.rain.mc.nbtedit.gui.component.window;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.gui.component.button.SpecialCharacterButton;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.utility.Constants;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import cx.rain.mc.nbtedit.utility.nbt.ParseHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class EditValueSubWindow extends SubWindowComponent {
    public static final ResourceLocation WINDOW_TEXTURE =
            new ResourceLocation(NBTEdit.MODID, "textures/gui/window.png");
    public static final int WIDTH = 178;
    public static final int HEIGHT = 93;

    protected Tag nbt;
    protected NBTEditGui gui;
    protected NBTTree.Node<?> node;
    protected boolean canEditName;
    protected boolean canEditValue;

    protected List<AbstractWidget> widgets = new ArrayList<>();

    protected EditBox nameField;
    protected EditBox valueField;

    protected Button saveButton;
    protected Button cancelButton;

    protected SpecialCharacterButton colorButton;
    protected SpecialCharacterButton newLineButton;

    protected String nameError;
    protected String valueError;

    public EditValueSubWindow(NBTEditGui parent, NBTTree.Node<?> nodeIn,
                              boolean canEditNameIn, boolean canEditValueIn) {
        super(0, 0, 178, 93, Component.literal("NBTEdit sub-window"), parent);

        gui = parent;
        node = nodeIn;
        nbt = nodeIn.getTag();
        canEditName = canEditNameIn;
        canEditValue = canEditValueIn;
    }

    public void init(int xLoc, int yLoc) {
        x = xLoc;
        y = yLoc;

//        colorButton = new SpecialCharacterButton((byte) 0, x + width - 1, y + 34, this::onColorButtonClicked);
//        newLineButton = new SpecialCharacterButton((byte) 1, x + width - 1, y + 50, this::onNewLineButtonClicked);

        String name = (nameField == null) ? node.getName() : nameField.getValue();
        String value = (valueField == null) ? getValue(nbt) : valueField.getValue();

        nameField = new EditBox(getMinecraft().font, x + 46, y + 18, 116, 15, Component.literal("Name"));
        widgets.add(nameField);

        valueField = new EditBox(getMinecraft().font, x + 46, y + 44, 116, 15, Component.literal("Value"));
        widgets.add(valueField);

        nameField.setValue(name);
        nameField.setEditable(canEditName);
        nameField.setBordered(false);

        valueField.setMaxLength(256);
        valueField.setValue(value);
        valueField.setEditable(canEditValue);
        valueField.setBordered(false);

        if (!nameField.isFocused() && !valueField.isFocused()) {
            if (canEditName) {
                nameField.setFocus(true);
            }
            else if (canEditValue) {
                valueField.setFocus(true);
            }
        }

        colorButton.active = valueField.isFocused();
        newLineButton.active = valueField.isFocused();

        saveButton = new Button(x + 9, y + 62, 75, 20,
                Component.translatable(Constants.GUI_BUTTON_SAVE), this::onSaveButtonClicked);
        widgets.add(saveButton);

        cancelButton = new Button(x + 93, y + 62, 75, 20,
                Component.translatable(Constants.GUI_BUTTON_CANCEL), this::onCancelButtonClicked);
        widgets.add(cancelButton);
    }

    protected void onSaveButtonClicked(Button button) {
        nameField.mouseClicked(button.x, button.y, 0);
        valueField.mouseClicked(button.x, button.y, 0);
        save();
        quit();
    }

    protected void onCancelButtonClicked(Button button) {
        nameField.mouseClicked(button.x, button.y, 0);
        valueField.mouseClicked(button.x, button.y, 0);
        quit();
    }

    protected void onNewLineButtonClicked(Button button) {
        valueField.insertText("\n");
        isValidInput();
    }

    protected void onColorButtonClicked(Button button) {
        valueField.insertText("" + NBTHelper.SECTION_SIGN);
        isValidInput();
    }

    private void save() {
        if (canEditName) {
            node.setName(nameField.getValue());
        }
        setValidValue(((NBTTree.Node<Tag>) node), valueField.getValue());
        gui.update(node);
    }

    private void quit() {
        gui.closeWindow(this);
    }

    protected Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);

        blit(stack, x, y, 0, 0, width, height);
        if (!canEditName) {
            fill(stack, x + 42, y + 15, x + 169, y + 31, 0x80000000);
        }
        if (!canEditValue) {
            fill(stack, x + 42, y + 41, x + 169, y + 57, 0x80000000);
        }

        nameField.render(stack, mouseX, mouseY, partialTicks);
        valueField.render(stack, mouseX, mouseY, partialTicks);

        saveButton.renderButton(stack, mouseX, mouseY, partialTicks);
        cancelButton.renderButton(stack, mouseX, mouseY, partialTicks);

        if (nameError != null) {
            drawCenteredString(stack, getMinecraft().font, nameError, x + width / 2, y + 4, 0xFF0000);
        }
        if (valueError != null) {
            drawCenteredString(stack, getMinecraft().font, nameError, x + width / 2, y + 32, 0xFF0000);
        }

        colorButton.render(stack, mouseX, mouseY, partialTicks);
        newLineButton.render(stack, mouseX, mouseY, partialTicks);
    }

//    public void update() {
//        nameField.tick();
//        valueField.tick();
//    }
//
//    public void setWindowTop(int xIn, int yIn) {
//        x = xIn;
//        y = yIn;
//    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (nameField.isFocused()) {
            return nameField.keyPressed(keyCode, scanCode, modifiers);
        } else {
            return valueField.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Todo: qyl27: remove new line button and color button.
        for (var widget : widgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                return widget.mouseClicked(mouseX, mouseY, button);
            }
        }
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput narration) {
        narration.add(NarratedElementType.HINT, "NBTEdit sub-window.");
    }

    private void isValidInput() {
        boolean isValid = true;
        nameError = null;
        valueError = null;
        if (canEditName && !isNameValid()) {
            isValid = false;
            nameError = "Duplicated Tag Name."; // Todo: qyl: i18n here.
        }
        try {
            isValueValid(valueField.getValue(), nbt.getId());
        } catch (NumberFormatException e) {
            valueError = e.getMessage();
            isValid = false;
        }
        saveButton.active = isValid;
    }

    private boolean isNameValid() {
        for (var n : node.getParent().getChildren()) {
            Tag base = n.getTag();
            if (base != nbt && n.getName().equals(nameField.getValue())) {
                return false;
            }
        }
        return true;
    }

    private static void isValueValid(String value, byte type) throws NumberFormatException {
        switch (type) {
            case 1 -> ParseHelper.parseByte(value);
            case 2 -> ParseHelper.parseShort(value);
            case 3 -> ParseHelper.parseInt(value);
            case 4 -> ParseHelper.parseLong(value);
            case 5 -> ParseHelper.parseFloat(value);
            case 6 -> ParseHelper.parseDouble(value);
            case 7 -> ParseHelper.parseByteArray(value);
            case 11 -> ParseHelper.parseIntArray(value);
        }
    }

    private static String getValue(Tag tag) {
        switch (tag.getId()) {
            case 7:
                var s = new StringBuilder();
                for (byte b : ((ByteArrayTag) tag).getAsByteArray()) {
                    s.append(b).append(" ");
                }
                return s.toString();
            case 9:
                return "TagList";
            case 10:
                return "TagCompound";
            case 11:
                var i = new StringBuilder();
                for (int a : ((IntArrayTag) tag).getAsIntArray()) {
                    i.append(a).append(" ");
                }
                return i.toString();
            default:
                return NBTHelper.toString(tag);
        }
    }

    protected static void setValidValue(NBTTree.Node<Tag> node, String value) {
        Tag tag = node.getTag();

        if (tag instanceof ByteTag) {
            node.setTag(ByteTag.valueOf(ParseHelper.parseByte(value)));
        }
        if (tag instanceof ShortTag) {
            node.setTag(ShortTag.valueOf(ParseHelper.parseShort(value)));
        }
        if (tag instanceof IntTag) {
            node.setTag(IntTag.valueOf(ParseHelper.parseInt(value)));
        }
        if (tag instanceof LongTag) {
            node.setTag(LongTag.valueOf(ParseHelper.parseLong(value)));
        }
        if (tag instanceof FloatTag) {
            node.setTag(FloatTag.valueOf(ParseHelper.parseFloat(value)));
        }
        if (tag instanceof DoubleTag) {
            node.setTag(DoubleTag.valueOf(ParseHelper.parseDouble(value)));
        }
        if (tag instanceof ByteArrayTag) {
            node.setTag(new ByteArrayTag(ParseHelper.parseByteArray(value)));
        }
        if (tag instanceof IntArrayTag) {
            node.setTag(new IntArrayTag(ParseHelper.parseIntArray(value)));
        }
        if (tag instanceof StringTag) {
            node.setTag(StringTag.valueOf(value));
        }
    }

    @Override
    public void close() {

    }
}
