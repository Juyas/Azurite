package input;

import java.util.HashMap;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 12.07.2021
 * @since 12.07.2021
 */
public class KeyInput {

    private int scancode;
    private String virtual_key;
    private String name;
    private HashMap<Byte, String> resultMap;

    public String getName() {
        return name;
    }

    public HashMap<Byte, String> getResultMap() {
        return resultMap;
    }

    public int getScancode() {
        return scancode;
    }

    public String getVirtual_key() {
        return virtual_key;
    }

    public String process(InputMod... mods) {
        byte mod = 0;
        for (InputMod inputMod : mods) {
            mod = (byte) (mod | inputMod.getMask());
        }
        return resultMap.get(mod);
    }

}