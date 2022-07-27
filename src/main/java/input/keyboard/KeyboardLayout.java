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
 * For detailed information, view <a href="https://kbdlayout.info">kbdlayout.info</a>
 *
 * @version 11.07.2021
 * @since 11.07.2021
 */
public class KeyboardLayout {

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

    public KeyboardLayout() {
        this(new HashMap<>());
    }

    public KeyboardLayout(HashMap<Integer, KeyInput> mapping) {
        this.mapping = mapping;
    }

    public boolean isChangesDirectionality() {
        return changesDirectionality;
    }

    public boolean isRightAltIsaltGr() {
        return rightAltIsaltGr;
    }

    public boolean isShiftCancelsCapsLock() {
        return shiftCancelsCapsLock;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLayoutDescriptor() {
        return layoutDescriptor;
    }

    public HashMap<Integer, KeyInput> getMapping() {
        return mapping;
    }

    //------------------------------ PARSING --------------------------------------

    public static KeyboardLayout parse(Locale locale, String path) {
        KeyboardLayout bindings = new KeyboardLayout();
        bindings.locale = locale;
        XMLElement xmlElement;
        try {
            File file = new File(path);
            xmlElement = XML.parse(BinaryIO.readData(file).array());
            if (!xmlElement.getTag().equals("KeyboardLayout")) {
                throw badSyntax("Keybinding xml has to have a KeyboardLayout tag as root -\n" + xmlElement);
            }
            //using the name without extension as the layout descriptor
            bindings.layoutDescriptor = file.getName().substring(0, file.getName().length() - 4);
            bindings.rightAltIsaltGr = Boolean.parseBoolean(xmlElement.getAttributes().getOrDefault("RightAltIsAltGr", "true"));
            bindings.shiftCancelsCapsLock = Boolean.parseBoolean(xmlElement.getAttributes().getOrDefault("ShiftCancelsCapsLock", "false"));
            bindings.changesDirectionality = Boolean.parseBoolean(xmlElement.getAttributes().getOrDefault("ChangesDirectionality", "false"));
            if (xmlElement.getChildren().size() != 1 || !xmlElement.getChildren().get(0).getTag().equals("PhysicalKeys")) {
                throw badSyntax("Keybinding xml has to have only a PhysicalKeys tag at second level -\n" + xmlElement);
            }
            //make PhysicalKeys the next parent containing all
            xmlElement = xmlElement.getChildren().get(0);
            //load each element
            for (XMLElement element : xmlElement.getChildren()) {
                //read PK element
                KeyInput keyInput = readPK(element);
                if (keyInput != null) bindings.mapping.put(keyInput.getScancode(), keyInput);
            }
            return bindings;
        } catch (IOException e) {
            Log.fatal("Loading keybindings failed: \"" + path + "\"");
        }
        return null;
    }

    private static KeyInput readPK(XMLElement pkElement) {
        if (!pkElement.getTag().equals("PK")) return null;
        if (pkElement.getAttributes().size() < 2) {
            throw badSyntax("each PK element is supposed to have a least two attributes: VK and SC -\n" + pkElement);
        }
        int sc = Integer.parseInt(pkElement.getAttributes().getOrDefault("SC", "-1"), 16);
        Key vk = Key.valueOf(pkElement.getAttributes().getOrDefault("VK", null));
        String name = pkElement.getAttributes().getOrDefault("Name", null);
        List<KeyInputResult> resultMap = new ArrayList<>();
        //empty body allowed for direct bindings
        if (pkElement.getChildren() != null) {
            for (XMLElement result : pkElement.getChildren()) {
                if (!result.getTag().equals("Result"))
                    throw badSyntax("PK elements should only contain Result nodes -\n" + result);
                //no deadKeyTable
                if (result.getChildren() == null || result.getChildren().size() == 0) {
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
                } else {
                    //deadKeyTable
                    XMLElement deadKeyTable = result.getChildren().get(0);
                    if (!deadKeyTable.getTag().equals("DeadKeyTable") || result.getChildren().size() > 1)
                        throw badSyntax("Result tag should only contain an optional DeadKeyTable -\n" + deadKeyTable);
                    String dktName = deadKeyTable.getAttributes().getOrDefault("Name", null);
                    String accent = deadKeyTable.getAttributes().getOrDefault("Accent", null);
                    Map<String, String> mapping = new HashMap<>();
                    //resolving deadKeyTable
                    for (XMLElement res : deadKeyTable.getChildren()) {
                        if (!res.getTag().equals("Result") || (res.getChildren() != null && res.getChildren().size() > 0) || res.getAttributes().size() < 2)
                            throw badSyntax("a deadKeyTable result subtag may not have a children -\n" + res);
                        String text, with;
                        text = res.getAttributes().get("Text");
                        with = res.getAttributes().get("With");
                        mapping.put(with, text);
                    }
                    Key[] with = null;
                    String w = result.getAttributes().getOrDefault("With", null);
                    if (w != null) {
                        String[] keys = w.split(" ");
                        with = new Key[keys.length];
                        for (int i = 0; i < with.length; i++)
                            with[i] = Key.valueOf(keys[i]);
                    }
                    DeadKeyTable dkt = new DeadKeyTable(dktName, accent, mapping);
                    KeyInputResult keyInputResult = new KeyInputResult(null, -1, with, null, dkt);
                    resultMap.add(keyInputResult);
                }
            }
        }
        return new KeyInput(sc, vk, name, resultMap);
    }

    private static XMLSyntaxException badSyntax(String context) {
        return new XMLSyntaxException("xml for keybinding has a bad syntax: " + context);
    }

}