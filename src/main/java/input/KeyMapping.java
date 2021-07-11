package input;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 11.07.2021
 * @since 11.07.2021
 */
public class KeyMapping<Input, Output> {

    public static final char PARSING_DELIMITER = ',';
    private final HashMap<Input, Output> mapping;

    public KeyMapping() {
        this(new HashMap<>());
    }

    public KeyMapping(HashMap<Input, Output> mapping) {
        this.mapping = mapping;
    }

    /**
     * Format: in1=out1,in2=out2
     * First character is delimiter.
     */
    public static KeyMapping<String, String> parse(String mapping) {
        String[] splits = mapping.split(Pattern.quote(String.valueOf(PARSING_DELIMITER)));
        HashMap<String, String> map = new HashMap<>();
        for (int i = 1; i < splits.length; i += 2) {
            map.put(splits[i - 1], splits[i]);
        }
        return new KeyMapping<>(map);
    }

    public boolean learn(Input input, Output output, boolean ifUnknown) {
        if (!ifUnknown || !mapping.containsKey(input))
            mapping.put(input, output);
        else return false;
        return true;
    }

    public boolean canMap(Input input) {
        return mapping.containsKey(input);
    }

    public Output map(Input input) {
        return mapping.get(input);
    }

    public <I, O> KeyMapping<I, O> convert(Function<Input, I> convertInput, Function<Output, O> convertOutput) {
        HashMap<I, O> map = new HashMap<>();
        for (Map.Entry<Input, Output> entry : mapping.entrySet()) {
            map.put(convertInput.apply(entry.getKey()), convertOutput.apply(entry.getValue()));
        }
        return new KeyMapping<>(map);
    }

}