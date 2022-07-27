package input.keyboard;

/**
 * @version 12.07.2021
 * @see InputState
 * @see InputMod
 * @since 21.04.2021
 */
public interface Option {

    byte getMask();

    default boolean match(byte options) {
        return getMask() == (options & getMask());
    }

}