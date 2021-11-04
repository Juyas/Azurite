package physics.light;

import ecs.LightBody;
import graphics.HSLColor;
import org.joml.Vector2f;
import org.joml.Vector2i;
import physics.collision.shape.PrimitiveShape;

import java.nio.FloatBuffer;
import java.util.List;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 03.11.2021
 * @since 03.11.2021
 */
public class LightCalculation {

    public static FloatBuffer calcPointLight(List<LightBody> worldObjects, Vector2f pointLightPosition, HSLColor pointLightColor, int circlePrecision, float lightReach) {
        //educated guess, to potentially reduce regenerating/resizing the buffer
        // -> amount of all objects estimated with 50 per object for circles times the precision modifier
        FloatBuffer floatBuffer = FloatBuffer.allocate(worldObjects.size() * circlePrecision);

        //TODO

        return floatBuffer;
    }

    /**
     * Calculates two points that create a lines touching the shape there starting by a fix sourcePoint.
     * Those two lines are called tangents therefore those two points are referred as tangent points to a specific sourcePoint.
     * It is used for point light calculations primarily.
     *
     * @param shape       the shape to tangent
     * @param sourcePoint the point to start the line
     * @return a Vector2i containing the tangent points as their indices in {@link PrimitiveShape#getAbsolutePoints()}
     */
    public static Vector2i getTangentPoints(PrimitiveShape shape, Vector2f sourcePoint) {
        Vector2f direction = sourcePoint.sub(shape.centroid(), new Vector2f());
        int vertices = shape.vertices();
        float angle = 1;
        int target = -1;
        Vector2f targetPoint = null;
        for (int i = 0; i < vertices; i++) {
            Vector2f pointX = shape.getAbsolutePoints()[i];
            float ang = repAngle(sourcePoint.sub(pointX, new Vector2f()), direction);
            if (ang < angle) {
                angle = ang;
                target = i;
                targetPoint = pointX;
            }
        }
        angle = 1;
        int secondTarget = -1;
        Vector2f targetDirection = sourcePoint.sub(targetPoint, new Vector2f());
        for (int i = 0; i < vertices; i++) {
            Vector2f pointX = shape.getAbsolutePoints()[i];
            float ang = repAngle(sourcePoint.sub(pointX, new Vector2f()), targetDirection);
            if (ang < angle) {
                angle = ang;
                secondTarget = i;
            }
        }

        return new Vector2i(target, secondTarget);
    }

    //helper method to calculate an angle relation without calculating the angle exactly
    //used for comparison reasons only, a precise angle can only be guessed by this method.
    //this method returns 1 if the vectors are collinear and -1 if they point in opposite directions
    private static float repAngle(Vector2f a, Vector2f b) {
        float dot = a.dot(b);
        float div = a.lengthSquared() * b.lengthSquared();
        return dot * Math.abs(dot) / div;
    }

}