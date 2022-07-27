package input.keyboard;

import event.EventData;
import event.Events;
import graphics.Window;
import util.Log;
import util.Observer;

import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 * @version 09.07.2021
 * @since 09.07.2021
 */
public class Keyboard {

    private static KeyboardLayout keyboardLayout;

    private static Map<String, Observer<Integer>> textCodepointObservers = new HashMap<>();

    private static final byte[] keyStates = new byte[Short.MAX_VALUE * 2]; // 0000-FFFF

    public static void setupCallbacks() {
        glfwSetKeyCallback(Window.glfwWindow(), (w, keycode, scancode, action, mods) -> {
            onKeyChange(w, keycode, scancode, action, mods);
            Events.keyEvent.onEvent(new EventData.KeyEventData(keycode, scancode, action, mods));
        });
        glfwSetCharCallback(Window.glfwWindow(), (w, code) -> {
            textCodepointObservers.values().forEach(observer -> observer.notify(code));
        });
    }

    public static void onKeyChange(long window, int glfwkey, int scancode, int action, int mods) {
        if (scancode > 400) {
            Log.fatal("Unknown keyboard scancode: " + scancode);
            return;
        }
        keyStates[scancode] = (byte) (InputMod.option(mods) | InputState.option(action));
    }

    public static void registerTextObserver(String tag, Observer<Integer> observer) {
        textCodepointObservers.put(tag, observer);
    }

    public static void unregisterTextObserver(String tag) {
        textCodepointObservers.remove(tag);
    }

    public static void setKeyboardLayout(KeyboardLayout layout) {
        keyboardLayout = layout;
    }

    public static KeyboardLayout getKeyboardLayout() {
        return keyboardLayout;
    }

    public static boolean isAny(Key key, Option... options) {
        if (keyboardLayout == null) {
            Log.fatal("There is no keyboard layout loaded; this method wont respond without a keyboard layout");
            throw new IllegalStateException("There is no keyboard layout loaded; this method wont respond without a keyboard layout");
        }
        Set<Integer> scancodes = keyboardLayout.getReverse_mapping().getOrDefault(key, Collections.emptySet());
        return scancodes.stream().anyMatch(code -> Arrays.stream(options).anyMatch(mask -> mask.match(keyStates[code])));
    }

    public static boolean isAll(Key key, Option... options) {
        if (keyboardLayout == null) {
            Log.fatal("There is no keyboard layout loaded; this method wont respond without a keyboard layout");
            throw new IllegalStateException("There is no keyboard layout loaded; this method wont respond without a keyboard layout");
        }
        Set<Integer> scancodes = keyboardLayout.getReverse_mapping().getOrDefault(key, Collections.emptySet());
        return scancodes.stream().anyMatch(code -> Arrays.stream(options).allMatch(mask -> mask.match(keyStates[code])));
    }

    public static boolean isAll(int scanCode, Option... options) {
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