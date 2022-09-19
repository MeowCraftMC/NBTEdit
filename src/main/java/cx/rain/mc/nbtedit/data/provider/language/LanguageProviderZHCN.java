package cx.rain.mc.nbtedit.data.provider.language;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageProviderZHCN extends LanguageProvider {
    public LanguageProviderZHCN(DataGenerator gen) {
        super(gen, NBTEdit.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(Constants.KEY_CATEGORY, "游戏内 NBT 修改器 （重制版）");
        add(Constants.KEY_NBTEDIT_SHORTCUT, "修改所指向内容的 NBT");

        add(Constants.MESSAGE_NOT_PLAYER, "只有在游戏中的玩家可以使用这个命令！");
        add(Constants.MESSAGE_NO_PERMISSION, "你没有权限使用 NBTEdit！");
        add(Constants.MESSAGE_NOTHING_TO_EDIT, "没有任何目标可供编辑。");
        add(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY, "没有目标方块实体可供编辑。");
        add(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER, "你不能编辑其他玩家！");
        add(Constants.MESSAGE_UNKNOWN_ENTITY_ID, "无效的实体 ID！");

        add(Constants.MESSAGE_EDITING_ENTITY, "正在编辑实体 %s。");
        add(Constants.MESSAGE_EDITING_BLOCK_ENTITY, "正在编辑位于 %s %s %s 的方块实体。");

        add(Constants.MESSAGE_SAVING_SUCCESSFUL, "保存成功！");
        add(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT, "保存失败。无效的 NBT！");
        add(Constants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS, "保存失败。目标方块实体已经不存在了！");
        add(Constants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS, "保存失败。目标实体不存在了！");

        add(Constants.GUI_TITLE_NBTEDIT_ENTITY, "正在编辑实体 ID： {0}");
        add(Constants.GUI_TITLE_NBTEDIT_BLOCK_ENTITY, "正在编辑位于 {0} {1} {2} 的方块实体");
        add(Constants.GUI_BUTTON_SAVE, "保存");
        add(Constants.GUI_BUTTON_LOAD, "加载");
        add(Constants.GUI_BUTTON_QUIT, "退出");
        add(Constants.GUI_BUTTON_CANCEL, "取消");
    }
}
