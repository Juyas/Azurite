package input;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>Azurite</h1>
 * Standard codes: https://kbdlayout.info/kbdusx/scancodes
 *
 * @author Juyas
 * @version 12.07.2021
 * @since 12.07.2021
 */
public enum Key {

    ESCAPE(0x01),
    KEY_1(0x02),
    KEY_2(0x03),
    KEY_3(0x04),
    KEY_4(0x05),
    KEY_5(0x06),
    KEY_6(0x07),
    KEY_7(0x08),
    KEY_8(0x09),
    KEY_9(0x0A),
    KEY_0(0x0B),
    KEY_OEM_MINUS(0x0C),
    KEY_OEM_PLUS(0x0D),
    KEY_BACK(0x0E),
    KEY_TAB(0x0F),
    KEY_Q(0x10),
    KEY_W(0x11),
    KEY_E(0x12),
    KEY_R(0x13),
    KEY_T(0x14),
    KEY_Y(0x15),
    KEY_U(0x16),
    KEY_I(0x17),
    KEY_O(0x18),
    KEY_P(0x19),
    KEY_OEM_4(0x1A),
    KEY_OEM_6(0x1B),
    KEY_RETURN(0x1C, 0xE0),
    KEY_LCONTROL(0x1D),
    KEY_PAUSE(0x1D, 0xE1),
    KEY_A(0x1E),
    KEY_S(0x1F),
    KEY_D(0x20),
    KEY_F(0x21),
    KEY_G(0x22),
    KEY_H(0x23),
    KEY_J(0x24),
    KEY_K(0x25),
    KEY_L(0x26),
    KEY_OEM_1(0x27),
    KEY_OEM_7(0x28),
    KEY_OEM_3(0x29),
    KEY_LSHIFT(0x2A),
    KEY_OEM_5(0x2B),
    KEY_Z(0x2C),
    KEY_X(0x2D),
    KEY_C(0x2E),
    KEY_V(0x2F),
    KEY_B(0x30),
    KEY_N(0x31),
    KEY_M(0x32),
    KEY_OEM_COMMA(0x33),
    KEY_OEM_PERIOD(0x34),
    KEY_OEM_2(0x35),
    KEY_DIVIDE(0x35, 0xE0),
    KEY_RSHIFT(0x36),
    KEY_MULTIPLY(0x37),
    KEY_LMENU(0x38),
    KEY_RMENU(0x38, 0xE0),
    KEY_SPACE(0x39),
    KEY_CAPITAL(0x3A),
    KEY_F1(0x3B),
    KEY_F2(0x3C),
    KEY_F3(0x3D),
    KEY_F4(0x3E),
    KEY_F5(0x3F),
    KEY_F6(0x40),
    KEY_F7(0x41),
    KEY_F8(0x42),
    KEY_F9(0x43),
    KEY_F10(0x44),
    KEY_NUMLOCK(0x45),
    KEY_SCROLL(0x46),
    KEY_HOME(0x47, 0xE0),
    KEY_UP(0x48, 0xE0),
    KEY_PRIOR(0x49, 0xE0),
    KEY_SUBTRACT(0x4A),
    KEY_LEFT(0x4B, 0xE0),
    KEY_CLEAR(0x4C),
    KEY_RIGHT(0x4D, 0xE0),
    KEY_ADD(0x4E),
    KEY_END(0x4F, 0xE0),
    KEY_DOWN(0x50, 0xE0),
    KEY_NEXT(0x51, 0xE0),
    KEY_INSERT(0x52, 0xE0),
    KEY_DELETE(0x53, 0xE0),
    KEY_SNAPSHOT(0x54),
    KEY_OEM_102(0x56),
    KEY_F11(0x57),
    KEY_F12(0x58),
    KEY_LWIN(0x5B, 0xE0),
    KEY_RWIN(0x5C, 0xE0),
    KEY_APPS(0x5D, 0xE0),
    KEY_PRTSC_SYSRQ(0xE0, 0x2A, 0x37, 0x54),
    KEY_PAUSE_BREAK(0xE1, 0x1D, 0x45, 0xE1, 0x9D, 0xC5, 0x46, 0xC6);

    public static final Map<Integer, Key> RAW_KEYMAPPING;

    static {
        HashMap<Integer, Key> map = new HashMap<>();
        for (Key k : values()) {
            for (int scan : k.scancode())
                map.putIfAbsent(scan, k);
        }
        RAW_KEYMAPPING = Collections.unmodifiableMap(map);
    }

    private final int[] scancode;

    Key(int... scancode) {
        this.scancode = scancode;
    }

    public int[] scancode() {
        return scancode;
    }

}