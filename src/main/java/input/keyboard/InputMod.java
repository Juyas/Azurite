package input.keyboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @version 12.07.2021
 * @since 21.04.2021
 */
public enum InputMod implements Option
{

    CONTROL( GLFW_MOD_CONTROL, (byte) 8 ),
    SHIFT( GLFW_MOD_SHIFT, (byte) 16 ),
    ALT( GLFW_MOD_ALT, (byte) 32 ),
    COMMAND( GLFW_MOD_SUPER, (byte) 64 ),
    CAPITAL( GLFW_MOD_CAPS_LOCK, (byte) 128 );

    private static final HashMap<Integer, InputMod> glfwCodes;

    static
    {
        glfwCodes = new HashMap<>();
        Arrays.stream( values() ).forEach( key -> glfwCodes.put( key.glfwMod, key ) );
    }

    public static Set<InputMod> getMods( int mod )
    {
        return Arrays.stream( values() )
                .filter( keymod -> ( keymod.glfwMod & mod ) == keymod.glfwMod )
                .collect( Collectors.toSet() );
    }

    public static byte option( int mod )
    {
        byte option = 0;
        for ( InputMod inputMod : getMods( mod ) )
        {
            option = (byte) ( option | inputMod.getMask() );
        }
        return option;
    }

    private final int glfwMod;
    private final byte mask;

    InputMod( int mod, byte option )
    {
        this.glfwMod = mod;
        this.mask = option;
    }

    @Override
    public byte getMask() {
        return mask;
    }

}