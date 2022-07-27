package ecs;

import input.keyboard.Key;
import input.keyboard.Keyboard;
import org.joml.Vector2f;
import physics.force.Force;

import static input.keyboard.InputState.HOLD;
import static input.keyboard.InputState.PRESSED;

/**
 * Character controllers built to support the Top down and Side scroller Demo scenes.
 */
public class CharacterController extends Component {

    private final float speedModifier;
    private final Force playerInputForce;

    private CharacterController(float speedModifier, Force playerInputForce) {
        super(ComponentOrder.INPUT);
        this.playerInputForce = playerInputForce;
        this.speedModifier = speedModifier;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public Force getPlayerInputForce() {
        return playerInputForce;
    }

    public static CharacterController standardPlatformer(Dynamics dynamics, float speedModifier) {
        Force f = new Force() {
            private final Vector2f direction = new Vector2f(0, 0);

            @Override
            public String identifier() {
                return "GodlikePlayerInput";
            }

            @Override
            public boolean update(float dt) {
                direction.set(0, 0);
                if (up()) direction.add(0, -speedModifier);
                //nothing on down input
                if (left()) direction.add(-speedModifier, 0);
                if (right()) direction.add(speedModifier, 0);
                return true;
            }

            @Override
            public Vector2f direction() {
                return direction;
            }
        };
        dynamics.applyForce(f);
        return new CharacterController(speedModifier, f);
    }

    public static CharacterController standardTopDown(Dynamics dynamics, float speedModifier) {
        Force f = new Force() {
            private final Vector2f direction = new Vector2f(0, 0);

            @Override
            public String identifier() {
                return "GodlikePlayerInput";
            }

            @Override
            public boolean update(float dt) {
                direction.set(0, 0);
                if (up()) direction.add(0, -speedModifier);
                if (down()) direction.add(0, speedModifier);
                if (left()) direction.add(-speedModifier, 0);
                if (right()) direction.add(speedModifier, 0);
                return true;
            }

            @Override
            public Vector2f direction() {
                return direction;
            }
        };
        dynamics.applyForce(f);
        return new CharacterController(speedModifier, f);
    }

    private static boolean up() {
        return Keyboard.isAny(Key.VK_UP, PRESSED, HOLD) || Keyboard.isAny(Key.VK_W, PRESSED, HOLD);
    }

    private static boolean down() {
        return Keyboard.isAny(Key.VK_DOWN, PRESSED, HOLD) || Keyboard.isAny(Key.VK_S, PRESSED, HOLD);
    }

    private static boolean left() {
        return Keyboard.isAny(Key.VK_LEFT, PRESSED, HOLD) || Keyboard.isAny(Key.VK_A, PRESSED, HOLD);
    }

    private static boolean right() {
        return Keyboard.isAny(Key.VK_RIGHT, PRESSED, HOLD) || Keyboard.isAny(Key.VK_D, PRESSED, HOLD);
    }
}
