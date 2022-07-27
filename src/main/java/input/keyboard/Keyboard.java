package input.keyboard;

import event.EventData;
import event.Events;
import graphics.Window;
import util.Log;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 09.07.2021
 * @since 09.07.2021
 */
public class Keyboard {

    private static final byte[] keyStates = new byte[400];

    public static void setupCallbacks() {
        glfwSetKeyCallback(Window.glfwWindow(), (w, keycode, scancode, action, mods) -> {
            onKeyChange(w, keycode, scancode, action, mods);
            Events.keyEvent.onEvent(new EventData.KeyEventData(keycode, scancode, action, mods));
        });
    }

    public static void onKeyChange(long window, int glfwkey, int scancode, int action, int mods) {
        if (scancode > 400) {
            Log.fatal("Unknown keyboard scancode: " + scancode);
            return;
        }
        keyStates[scancode] = (byte) (InputMod.option(mods) | InputState.option(action));
    }

    public static boolean is(int scanCode, Option... options) {
        byte keyState = keyStates[scanCode];
        return Arrays.stream(options).allMatch(mask -> mask.match(keyState));
    }

    public static byte getKeyState(int scancode) {
        if (scancode > 400) {
            Log.fatal("Unknown keyboard scancode: " + scancode);
            return -1;
        }
        return keyStates[scancode];
    }

}