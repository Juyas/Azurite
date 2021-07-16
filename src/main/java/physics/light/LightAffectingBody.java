package physics.light;

import org.joml.Vector2f;
import physics.collision.shape.PrimitiveShape;
import util.Pair;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 14.07.2021
 * @since 14.07.2021
 */
public interface LightAffectingBody {

    /**
     * The shape of the lightbody.
     *
     * @return the visible shape, that can interact with light
     */
    PrimitiveShape getVisibleShape();

    Pair<Vector2f, Vector2f> outerEdges(Vector2f lightsource);

}