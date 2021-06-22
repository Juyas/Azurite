package physics;

import physics.collision.GJKSMShape;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 21.06.2021
 * @since 21.06.2021
 */
public interface Collider {

    /**
     * The shape of the collider.
     *
     * @return the shape of the collider
     */
    GJKSMShape getCollisionShape();

    /**
     * Determines whether a collider intersects with another collider IGNORING the collision layers.
     *
     * @param collider the other collider
     * @return true if and only if the two objects do collide
     */
    boolean doesCollideWith(Collider collider);

    /**
     * Determines whether a collider could potentially intersect with another collider ONLY by their collision layers.
     *
     * @param collider the other collider
     * @return true if and only if both objects could potentially collide
     */
    boolean canCollideWith(Collider collider);

    /**
     * A short containing all enabled layers encoded as bit mask.
     * Should be usable to compare different masks with each other to check for intersection.
     *
     * @return a short with all layer encoded
     */
    short layers();

    /**
     * A short containing all enabled masks encoded as bit mask.
     * Should be usable to compare different masks with each other to check for intersection.
     *
     * @return a short with all masks encoded
     */
    short mask();

    /**
     * Determines whether a collision mask for a specified layer is enabled.
     *
     * @param layer the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @return true if and only if the mask for the specified layer is enabled
     */
    boolean hasMask(int layer);

    /**
     * Determines whether the collider exists in a specified layer.
     *
     * @param layer the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @return true if and only if the collider exists in the specified layer
     */
    boolean isOnLayer(int layer);

    /**
     * Change the colliders presence in a specified layer.
     * Making a collider present in a specified layer will it enable to collide
     * with all collider including the specified layer in their collision mask.
     *
     * @param layer  the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @param active whether the collider should be present (true) or not (false)
     */
    void setLayer(int layer, boolean active);

    /**
     * Changes the collision mask entry for a specified layer.
     * Enabling the mask for layer n will allow this collider to collide with any other collider set in the target layer n vise versa.
     *
     * @param layer  the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @param active whether the mask should enabled (true) or disabled (false)
     */
    void setMask(int layer, boolean active);

}