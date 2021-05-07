package com.github.LilZcrazyG;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        Dimension size = Toolkit. getDefaultToolkit(). getScreenSize();
        GameEngine.WindowEngine.createWindow( "main", new GameEngine.WindowEngine.Window( (int) size.getWidth(), (int) size.getHeight(), "Game Window" ) );
        GameEngine.GraphicsEngine.initialize( GameEngine.WindowEngine.getWindow( "main" ) );
        GameOfLife.start();
        GameEngine.run();

    }

}
