package cx.rain.mc.nbtedit.api.command;

public enum ModPermissions {
    USE(2, "use"),
    READ_ONLY(1, "read_only"),
    EDIT_ON_PLAYER(4, "edit_on_player"),
    ;

    private final int level;
    private final String name;

    ModPermissions(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getDefaultLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
