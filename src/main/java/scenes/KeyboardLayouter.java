package scenes;

import graphics.Camera;
import graphics.Color;
import scene.Scene;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 12.07.2021
 * @since 12.07.2021
 */
public class KeyboardLayouter extends Scene {

    public static void main(String[] args) {
        Engine.init(500, 300, "Azurite Engine Demo KeyboardLayouter", 1.0f);
        Engine.scenes().switchScene(new KeyboardLayouter(), true);
        Engine.showWindow();
    }

    @Override
    public void awake() {
        camera = new Camera();
        setDefaultBackground(new Color(3, 1, 4));
    }

    @Override
    public void update() {
        super.update();

    }

}