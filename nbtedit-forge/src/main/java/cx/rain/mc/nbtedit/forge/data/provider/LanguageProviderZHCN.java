package cx.rain.mc.nbtedit.forge.data.provider;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageProviderZHCN extends LanguageProvider {
    public LanguageProviderZHCN(PackOutput packOutput) {
        super(packOutput, NBTEdit.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(Constants.KEY_CATEGORY, "游戏内 NBT 修改器 （重制版）");
        add(Constants.KEY_NBTEDIT_SHORTCUT, "修改所指向内容的 NBT");

        add(Constants.MESSAGE_NOT_PLAYER, "只有在游戏中的玩家可以使用这个命令！");
        add(Constants.MESSAGE_NO_PERMISSION, "你没有权限使用 NBTEdit！");
        add(Constants.MESSAGE_NOT_LOADED, "方块还未加载！");
        add(Constants.MESSAGE_NOTHING_TO_EDIT, "没有任何目标可供编辑。");
        add(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY, "没有目标方块实体可供编辑。");
        add(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER, "你不能编辑其他玩家！");
        add(Constants.MESSAGE_UNKNOWN_ENTITY_ID, "无效的实体 ID！");

        add(Constants.MESSAGE_EDITING_ENTITY, "正在编辑实体 %1$s。");
        add(Constants.MESSAGE_EDITING_BLOCK_ENTITY, "正在编辑位于 %1$s %2$s %3$s 的方块实体。");
        add(Constants.MESSAGE_EDITING_ITEM_STACK, "正在编辑名为 %1$s 的物品。");

        add(Constants.MESSAGE_SAVING_SUCCESSFUL, "保存成功！");
        add(Constants.MESSAGE_SAVING_FAILED_INVALID_NBT, "保存失败。无效的 NBT！");
        add(Constants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS, "保存失败。目标方块实体已经不存在了！");
        add(Constants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS, "保存失败。目标实体不存在了！");

        add(Constants.GUI_TITLE_NBTEDIT_ENTITY, "正在编辑实体 ID： %1$s");
        add(Constants.GUI_TITLE_NBTEDIT_BLOCK_ENTITY, "正在编辑位于 %1$s %2$s %3$s 的方块实体");
        add(Constants.GUI_TITLE_NBTEDIT_ITEM_STACK, "正在编辑名为 %1$s 的物品");
        add(Constants.GUI_BUTTON_SAVE, "保存");
        add(Constants.GUI_BUTTON_LOAD, "加载");
        add(Constants.GUI_BUTTON_QUIT, "退出");
        add(Constants.GUI_BUTTON_CANCEL, "取消");

        add(Constants.GUI_NARRATION_BUTTON_COPY, "复制按钮");
        add(Constants.GUI_NARRATION_BUTTON_PASTE, "粘贴按钮");
        add(Constants.GUI_NARRATION_BUTTON_CUT, "剪切按钮");
        add(Constants.GUI_NARRATION_BUTTON_EDIT, "编辑按钮");
        add(Constants.GUI_NARRATION_BUTTON_DELETE, "删除按钮");
        add(Constants.GUI_NARRATION_BUTTON_ADD, "添加按钮");
        add(Constants.GUI_NARRATION_BUTTON_SPECIAL_CHARACTER, "插入特殊符号");
        add(Constants.GUI_NARRATION_SUB_WINDOW_VALUE_EDITOR, "数值编辑子窗口");

        add(Constants.GUI_TOOLTIP_PREVIEW_COMPONENT, "【文本预览】");
        add(Constants.GUI_NARRATION_TOOLTIP_PREVIEW_COMPONENT, "文本预览：");
        add(Constants.GUI_TOOLTIP_PREVIEW_ITEM, "【物品预览】");
        add(Constants.GUI_NARRATION_TOOLTIP_PREVIEW_ITEM, "物品预览：");
        add(Constants.GUI_TOOLTIP_PREVIEW_UUID, "【UUID 预览】");
        add(Constants.GUI_NARRATION_TOOLTIP_PREVIEW_UUID, "UUID 预览：");
    }
}
