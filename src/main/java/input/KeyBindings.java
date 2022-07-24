package input;

import io.FileFormats;
import io.bin.BinaryIO;
import io.xml.XMLElement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 11.07.2021
 * @since 11.07.2021
 */
public class KeyBindings {

    /**
     * The locale that this binding might be used in.
     */
    private Locale locale;
    /**
     * A name or any id describing the layout with a unique identifier
     */
    private String layoutDescriptor;
    /**
     * Whether the right ALT key is considered a ALT GR key, which means (CTRL+ALT)
     */
    private boolean rightAltIsaltGr;
    /**
     * The layout turns off CapsLock when the SHIFT key is depressed.
     */
    private boolean shiftCancelsCapsLock;
    /**
     * The layout inserts Left-to-Right Marker (LRM) on LSHIFT+BACKSPACE and Right-to-Left Marker (RLM) on RSHIFT+BACKSPACE.
     * Furthermore, LSHIFT+CTRL and RSHIFT+CTRL change the directionality of a paragraph, or a text box to LTR and RTL respectively.
     */
    private boolean changesDirectionality;

    private final HashMap<Key, KeyInput> mapping;

    public KeyBindings() {
        this(new HashMap<>());
    }

    public KeyBindings(HashMap<Key, KeyInput> mapping) {
        this.mapping = mapping;
    }

    public static KeyBindings parse(String path) {
        XMLElement xmlElement;
        try {
            xmlElement = FileFormats.XML.parse(BinaryIO.readData(new File(path)).array());
            System.out.println(xmlElement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean learn(Key input, KeyInput output, boolean ifUnknown) {
        if (!ifUnknown || !mapping.containsKey(input))
            mapping.put(input, output);
        else return false;
        return true;
    }

    public boolean canMap(Key input) {
        return mapping.containsKey(input);
    }

    public KeyInput map(Key input) {
        return mapping.get(input);
    }

}