package input.keyboard;

import java.util.List;

/**
 * @version 12.07.2021
 * @since 12.07.2021
 */
public class KeyInput {

    private final int scancode;
    private final Key virtualKey;
    private final String name;
    private final List<KeyInputResult> resultMap;

    public KeyInput(int scancode, Key virtualKey, String name, List<KeyInputResult> resultMap) {
        this.scancode = scancode;
        this.virtualKey = virtualKey;
        this.name = name;
        this.resultMap = resultMap;
    }

    public String getName() {
        return name;
    }

    public List<KeyInputResult> getResultMap() {
        return resultMap;
    }

    public int getScancode() {
        return scancode;
    }

    public Key getVirtualKey() {
        return virtualKey;
    }

    @Override
    public String toString() {
        return "KeyInput{" +
                "sc=" + scancode +
                ",vk=" + virtualKey +
                ",name='" + name + '\'' +
                '}';
    }
}