package input;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 12.07.2021
 * @since 21.04.2021
 */
public interface Option {

    byte getMask();

    default boolean match(byte options) {
        return getMask() == (options & getMask());
    }

}