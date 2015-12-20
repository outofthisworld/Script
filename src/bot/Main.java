/*
 * Copyright (c) 2015. Dale Appleby
 */

package bot;

import GUI.Window;
import config.ScriptConstants;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.ScriptManifest;
import services.DelayedExecutionService;
import tasks.Task;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;

/**
 * Created by Unknown on 11/12/2015.
 */


@ScriptManifest(name = "BlackJemCrabs", info = "A rock crab killer", version = 1.0, author = "BlackJem", logo = "")
public class Main extends org.osbot.rs07.script.Script implements Annotation {
    static final int UNWALKABLE = 256 | 0x200000 | 0x40000;
    private static Image backgroundImage = null;
    private final DelayedExecutionService delayedExecutionService = new DelayedExecutionService();
    private final Window window = new Window(
            this,
            ScriptConstants.SCRIPT_NAME,
            ScriptConstants.GUI_WINDOW_WIDTH,
            ScriptConstants.GUI_WINDOW_HEIGHT
    );
    private final Task[] osBotTasks = {
    };
    static {
        try {
            InputStream inputStream = Main.class.getResourceAsStream("Background.png");
            if (inputStream != null)
                backgroundImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Main() {
        super();
    }

    //Local debugging
    public static void main(String[] args) {
        try {
            new Main().onStart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onExit() throws InterruptedException {
        super.onExit();
        delayedExecutionService.stopServiceImmediately();
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        super.onMessage(message);
    }

    @Override
    public void onStart() throws InterruptedException {
        delayedExecutionService.startService();
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        if (backgroundImage != null)
            graphics.drawImage(backgroundImage, 0, 100, 200, 200, null);
    }

    @Override
    public void onResponseCode(int responseCode) throws InterruptedException {
        super.onResponseCode(responseCode);
    }

    @Override
    public void onConfig(int config1, int config2) throws InterruptedException {
        super.onConfig(config1, config2);
    }

    @Override
    public void onPlayAudio(int audio) {
        super.onPlayAudio(audio);
    }

    @Override
    public int onLoop() throws InterruptedException {


        return random(200, 300);
    }

    public void sleep(int int1, int in2) {
        try {
            MethodProvider.sleep(MethodProvider.random(1500, 2200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }


}
