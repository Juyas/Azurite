package scene;

import graphics.Camera;
import graphics.Texture;
import graphics.renderer.*;
import graphics.renderer.DebugRenderer;
import graphics.renderer.DefaultRenderer;
import graphics.renderer.LightmapRenderer;
import graphics.renderer.Renderer;
import input_old.Keyboard_old;
import org.lwjgl.glfw.GLFW;
import physics.collision.Collider;
import postprocess.ForwardToTexture;
import postprocess.PostProcessStep;
import util.Engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Asher Haun
 * @author Juyas
 * @author VoxelRifts
 *
 * <p>
 * Abstract class encapsulating the game logic, the gameObjects, the renderers, the physics
 * specifications and all the necessary ecs-related logic. Programming/Interacting with this
 * game engine will usually involve the following boilerplate code:
 * <pre>
 *     public class Main extends Scene {
 *         public static void main(String[] args) {
 *             Engine.init(1920, 1080, "Azurite Engine Demo In Comment", 1.0f);
 *             Engine.scenes().switchScene(new Main(), true);
 *             Engine.showWindow();
 *         }
 *
 *         public void awake() {
 *             camera = new Camera();
 *             ...
 *         }
 *
 *         public void update() {
 *             ...
 *         }
 *     }
 * </pre>
 * A simple example of a scene with just a rendered sprite:
 * <pre>
 *     public class Main extends Scene {
 *         GameObject player;
 *         Sprite s;
 *
 *         public static void main(String[] args) {
 *             Engine.init(1920, 1080, "Azurite Engine Demo In Comment", 1.0f);
 *             Engine.scenes().switchScene(new Main(), true);
 *             Engine.showWindow();
 *         }
 *
 *         public void awake() {
 *             camera = new Camera();
 *
 *             player = new GameObject();
 *             s = new Sprite
 *             player.addComponent(new SpriteRenderer(s, new Vector2f(100)));
 *         }
 *
 *         public void update() {
 *             if (Keyboard.getKeyDown(GLFW.GLFW_KEY_SPACE)) {
 *                 player.transform.add(new Vector2f(1, 0));
 *             }
 *         }
 *     }
 * </pre>
 * </p>
 * @see SceneManager
 */
public abstract class Scene {

    private static int sceneCounter = 0;
    private final int sceneId = sceneCounter++;
    private final List<GameObject> gameObjects = new LinkedList<>();

    private final List<Collider> colliders = new LinkedList<>();

    public DefaultRenderer renderer = new DefaultRenderer();
    public LightmapRenderer lightmapRenderer = new LightmapRenderer();
    public DebugRenderer debugRenderer = new DebugRenderer();
    public TextRenderer textRenderer = new TextRenderer();

    protected Camera camera;
    protected ForwardToTexture forwardToScreen;
    private final List<Renderer> rendererRegistry = new LinkedList<>();
    private boolean debugMode = false;
    private boolean active = false;
    private final ArrayList<Text> uiObjects = new ArrayList<>();

    public boolean isActive() {
        return active;
    }

    /**
     * Runs only once on startup, useful for initializing gameObjects or for first time setup.
     */
    public void awake() {
        camera = new Camera();
    }

    /**
     * This method will be called each time this scene becomes active by {@link SceneManager}.
     * Will be called right before the first update.
     * Can be used to prepare the scene to be shown after been shown previously to reset to a certain state.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * This method will be called each time this scene becomes inactive by {@link SceneManager},
     * because of switching to another method or termination of the program.
     * Can be used to preserve the current state of the scene or quickly complete/cancel tasks that were midst execution.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * This method is called every frame, and can be used to update objects.
     */
    public void update() {
        if (Keyboard_old.getKeyDown(GLFW.GLFW_KEY_GRAVE_ACCENT)) {
            debugMode = !debugMode;
        }
    }

    /**
     * Apply post processing to a texture
     *
     * @param texture input texture
     */
    public void postProcess(Texture texture) {
        forwardToScreen.setTexture(texture);
        forwardToScreen.apply();
    }

    /**
     * This method is called at the end of the program
     */
    public void clean() {
        this.renderer.clean();
        this.lightmapRenderer.clean();
        this.debugRenderer.clean();
        this.textRenderer.clean();
        rendererRegistry.forEach(Renderer::clean);
    }

    // The following methods shouldn't be overridden. For this, added final keyword

    public final void startUi() {
        textRenderer.init();
    }

    /**
     * Loops through all gameobjects already in the scene and calls their start methods.
     */
    public final void startGameObjects() {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
            this.lightmapRenderer.add(gameObject);
            this.debugRenderer.add(gameObject);
            rendererRegistry.forEach(r -> r.add(gameObject));
        }
    }

    public List<Collider> getColliders() {
        return colliders;
    }

    public final void registerCollider(GameObject gameObject) {
        colliders.add(gameObject.getComponent(Collider.class));
    }

    public final void unregisterCollider(GameObject gameObject) {
        colliders.remove(gameObject.getComponent(Collider.class));
    }

    /**
     * The sceneId is meant to represent the instance of a scene as an integer
     *
     * @see SceneManager
     */
    public final int sceneId() {
        return sceneId;
    }

    /**
     * @return the List of gameObjects contained in the scene.
     */
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * @param gameObject GameObject to be added.
     *                   Add a new gameObject to the scene and immediately call its start method.
     */
    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (active) {
            gameObject.start();
            addToRenderers(gameObject);
        }
    }

    /**
     * @param gameObject GameObject to be added.
     */
    public void removeGameObjectFromScene(GameObject gameObject) {
        gameObjects.remove(gameObject);
        removeFromRenderers(gameObject);
    }

    /**
     * Register a renderer to this scene
     *
     * @param renderer the renderer to be registered
     */
    public void registerRenderer(Renderer renderer) {
        rendererRegistry.add(renderer);
    }

    /**
     * @return Returns the scene's instance of Camera
     */
    public Camera camera() {
        return this.camera;
    }

    /**
     * Loops through all the gameObjects in the scene and calls their update methods.
     */
    public void updateGameObjects() {
        for (GameObject go : gameObjects) {
            go.update(Engine.deltaTime());
        }
    }

    public void render() {
        rendererRegistry.forEach(Renderer::render);
        lightmapRenderer.render();
        lightmapRenderer.bindLightmap();
        renderer.render();
    }

    public void debugRender() {
        if (debugMode) this.debugRenderer.render();
    }

    /**
     * Initialize all renderers
     */
    public void initRenderers() {
        debugRenderer.init();
        lightmapRenderer.init();
        renderer.init();
        forwardToScreen = new ForwardToTexture(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        forwardToScreen.init();
    }

    public void addUiObject(Text t) {
        uiObjects.add(t);
    }

    public final void textRender() {
        textRenderer.render();
    }

    public void updateUI() {
        for (Text i : uiObjects) {
            i.update();
        }
    }

    /**
     * Add a gameObject to all renderers
     *
     * @param gameObject the gameObject to be added
     */
    public void addToRenderers(GameObject gameObject) {
        this.renderer.add(gameObject);
        this.lightmapRenderer.add(gameObject);
        this.debugRenderer.add(gameObject);
        rendererRegistry.forEach(r -> r.add(gameObject));
    }

    /**
     * Remove a gameObject from all renderers
     *
     * @param gameObject the gameObject to be removed
     */
    private void removeFromRenderers(GameObject gameObject) {
        this.renderer.remove(gameObject);
        this.lightmapRenderer.remove(gameObject);
        this.debugRenderer.remove(gameObject);
        rendererRegistry.forEach(r -> r.remove(gameObject));
    }
}