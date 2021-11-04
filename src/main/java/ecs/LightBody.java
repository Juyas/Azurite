package ecs;

import graphics.Color;
import org.joml.Vector3f;
import physics.LocationSensitive;
import physics.collision.shape.PrimitiveShape;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 03.11.2021
 * @since 03.11.2021
 */
public class LightBody extends Component implements LocationSensitive {

    /**
     * Describes the form of the object that affects lighting in some way
     */
    private PrimitiveShape shape;

    /**
     * how opaque is the object - 1 means opaque, 0 means transparent
     * the zweo value means that this object does not affect lighting at all
     */
    private float opacity = 1;

    /**
     * the color of the object. does affect light that passes through the object.
     * <code>null</code> means no color change - fully opaque objects dont change colors as well
     */
    private Color transparentColor;

    public PrimitiveShape getShape() {
        return shape;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setTransparentColor(Color transparentColor) {
        this.transparentColor = transparentColor;
    }

    public Color getTransparentColor() {
        return transparentColor;
    }

    @Override
    public void update(Vector3f changedLocationData) {
        shape.setPosition(this.gameObject.getReadOnlyPosition());
    }

}