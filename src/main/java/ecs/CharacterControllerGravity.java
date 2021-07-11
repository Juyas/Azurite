package ecs;

import input_old.Keyboard_old;
import org.joml.Vector2f;
import physics.AABB;
import util.Engine;

public class CharacterControllerGravity extends CharacterController {

    private float gravity = 9f;
    Vector2f velocity = new Vector2f(0, 100);
    private boolean grounded = false;

    AABB aabb;

    @Override
    public void start() {
        super.start();
        aabb = gameObject.getComponent(AABB.class);
    }

    @Override
    public void update (float dt) {
        super.update(dt);

        if (aabb != null) {
            if (aabb.isCollidingY()) {
                grounded = true;
            } else {
                grounded = false;
            }
        } else {
            aabb = gameObject.getComponent(AABB.class);
        }
    }

    @Override
    protected void moveY () {
        lastPosition.y = gameObject.getTransform().getY();
        if ((Keyboard_old.getKey(Keyboard_old.W_KEY)) && grounded) {
            gameObject.getTransform().addY(-1);
            velocity.y = -560;
            grounded = false;
        }
        if (!grounded) {
            velocity.y += gravity;
        }
        gameObject.getTransform().addY(velocity.y * Engine.deltaTime());
    }
}
