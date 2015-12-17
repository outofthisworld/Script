/*
 * Copyright (c) 2015. Dale Appleby
 */

package GUI;

import bot.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Unknown on 11/12/2015.
 */
public class Window {
    private final JFrame jFrame;
    private final Main script;
    private boolean isScriptStarted = false;

    public Window(Main script, String title, int width, int height) {
        this.jFrame = new JFrame(title);
        this.script = script;
        jFrame.setSize(new Dimension(width, height));
        init();
    }

    public final void show() {
        jFrame.setVisible(true);
        jFrame.setEnabled(true);
    }

    private final void init() {
        jFrame.setAlwaysOnTop(true);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setResizable(false);
        JButton startButton = new JButton("Start script");
        startButton.addActionListener(e -> {
                    isScriptStarted = true;
                    this.jFrame.setVisible(false);
                }
        );
        jFrame.add(startButton).setBounds(30, 30, 30, 30);
    }

    public boolean isScriptStarted() {
        return isScriptStarted;
    }
}
