package input.keyboard;

import java.util.Map;

/**
 * @version 27.07.2022
 * @since 27.07.2022
 */
public class DeadKeyTable {

    private final String name;
    private final String accent;
    private final Map<String, String> mapping;

    public DeadKeyTable(String name, String accent, Map<String, String> mapping) {
        this.name = name;
        this.accent = accent;
        this.mapping = mapping;
    }

    public String getName() {
        return name;
    }

    public String getAccent() {
        return accent;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }
}