package cx.rain.mc.nbtedit.data.provider.language;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageProviderENUS extends LanguageProvider {
    public LanguageProviderENUS(DataGenerator gen) {
        super(gen, NBTEdit.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Constants.KEY_CATEGORY, "In-game NBTEdit (Reborn)");
        add(Constants.KEY_NBTEDIT_SHORTCUT, "Modify target Entity or BlockEntity NBT");

        add(Constants.MESSAGE_NOT_PLAYER, "Only players can use this command.");
        add(Constants.MESSAGE_NO_PERMISSION, "You have no permission to use NBTEdit.");
        add(Constants.MESSAGE_NOTHING_TO_EDIT, "There is no any target for editing.");
        add(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY, "There is no BlockEntity to edit.");
        add(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER, "Sorry, but you cannot edit other player.");
        add(Constants.MESSAGE_UNKNOWN_ENTITY_ID, "Unknown Entity ID.");

        add(Constants.MESSAGE_EDITING_ENTITY, "Editing Entity {0}.");
        add(Constants.MESSAGE_EDITING_BLOCK_ENTITY, "Editing BlockEntity at {0} {1} {2}");

        add(Constants.MESSAGE_SAVING_SUCCESSFUL, "Saved successfully!");
        add(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT, "Save failed. Invalid NBT.");
        add(Constants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS, "Save failed. the BlockEntity is no longer exists.");
        add(Constants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS, "Save failed. the Entity is no longer exists.");

        add(Constants.GUI_TITLE_NBTEDIT_ENTITY, "Editing Entity {0}");
        add(Constants.GUI_TITLE_NBTEDIT_BLOCK_ENTITY, "Editing BlockEntity in {0} {1} {2}");
        add(Constants.GUI_BUTTON_SAVE, "Save");
        add(Constants.GUI_BUTTON_QUIT, "Quit");
        add(Constants.GUI_BUTTON_LOAD, "Load");
    }
}
