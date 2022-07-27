package input.keyboard;

import java.util.Arrays;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 12.07.2021
 * @since 21.04.2021
 */
public enum InputState implements Option {

    PRESSED(GLFW_PRESS, (byte) 1),
    HOLD(GLFW_REPEAT, (byte) 2),
    RELEASED(GLFW_RELEASE, (byte) 4);

    private static final HashMap<Integer, InputState> glfwCodes;

    static {
        glfwCodes = new HashMap<>();
        Arrays.stream(values()).forEach(key -> glfwCodes.put(key.glfwId, key));
    }

    public static InputState getState(int glfwState) {
        return glfwCodes.get(glfwState);
    }

    public static byte option(int glfwState) {
        return getState(glfwState).getMask();
    }

    private final int glfwId;
    private final byte mask;

    InputState(int glfwId, byte mask) {
        this.glfwId = glfwId;
        this.mask = mask;
    }

    @Override
    public byte getMask() {
        return mask;
    }
}