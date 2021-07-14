package physics.light;

import physics.collision.shape.PrimitiveShape;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 14.07.2021
 * @since 14.07.2021
 */
public interface LightBody {

    /**
     * The shape of the lightbody.
     *
     * @return the visible shape, that can interact with light
     */
    PrimitiveShape getVisibleShape();



}