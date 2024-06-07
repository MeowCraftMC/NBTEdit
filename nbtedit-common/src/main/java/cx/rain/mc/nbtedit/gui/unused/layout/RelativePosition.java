package cx.rain.mc.nbtedit.gui.unused.layout;

public class RelativePosition extends Position {
    private final Position parent;
    private final int relativeX;
    private final int relativeY;

    public RelativePosition(Position parent, int relativeX, int relativeY) {
        super(parent.getX(), parent.getY());
        this.parent = parent;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    public int getX() {
        return parent.getX() + relativeX;
    }

    public int getY() {
        return parent.getY() + relativeY;
    }

    public int getRelativeX() {
        return relativeX;
    }

    public int getRelativeY() {
        return relativeY;
    }
}
