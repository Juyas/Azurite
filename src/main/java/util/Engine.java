package util; 

import audio.AudioMaster;
import graphics.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import scene.SceneManager;
import util.safety.Preconditions;

import static org.lwjgl.glfw.GLFW.glfwInit;

/**
 * The Engine class initializes the Window and the game loop.
 * It can also be used to access some globally used classes like the scene manager and Window.
 */
public final class Engine {

    private static final Engine instance = new Engine();

    private final long startMillis;
    private Window window;
    private boolean running;
    private float deltaTime;

    //private to prevent creating new instances
    private Engine() {
        running = true;
        deltaTime = 0;
        startMillis = System.currentTimeMillis();
    }

    /**
     * Get the global unique instance of the Engine object.
     */
    public static Engine getInstance() {
        return instance;
    }

    public static float deltaTime() {
        return getInstance().getDeltaTime();
    }

    public static void updateDeltaTime(float deltaTime) {
        getInstance().deltaTime = deltaTime;
    }

    public static boolean isRunning() {
        return getInstance().running;
    }

    public static Window window() {
        return getInstance().getWindow();
    }

    public static void showWindow() {
        Preconditions.nonNull("window", window()).showWindow();
    }

    public static SceneManager scenes() {
        return window().getSceneManager();
    }

    // Internal method called before any init
    private static void preInit() {

        System.setProperty("java.awt.headless", "true");

        //ensure that the Engine is running on main thread
        Preconditions.ensureMainThread("engine initialization");

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");
      
        AudioMaster.get();
    }

    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowWidth      Width of the window to be created
     * @param windowHeight     Height of the window to be created
     * @param windowTitle      Title of the window to be created
     * @param minSceneLighting float from 0-1 indicating the minimum scene light level
     */
    public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting) {
        preInit();
        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, true);
    }

    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowWidth  Width of the window to be created
     * @param windowHeight Height of the window to be created
     * @param windowTitle  Title of the window to be created
     */
    public static void init(int windowWidth, int windowHeight, String windowTitle) {
        preInit();
        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, false);
    }

    /**
     * Start the engine, and initialize GLFW. This will create a fullscreen window.
     *
     * @param windowTitle      Title of the window to be created
     * @param minSceneLighting float from 0-1 indicating the minimum scene light level
     */
    public static void init(String windowTitle, float minSceneLighting) {
        preInit();
        getInstance().window = new Window(windowTitle, minSceneLighting, false);
    }

    public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting, boolean recalculateProjectionOnResize) {
        preInit();
        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, recalculateProjectionOnResize);
    }

    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowTitle Title of the window to be created
     */
    public static void init(String windowTitle) {
        preInit();
        getInstance().window = new Window(windowTitle);
    }

    /**
     * @return Returns the number of milliseconds since the engine started. (since the first call)
     */
    public static double millisRunning() {
        return System.currentTimeMillis() - getInstance().startMillis;
    }

    public Window getWindow() {
        return window;
    }

    public float getDeltaTime() {
        return deltaTime;
    }
}
