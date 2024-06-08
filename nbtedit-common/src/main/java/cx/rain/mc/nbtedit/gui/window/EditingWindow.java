package cx.rain.mc.nbtedit.gui.window;

import cx.rain.mc.nbtedit.editor.NbtTree;
import cx.rain.mc.nbtedit.editor.NodeParser;
import cx.rain.mc.nbtedit.gui.component.ButtonComponent;
import cx.rain.mc.nbtedit.gui.component.EditBoxComponent;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class EditingWindow extends AbstractWindow {
    private final NbtTree.Node<?> node;
    private final boolean nameEditable;
    private final boolean valueEditable;

    private EditBoxComponent nameField;
    private EditBoxComponent valueField;

    public EditingWindow(int x, int y, int width, int height, NbtTree.Node<?> node, boolean nameEditable, boolean valueEditable) {
        super(x, y, width, height, Component.translatable(ModConstants.GUI_TITLE_EDITING_WINDOW));

        this.node = node;
        this.nameEditable = nameEditable;
        this.valueEditable = valueEditable;

        update();
    }

    @Override
    public void tick() {
        super.tick();

        nameField.tick();
        valueField.tick();
    }

    @Override
    public void update() {
        super.update();

        var name = (nameField == null) ? node.getName() : nameField.getValue();
        var value = (valueField == null) ? NodeParser.getString(node) : valueField.getValue();

        clearChildren();

        nameField = new EditBoxComponent(getMinecraft().font, getX() + 46, getY() + 18, 116, 15,
                Component.translatable(ModConstants.GUI_EDIT_BOX_NAME));
        nameField.setMaxLength(Integer.MAX_VALUE);
        nameField.setValue(name);
        nameField.setEditable(nameEditable);
        nameField.setBordered(true);
        addChild(nameField);

        valueField = new EditBoxComponent(getMinecraft().font, getX() + 46, getY() + 44, 116, 15,
                Component.translatable(ModConstants.GUI_EDIT_BOX_VALUE));
        nameField.setMaxLength(Integer.MAX_VALUE);
        nameField.setValue(value);
        nameField.setEditable(valueEditable);
        nameField.setBordered(true);
        addChild(valueField);

        var saveButton = new ButtonComponent(getX() + 9, getY() + 62, 75, 20,
                Component.translatable(ModConstants.GUI_BUTTON_OK),
                b -> onOk(),
                n -> Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_OK));
        addChild(saveButton);

        var cancelButton = new ButtonComponent(getX() + 93, getY() + 62, 75, 20,
                Component.translatable(ModConstants.GUI_BUTTON_CANCEL),
                b -> onCancel(),
                n -> Component.translatable(ModConstants.GUI_TOOLTIP_BUTTON_CANCEL));
        addChild(cancelButton);
    }

    private void onOk() {
        if (nameEditable) {
            node.setName(nameField.getValue());
        }

        if (valueEditable) {
            node.setTag(NodeParser.getTag(node, valueField.getValue()));
        }

        if (getParent() != null) {
            getParent().update();
        }

        onCancel();
    }

    private void onCancel() {
        if (getParent() instanceof IWindowHolder holder) {
            holder.closeWindow(this);
        }
    }

    @Override
    public void onClose() {
        super.onClose();

        if (getParent() != null) {
            getParent().update();
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(ModConstants.GUI_TITLE_EDITING_WINDOW_NARRATION));
    }
}
