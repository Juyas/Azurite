package ecs;

import input_old.Keyboard_old;
import org.joml.Vector2f;
import physics.AABB;
import util.Engine;

public class CharacterController extends Component {

	Vector2f position = new Vector2f(0, 0);
	Vector2f speed = new Vector2f(300, 300);

	float gravity = 9;
	private final boolean grounded = false;
	protected Vector2f lastPosition;

	float sprintSpeed = 0;

	protected AABB collision;
	public boolean AABB_enabled = false;

	@Override
	public void start() {
		lastPosition = new Vector2f();
		position = gameObject.getTransform().getPosition();
		super.start();
	}

	@Override
	public void update(float dt) {
		moveX();
		if (collision != null) collision.collideX();

		moveY();
		if (collision != null) collision.collideY();
	}

	public void enableAABB() {
		AABB_enabled = true;
		collision = gameObject.getComponent(AABB.class);
	}

	protected void moveX() {
		// X
		gameObject.setTransformX(position.x);
		if (Keyboard_old.getKey(Keyboard_old.A_KEY) || Keyboard_old.getKey(Keyboard_old.LEFT_ARROW)) {
			position.x += (-speed.x + sprintSpeed) * Engine.deltaTime();
		}
		if (Keyboard_old.getKey(Keyboard_old.D_KEY) || Keyboard_old.getKey(Keyboard_old.RIGHT_ARROW)) {
			position.x += (speed.x + sprintSpeed) * Engine.deltaTime();
		}
	}

	protected void moveY() {
		// Y
		gameObject.setTransformY(position.y);

		if (Keyboard_old.getKey(Keyboard_old.W_KEY) || Keyboard_old.getKey(Keyboard_old.UP_ARROW)) {
			position.y += (-speed.y + sprintSpeed) * Engine.deltaTime();
		}
		if (Keyboard_old.getKey(Keyboard_old.S_KEY) || Keyboard_old.getKey(Keyboard_old.DOWN_ARROW)) {
			position.y += (speed.y + sprintSpeed) * Engine.deltaTime();
		}
	}

}
