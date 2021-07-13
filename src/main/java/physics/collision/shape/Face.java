package physics.collision.shape;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 13.07.2021
 * @since 27.06.2021
 */
public class Face {

    private final Vector2f relativeFixPoint;
    private final PrimitiveShape parent;
    private final Vector2f relativeFace;
    private final Vector2f innerNormal;
    private final Vector2f outerNormal;

    public Face(PrimitiveShape parent, Vector2f relativePoint, Vector2f relativeFace) {
        this.parent = parent;
        this.relativeFixPoint = relativePoint;
        this.relativeFace = relativeFace;
        Vector2f relativeCentroid = parent.relativeCentroid;
        //normal vectors
        this.innerNormal = new Vector2f(-relativeFace.y, relativeFace.x);
        this.outerNormal = new Vector2f(relativeFace.y, -relativeFace.x);
    }

    public Vector2f getInnerNormal() {
        return innerNormal;
    }

    public Vector2f getOuterNormal() {
        return outerNormal;
    }

    public Vector2f getRelativeFixPoint() {
        return relativeFixPoint;
    }

    public Vector2f getRelativeFace() {
        return relativeFace;
    }

    public Vector2f getAbsoluteFixPoint() {
        return relativeFixPoint.add(parent.position(), new Vector2f());
    }

}