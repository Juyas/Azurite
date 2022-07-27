package input.keyboard;

import java.util.Map;

/**
 * @author Juyas
 * @version 27.07.2022
 * @since 27.07.2022
 */
public class DeadKeyTable {

    private String name;
    private char accent;
    private Map<Character, Character> mapping;

    public DeadKeyTable(String name, char accent, Map<Character, Character> mapping) {
        this.name = name;
        this.accent = accent;
        this.mapping = mapping;
    }

    public String getName() {
        return name;
    }

    public char getAccent() {
        return accent;
    }

    public Map<Character, Character> getMapping() {
        return mapping;
    }
}