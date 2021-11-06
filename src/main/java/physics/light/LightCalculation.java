package physics.light;

import ecs.LightBody;
import graphics.HSLColor;
import org.joml.Vector2f;
import org.joml.Vector2i;
import physics.collision.CollisionUtil;
import physics.collision.shape.Circle;
import physics.collision.shape.PrimitiveShape;
import util.Pair;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        //float buffer will contain triangles with colors for a fan
        //[posX,posY,colorR,colorG,colorB,colorA...]
        //elementwise you can build triangles like the following:
        //elements: [1,2,3,4] -> root+1+2, root+2+3, root+3+4, root+4+1
        FloatBuffer floatBuffer = FloatBuffer.allocate(worldObjects.size() * circlePrecision);

        //a circle to model the range of the point light
        Circle circle = new Circle(new Vector2f(0, 0), lightReach);
        circle.setPosition(pointLightPosition);
        //filter all bodies, that could be hit by distance
        List<LightBody> bodies = worldObjects.stream()
                .filter(obj -> CollisionUtil.gjksmCollision(circle, obj.getShape()).collision())
                .sorted((body1, body2) -> Float.compare(body1.getShape().centroid().distanceSquared(pointLightPosition), body2.getShape().centroid().distanceSquared(pointLightPosition)))
                .collect(Collectors.toList());

        //TODO ray casting /ray marching

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
        //vector from the center of the shape to sourcePoint
        Vector2f direction = sourcePoint.sub(shape.centroid(), new Vector2f());
        int vertices = shape.vertices();
        float angle = 1;
        int target = -1;
        Vector2f targetPoint = null;
        //find the point creating the smallest angle to the direction vector to get one tangent point
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
        //use the first tangent point to get the second one by redefining the direction as the vector
        //from the first tangent point to the sourcePoint
        //finding the smallest angle now will result in the second tangent point,
        //since the angle against a convex shape can never accede 180 degree,
        //the used repAngle method will never produce numbers, that start to grow again
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

    /**
     * Cast two rays and see if they intersect.
     * The solution will be calculated based on endless rays with a fixpoint and a ray direction.
     * If a there is intersection point of both ray, they arent parallel and the factors x and y are calculated,
     * so that intersection = pointA+x*rayA = pointB+y*rayB is valid.
     *
     * @param pointA the starting point of ray 1
     * @param rayA   the direction and length of ray 1
     * @param pointB the starting point of ray 2
     * @param rayB   the direction of ray 2
     * @return a pair containing the intersection point first and a vector with factors x and y
     */
    public static Optional<Pair<Vector2f, Vector2f>> rayCastIntersection(Vector2f pointA, Vector2f rayA, Vector2f pointB, Vector2f rayB) { //x,a,y,b
        //solve the linear equation
        Vector2f factors = CollisionUtil.solveSimultaneousEquations(rayA.x, -rayB.x, rayA.y, -rayB.y, pointB.x - pointA.x, pointB.y - pointA.y);
        //calculate the point where the intersection happened and return
        Vector2f dest = rayA.mul(factors.x, new Vector2f());
        return Optional.of(new Pair<>(dest.add(pointA), factors));
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