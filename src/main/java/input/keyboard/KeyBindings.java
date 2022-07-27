package input.keyboard;

import io.bin.BinaryIO;
import io.xml.XML;
import io.xml.XMLElement;
import io.xml.XMLSyntaxException;
import util.Log;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
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

    private final HashMap<Integer, KeyInput> mapping;

    public KeyBindings() {
        this(new HashMap<>());
    }

    public KeyBindings(HashMap<Integer, KeyInput> mapping) {
        this.mapping = mapping;
    }

    public static KeyBindings parse(Locale locale, String path) {
        KeyBindings bindings = new KeyBindings();
        bindings.locale = locale;
        XMLElement xmlElement;
        try {
            File file = new File(path);
            xmlElement = XML.parse(BinaryIO.readData(file).array());
            if (!xmlElement.getTag().equals("KeyboardLayout")) {
                throw badSyntax("Keybinding xml has to have a KeyboardLayout tag as root");
            }
            //using the name without extension as the layout descriptor
            bindings.layoutDescriptor = file.getName().substring(0, file.getName().length() - 4);
            xmlElement.getAttributes().forEach((key, value) -> {
                switch (key) {
                    case "RightAltIsAltGr":
                        bindings.rightAltIsaltGr = Boolean.getBoolean(value);
                        break;
                    case "ShiftCancelsCapsLock":
                        bindings.shiftCancelsCapsLock = Boolean.getBoolean(value);
                        break;
                    case "ChangesDirectionality":
                        bindings.changesDirectionality = Boolean.getBoolean(value);
                        break;
                }
            });
            if (xmlElement.getChildren().size() != 1 || !xmlElement.getChildren().get(0).getTag().equals("PhysicalKeys")) {
                throw badSyntax("Keybinding xml has to have only a PhysicalKeys tag at second level");
            }
            //make PhysicalKeys the next parent containing all
            xmlElement = xmlElement.getChildren().get(0);
            //load each element
            for (XMLElement element : xmlElement.getChildren()) {
                KeyInput keyInput = readPK(element);
                if (keyInput != null)
                    bindings.mapping.put(keyInput.getScancode(), keyInput);
            }
        } catch (IOException e) {
            Log.fatal("Loading keybindings failed: \"" + path + "\"");
        }
        return null;
    }

    private static KeyInput readPK(XMLElement pkElement) {
        if (!pkElement.getTag().equals("PK")) return null;
        if (pkElement.getAttributes().size() < 2) {
            throw badSyntax("each PK element is supposed to have a least two attributes: VK and SC");
        }
        int sc = -1;
        Key vk = null;
        String name = null;
        for (Map.Entry<String, String> x : pkElement.getAttributes().entrySet()) {
            switch (x.getKey()) {
                case "VK":
                    vk = Key.valueOf(x.getValue());
                    break;
                case "SC":
                    sc = Integer.parseInt(x.getValue(), 16);
                    break;
                case "Name":
                    name = x.getValue();
                    break;
            }
        }
        List<KeyInputResult> resultMap = new ArrayList<>();
        //empty body allowed for direct bindings
        if (pkElement.getChildren() != null) {
            for (XMLElement result : pkElement.getChildren()) {
                if (!result.getTag().equals("Result"))
                    throw badSyntax("PK elements should only contain Result nodes");
                //no deadKeyTable
                if (result.getChildren() == null) {
                    String txt = null;
                    Key[] with = null;
                    Key reroute = null;
                    int codepoints = -1;
                    for (Map.Entry<String, String> x : result.getAttributes().entrySet()) {
                        switch (x.getKey()) {
                            case "Text":
                                txt = x.getValue();
                                break;
                            case "With":
                                String[] keys = x.getValue().split(" ");
                                with = new Key[keys.length];
                                for (int i = 0; i < with.length; i++)
                                    with[i] = Key.valueOf(keys[i]);
                                break;
                            case "VK":
                                reroute = Key.valueOf(x.getValue());
                                break;
                            case "TextCodepoints":
                                codepoints = Integer.parseInt(x.getValue(), 16);
                        }
                    }
                    KeyInputResult keyInputResult = new KeyInputResult(txt, codepoints, with, vk, null);
                    resultMap.add(keyInputResult);
                } else { //deadkeytable
                    //TODO
                }
            }
        }
        return new KeyInput(sc, vk, name, resultMap);
    }

    private static XMLSyntaxException badSyntax(String context) {
        return new XMLSyntaxException("xml for keybinding has a bad syntax: " + context);
    }

}