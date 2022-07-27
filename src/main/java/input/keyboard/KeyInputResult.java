package input.keyboard;

/**
 * @version 27.07.2022
 * @since 27.07.2022
 */
public class KeyInputResult {

    private final String text;
    private final int textCodepoint;
    private final Key[] combinedWith;
    private final Key virtualKey;
    private final DeadKeyTable deadKeyTable;

    public KeyInputResult(String text, int textCodepoint, Key[] combinedWith, Key virtualKey, DeadKeyTable deadKeyTable) {
        this.text = text;
        this.textCodepoint = textCodepoint;
        this.combinedWith = combinedWith;
        this.virtualKey = virtualKey;
        this.deadKeyTable = deadKeyTable;
    }

    public String getText() {
        return text;
    }

    public Key[] getCombinedWith() {
        return combinedWith;
    }

    public Key getVirtualKey() {
        return virtualKey;
    }

    public int getTextCodepoint() {
        return textCodepoint;
    }

    public DeadKeyTable getDeadKeyTable() {
        return deadKeyTable;
    }
}