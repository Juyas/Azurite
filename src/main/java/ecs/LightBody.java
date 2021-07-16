package ecs;

import org.joml.Vector2f;
import physics.Transform;
import physics.TransformSensitive;
import physics.collision.shape.PrimitiveShape;
import physics.light.LightAffectingBody;
import util.Pair;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class LightBody extends Component implements LightAffectingBody, TransformSensitive {

    private PrimitiveShape visibleShape;

    @Override
    public PrimitiveShape getVisibleShape() {
        return visibleShape;
    }

    @Override
    public Pair<Vector2f, Vector2f> outerEdges(Vector2f lightsource) {
        return null;
    }

    @Override
    public void update(Transform changedTransform) {
        visibleShape.setPosition(changedTransform.getPosition());
    }

}