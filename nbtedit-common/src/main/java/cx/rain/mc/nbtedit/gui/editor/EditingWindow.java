package cx.rain.mc.nbtedit.gui.editor;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.editor.NbtTree;
import cx.rain.mc.nbtedit.editor.NodeParser;
import cx.rain.mc.nbtedit.editor.TagReadingHelper;
import cx.rain.mc.nbtedit.gui.component.ButtonComponent;
import cx.rain.mc.nbtedit.gui.component.EditBoxComponent;
import cx.rain.mc.nbtedit.gui.window.AbstractWindow;
import cx.rain.mc.nbtedit.gui.window.IWindowHolder;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class EditingWindow extends AbstractWindow {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(NBTEdit.MODID, "window");
    public static final int WIDTH = 178;
    public static final int HEIGHT = 93;

    private final NbtTree.Node<?> node;
    private final boolean nameEditable;
    private final boolean valueEditable;

    private EditBoxComponent nameField;
    private EditBoxComponent valueField;

    private boolean parseAsUuid = false;

    public EditingWindow(int x, int y, NbtTree.Node<?> node, boolean nameEditable, boolean valueEditable) {
        super(x, y, WIDTH, HEIGHT, Component.translatable(ModConstants.GUI_TITLE_EDITING_WINDOW));

        this.node = node;
        this.nameEditable = nameEditable;
        this.valueEditable = valueEditable;
    }

    @Override
    public void tick() {
        super.tick();

        if (nameEditable && nameField != null) {
            nameField.tick();
        }

        if (valueEditable && valueField != null) {
            valueField.tick();
        }
    }

    @Override
    protected void createChildren() {
        var name = (nameField == null) ? node.getName() : nameField.getValue();
        var value = (valueField == null) ? NodeParser.getString(node) : valueField.getValue();

        var uuid = TagReadingHelper.tryReadUuid(node.getTag());
        if (uuid != null) {
            value = uuid.toString();
            parseAsUuid = true;
        }

        clearChildren();

        nameField = new EditBoxComponent(getMinecraft().font, getX() + 47, getY() + 20, 116, 15,
                Component.translatable(ModConstants.GUI_EDIT_BOX_NAME));
        nameField.setMaxLength(Integer.MAX_VALUE);
        nameField.setValue(name);
        nameField.setEditable(nameEditable);
        nameField.active = nameEditable;
        nameField.setBordered(false);
        addChild(nameField);

        valueField = new EditBoxComponent(getMinecraft().font, getX() + 47, getY() + 46, 116, 15,
                Component.translatable(ModConstants.GUI_EDIT_BOX_VALUE));
        valueField.setMaxLength(Integer.MAX_VALUE);
        valueField.setValue(value);
        valueField.setEditable(valueEditable);
        valueField.active = valueEditable;
        valueField.setBordered(false);
        addChild(valueField);

        var saveButton = ButtonComponent.getBuilder(Component.translatable(ModConstants.GUI_BUTTON_OK), b -> onOk())
                .pos(getX() + 9, getY() + 62)
                .size(75, 20)
                .createNarration(n -> Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_OK))
                .build();
        addChild(saveButton);

        var cancelButton = ButtonComponent.getBuilder(Component.translatable(ModConstants.GUI_BUTTON_CANCEL), b -> onCancel())
                .pos(getX() + 93, getY() + 62)
                .size(75, 20)
                .createNarration(n -> Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_CANCEL))
                .build();
        addChild(cancelButton);
    }

    private void onOk() {
        if (nameEditable) {
            node.setName(nameField.getValue());
        }

        if (valueEditable) {
            if (parseAsUuid) {
                try {
                    var uuid = UUID.fromString(valueField.getValue());
                    node.setTag(NbtUtils.createUUID(uuid));
                } catch (Exception ignored) {
                }
            } else {
                node.setTag(NodeParser.getTag(node, valueField.getValue()));
            }
        }

        onCancel();
    }

    private void onCancel() {
        if (getParent() != null) {
            getParent().update();

            if (getParent() instanceof IWindowHolder holder) {
                holder.closeWindow(this);
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(TEXTURE, getX(), getY(), getWidth(), getHeight());

        if (!nameEditable) {
            guiGraphics.fill(getX() + 43, getY() + 16, getX() + 170, getY() + 31, 0x80000000);
        }

        if (!valueEditable) {
            guiGraphics.fill(getX() + 43, getY() + 43, getX() + 170, getY() + 59, 0x80000000);
        }

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(ModConstants.GUI_TITLE_EDITING_WINDOW_NARRATION));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            onCancel();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
