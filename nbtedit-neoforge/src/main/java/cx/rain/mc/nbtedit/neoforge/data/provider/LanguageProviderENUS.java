package cx.rain.mc.nbtedit.neoforge.data.provider;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LanguageProviderENUS extends LanguageProvider {
    public LanguageProviderENUS(PackOutput packOutput) {
        super(packOutput, NBTEdit.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Constants.KEY_CATEGORY, "In-game NBTEdit Reborn");
        add(Constants.KEY_NBTEDIT_SHORTCUT, "Open the NBT editor");

        add(Constants.MESSAGE_NOT_PLAYER, "Only players can use this command.");
        add(Constants.MESSAGE_NO_PERMISSION, "You have no permission to use NBTEdit!");
        add(Constants.MESSAGE_NOT_LOADED, "Block pos was not loaded.");
        add(Constants.MESSAGE_NOTHING_TO_EDIT, "There is no any target for editing.");
        add(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY, "There is no BlockEntity to edit.");
        add(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER, "Sorry, but you cannot edit other player.");
        add(Constants.MESSAGE_UNKNOWN_ENTITY_ID, "Invalid Entity ID.");
        add(Constants.MESSAGE_EDITING_ENTITY, "Editing Entity (%1$s).");
        add(Constants.MESSAGE_EDITING_BLOCK_ENTITY, "Editing BlockEntity (%1$s, %2$s, %3$s).");
        add(Constants.MESSAGE_EDITING_ITEM_STACK, "Editing ItemStack (%1$s).");
        add(Constants.MESSAGE_SAVING_SUCCESSFUL, "Save successful!");
        add(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT, "Save failed! Invalid NBT.");
        add(Constants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS, "Save failed! the BlockEntity is no longer exists.");
        add(Constants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS, "Save failed! the Entity is no longer exists.");

        add(Constants.NBT_TYPE_BYTE, "Byte");
        add(Constants.NBT_TYPE_SHORT, "Short");
        add(Constants.NBT_TYPE_INT, "Integer");
        add(Constants.NBT_TYPE_LONG, "Long");
        add(Constants.NBT_TYPE_FLOAT, "Float");
        add(Constants.NBT_TYPE_DOUBLE, "Double");
        add(Constants.NBT_TYPE_BYTE_ARRAY, "Byte Array");
        add(Constants.NBT_TYPE_STRING, "String");
        add(Constants.NBT_TYPE_LIST, "List");
        add(Constants.NBT_TYPE_COMPOUND, "Compound");
        add(Constants.NBT_TYPE_INT_ARRAY, "Integer Array");
        add(Constants.NBT_TYPE_LONG_ARRAY, "Long Array");

        add(Constants.GUI_TITLE_NBTEDIT_ENTITY, "Editing Entity (%1$s)");
        add(Constants.GUI_TITLE_NBTEDIT_BLOCK_ENTITY, "Editing BlockEntity (%1$s, %2$s, %3$s)");
        add(Constants.GUI_TITLE_NBTEDIT_ITEM_STACK, "Editing ItemStack (%1$s)");
        add(Constants.GUI_TITLE_EDITOR_ENTITY_NARRATION, "Entity NBT editor");
        add(Constants.GUI_TITLE_EDITOR_BLOCK_ENTITY_NARRATION, "BlockEntity NBT editor");
        add(Constants.GUI_TITLE_EDITOR_ITEM_STACK_NARRATION, "ItemStack NBT editor");

        add(Constants.GUI_TITLE_TREE_VIEW, "NBT Tree");
        add(Constants.GUI_TITLE_TREE_VIEW_NODE, "NBT Node");
        add(Constants.GUI_TITLE_TREE_VIEW_NARRATION, "NBT Node Tree");
        add(Constants.GUI_TITLE_TREE_VIEW_NODE_NARRATION, "NBT Node: %1$s");

        add(Constants.GUI_TITLE_SCROLL_BAR, "Scrollbar");
        add(Constants.GUI_TITLE_SCROLL_BAR_NARRATION, "Drag to scroll");

        add(Constants.GUI_BUTTON_COPY, "Copy");
        add(Constants.GUI_BUTTON_PASTE, "Paste");
        add(Constants.GUI_BUTTON_CUT, "Cut");
        add(Constants.GUI_BUTTON_EDIT, "Edit");
        add(Constants.GUI_BUTTON_DELETE, "Delete");
        add(Constants.GUI_BUTTON_ADD, "Add %1$s Tag");
        add(Constants.GUI_BUTTON_SAVE, "Save");
        add(Constants.GUI_BUTTON_QUIT, "Quit");

        add(Constants.GUI_TOOLTIP_BUTTON_COPY, "Copy selected tag.");
        add(Constants.GUI_TOOLTIP_BUTTON_PASTE, "Paste tag into selected container.");
        add(Constants.GUI_TOOLTIP_BUTTON_CUT, "Cut selected tag.");
        add(Constants.GUI_TOOLTIP_BUTTON_EDIT, "Edit selected tag.");
        add(Constants.GUI_TOOLTIP_BUTTON_DELETE, "Delete selected tag.");
        add(Constants.GUI_TOOLTIP_BUTTON_ADD, "Add tag typed %1$s.");

        add(Constants.GUI_TOOLTIP_BUTTON_SAVE, "Save and quit the editor.");
        add(Constants.GUI_TOOLTIP_BUTTON_QUIT, "Discard and quit the editor.");

        add(Constants.GUI_TOOLTIP_PREVIEW_COMPONENT, "[Text Preview] ");
        add(Constants.GUI_TOOLTIP_PREVIEW_COMPONENT_NARRATION, "Text Preview: ");
        add(Constants.GUI_TOOLTIP_PREVIEW_ITEM, "[Item Preview] ");
        add(Constants.GUI_TOOLTIP_PREVIEW_ITEM_NARRATION, "Item Preview: ");
        add(Constants.GUI_TOOLTIP_PREVIEW_UUID, "[UUID Preview] ");
        add(Constants.GUI_TOOLTIP_PREVIEW_UUID_NARRATION, "UUID Preview: ");
    }
}
