package com.github.LilZcrazyG;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

public class GameOfLife {

    // private variables
    private static Grid grid = new Grid( 0, 0, 100, 100, 10 );
    private static boolean playing = false;

    public static void start() {
        GameEngine.GameStateManager.createState( "Game State", new GameEngine.GameStateManager.GameState() {
            @Override
            public void tick() {
                GameOfLife.tick();
            }

            @Override
            public void render() {
                GameEngine.GraphicsEngine.clearScreen();
                GameEngine.GraphicsEngine.setColor( Color.BLACK );
                AffineTransform t = GameEngine.GraphicsEngine.getGraphics().getTransform();
                GameEngine.GraphicsEngine.rectangleFilled((int) -t.getTranslateX(), (int) -t.getTranslateY(), GameEngine.WindowEngine.getWindow( "main" ).getWidth(), GameEngine.WindowEngine.getWindow( "main" ).getHeight() );
                GameOfLife.render();
                GameEngine.GraphicsEngine.show();
            }
        });
        GameEngine.GameStateManager.setCurrentState( "Game State" );
        GameEngine.InputManager.createKeyListener( GameEngine.WindowEngine.getWindow( "main" ), new GameEngine.InputManager.KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if ( e.getKeyChar() == 'p' || e.getKeyChar() == 'P' ) {
                    playing = !playing;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                GameEngine.InputManager.setLastKeyPressed( e.getKeyCode() );
                GameEngine.InputManager.setKey( e.getKeyCode(), true );
            }

            @Override
            public void keyReleased(KeyEvent e) {
                GameEngine.InputManager.setLastKeyReleased( e.getKeyCode() );
                GameEngine.InputManager.setKey( e.getKeyCode(), false );
            }
        });
        GameEngine.InputManager.createMouseListener( GameEngine.WindowEngine.getWindow( "main" ), new GameEngine.InputManager.MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    AffineTransform t = GameEngine.GraphicsEngine.getGraphics().getTransform();
                    Grid.Cell c = Grid.getCell( (e.getX()-(int) t.getTranslateX())/grid.getCellSize(), (e.getY()-(int) t.getTranslateY())/grid.getCellSize() );
                    c.setAlive( !c.getAlive() );
                    Grid.getCell( (e.getX()-(int) t.getTranslateX())/grid.getCellSize(), (e.getY()-(int) t.getTranslateY())/grid.getCellSize() ).setAlive( c.getAlive() );
                } catch (Exception ex) {
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

            }
        });
    }

    private static void tick() {
        if ( GameEngine.InputManager.getKey( KeyEvent.VK_UP ) || playing ) {
            grid.tick();
        }
    }

    private static void render() {
        int shiftBy = 4;
        if ( GameEngine.InputManager.getKey( KeyEvent.VK_SHIFT ) ) {
            shiftBy = 8;
        }
        if ( GameEngine.InputManager.getKey( KeyEvent.VK_W ) || GameEngine.InputManager.getKey( 'W' ) ) {
            GameEngine.GraphicsEngine.translate( 0, shiftBy );
        }
        if ( GameEngine.InputManager.getKey( 's' ) || GameEngine.InputManager.getKey( 'S' ) ) {
            GameEngine.GraphicsEngine.translate( 0, -shiftBy );
        }
        if ( GameEngine.InputManager.getKey( 'a' ) || GameEngine.InputManager.getKey( 'A' ) ) {
            GameEngine.GraphicsEngine.translate( shiftBy, 0 );
        }
        if ( GameEngine.InputManager.getKey( 'd' ) || GameEngine.InputManager.getKey( 'D' ) ) {
            GameEngine.GraphicsEngine.translate( -shiftBy, 0 );
        }
        if ( GameEngine.InputManager.getKey( KeyEvent.VK_SPACE ) ) {
            grid.setVisible( true );
        } else if ( !GameEngine.InputManager.getKey( KeyEvent.VK_SPACE ) ) {
            grid.setVisible( false );
        }
        grid.render();
    }

}
