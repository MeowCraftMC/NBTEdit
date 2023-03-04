package cx.rain.mc.nbtedit.gui.component.window;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.gui.NBTEditGui;
import cx.rain.mc.nbtedit.gui.component.IWidgetHolder;
import cx.rain.mc.nbtedit.gui.component.button.SpecialCharacterButton;
import cx.rain.mc.nbtedit.nbt.NBTTree;
import cx.rain.mc.nbtedit.utility.Constants;
import cx.rain.mc.nbtedit.nbt.NBTHelper;
import cx.rain.mc.nbtedit.utility.nbt.ParseHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class EditValueSubWindow extends SubWindowComponent implements IWidgetHolder {
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
        setX(getX() + xLoc);
        setY(getY() + yLoc);

//        colorButton = new SpecialCharacterButton((byte) 0, getX()+ width - 1, getY() + 34, this::onColorButtonClicked);
//        newLineButton = new SpecialCharacterButton((byte) 1, getX()+ width - 1, getY() + 50, this::onNewLineButtonClicked);

        String name = (nameField == null) ? node.getName() : nameField.getValue();
        String value = (valueField == null) ? getValue(nbt) : valueField.getValue();

        nameField = new EditBox(getMinecraft().font, getX() + 46, getY() + 18, 116, 15, Component.literal("Name"));
        addWidget(nameField);

        valueField = new EditBox(getMinecraft().font, getX() + 46, getY() + 44, 116, 15, Component.literal("Value"));
        addWidget(valueField);

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
            } else if (canEditValue) {
                valueField.setFocus(true);
            }
        }

//        colorButton.active = valueField.isFocused();
//        newLineButton.active = valueField.isFocused();

        saveButton = Button.builder(Component.translatable(Constants.GUI_BUTTON_SAVE), this::onSaveButtonClicked)
                .pos(getX() + 9, getY() + 62)
                .size(75, 20)
                .build();
        addWidget(saveButton);

        cancelButton = Button.builder(Component.translatable(Constants.GUI_BUTTON_CANCEL), this::onCancelButtonClicked)
                .pos(getX() + 93, getY() + 62)
                .size(75, 20)
                .build();
        addWidget(cancelButton);
    }

    protected void onSaveButtonClicked(Button button) {
        nameField.mouseClicked(button.getX(), button.getY(), 0);
        valueField.mouseClicked(button.getX(), button.getY(), 0);
        save();
        quit();
    }

    protected void onCancelButtonClicked(Button button) {
        nameField.mouseClicked(button.getX(), button.getY(), 0);
        valueField.mouseClicked(button.getX(), button.getY(), 0);
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

        blit(stack, getX(), getY(), 0, 0, width, height);
        if (!canEditName) {
            fill(stack, getX() + 42, getY() + 15, getX() + 169, getY() + 31, 0x80000000);
        }
        if (!canEditValue) {
            fill(stack, getX() + 42, getY() + 41, getX() + 169, getY() + 57, 0x80000000);
        }

        renderWidgets(stack, mouseX, mouseY, partialTicks);

        if (nameError != null) {
            drawCenteredString(stack, getMinecraft().font, nameError, getX() + width / 2, getY() + 4, 0xFF0000);
        }
        if (valueError != null) {
            drawCenteredString(stack, getMinecraft().font, nameError, getX() + width / 2, getY() + 32, 0xFF0000);
        }

//        colorButton.render(stack, mouseX, mouseY, partialTicks);
//        newLineButton.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        nameField.tick();
        valueField.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (nameField.isFocused()) {
            return nameField.keyPressed(keyCode, scanCode, modifiers);
        } else {
            return valueField.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean charTyped(char character, int keyId) {
        if (nameField.isFocused()) {
            nameField.charTyped(character, keyId);
        } else {
            valueField.charTyped(character, keyId);
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Todo: qyl27: remove new line button and color button.
        for (var widget : widgets) {
            if (widget instanceof EditBox) {
                widget.mouseClicked(mouseX, mouseY, button);
            } else if (widget.isMouseOver(mouseX, mouseY)) {
                return widget.mouseClicked(mouseX, mouseY, button);
            }
        }
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narration) {
        narration.add(NarratedElementType.HINT, Component.translatable(Constants.GUI_NARRATION_SUB_WINDOW_VALUE_EDITOR));
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
            case Tag.TAG_BYTE -> {
                var byteTag = (ByteTag) tag;
                return Integer.toString(byteTag.getAsInt());
            }
            case Tag.TAG_SHORT -> {
                var shortTag = (ShortTag) tag;
                return Integer.toString(shortTag.getAsInt());
            }
            case Tag.TAG_LONG -> {
                var longTag = (LongTag) tag;
                return Long.toString(longTag.getAsLong());
            }
            case Tag.TAG_FLOAT -> {
                var floatTag = (FloatTag) tag;
                return Float.toString(floatTag.getAsFloat());
            }
            case Tag.TAG_DOUBLE -> {
                var doubleTag = (DoubleTag) tag;
                return Double.toString(doubleTag.getAsDouble());
            }
            case Tag.TAG_BYTE_ARRAY -> {
                var s = new StringBuilder();
                for (byte b : ((ByteArrayTag) tag).getAsByteArray()) {
                    s.append(b).append(",");
                }
                return s.toString();
            }
            case Tag.TAG_LIST -> {
                return "TagList";
            }
            case Tag.TAG_COMPOUND -> {
                return "TagCompound";
            }
            case Tag.TAG_INT_ARRAY -> {
                var i = new StringBuilder();
                for (int a : ((IntArrayTag) tag).getAsIntArray()) {
                    i.append(a).append(",");
                }
                return i.toString();
            }
            default -> {
                return NBTHelper.toString(tag);
            }
        }
    }

    protected static void setValidValue(NBTTree.Node<Tag> node, String value) {
        Tag tag = node.getTag();
        try {
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
        } catch (NumberFormatException ex) {
            setValidValue(node, "0");   // Todo: qyl27: 但愿人没事。
        }
    }

    @Override
    public void close() {

    }

    @Override
    public List<AbstractWidget> getWidgets() {
        return widgets;
    }

    @Override
    public void addWidget(AbstractWidget widget) {
        widgets.add(widget);
    }

    @Override
    public void clearWidgets() {
        widgets.clear();
    }
}
