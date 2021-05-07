package com.github.LilZcrazyG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameEngine {

    // private variables
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool( 2 );
    private static boolean running = false;

    public static class GraphicsEngine {

        private static WindowEngine.Window window;
        private static BufferStrategy bufferStrategy;
        private static Graphics2D graphics;
        private static BufferedImage bufferedImage;

        public static void initialize( WindowEngine.Window window ) {
            GraphicsEngine.window = window;
            if ( window.getCanvas() == null ) window.addCanvas();
            bufferStrategy = window.getCanvas().getBufferStrategy();
            if ( bufferStrategy == null ) {
                window.getCanvas().createBufferStrategy( 3 );
                bufferStrategy = window.getCanvas().getBufferStrategy();
            }
            graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        }

        public static Graphics2D getGraphics() {
            return graphics;
        }

        public static BufferedImage createImage(int width, int height ) {
            bufferedImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
            return bufferedImage;
        }

        public static Graphics2D getImageGraphics() {
            return (Graphics2D) bufferedImage.getGraphics();
        }

        public static void show() {
            bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();
        }

        public static void dispose() {
            bufferStrategy.dispose();
        }

        public static AffineTransform translate(int x, int y ) {
            AffineTransform oldTransform = graphics.getTransform();
            graphics.translate( x, y );
            return oldTransform;
        }

        public static AffineTransform resetTranslation( AffineTransform transform ) {
            AffineTransform oldTransform = graphics.getTransform();
            graphics.translate( -transform.getTranslateX(), -transform.getTranslateY() );
            return oldTransform;
        }

        public static void scale( double sx, double sy ) {
            graphics.scale( sx, sy );
        }

        public static void clearRect( int x, int y, int width, int height ) {
            graphics.clearRect( x, y, width, height );
        }

        public static void clearScreen() {
            AffineTransform transform = getGraphics().getTransform();
            graphics.clearRect((int) -transform.getTranslateX(), (int) -transform.getTranslateY(), window.getWidth(), window.getHeight() );
        }

        public static Color setColor( Color color ) {
            Color oldColor = graphics.getColor();
            graphics.setColor( color );
            return oldColor;
        }

        public static Color setBackgroundColor( Color color ) {
            Color oldColor = graphics.getBackground();
            graphics.setBackground( color );
            return oldColor;
        }

        public static void text( int x, int y, String str ) {
            graphics.drawString( str, x, y );
        }

        public static void line( int x1, int y1, int x2, int y2 ) {
            graphics.drawLine( x1, y1, x2, y2 );
        }

        public static void rectangle( int x, int y, int width, int height ) {
            graphics.drawRect( x, y, width, height );
        }

        public static void rectangleFilled( int x, int y, int width, int height ) {
            graphics.fillRect( x, y, width, height );
        }

        public static void oval( int x, int y, int width, int height ) {
            graphics.drawOval( x, y, width, height );
        }

        public static void ovalFilled( int x, int y, int width, int height ) {
            graphics.fillOval( x, y, width, height );
        }

        public static void circle( int x, int y, int radius ) {
            graphics.drawOval( x, y, radius*2, radius*2 );
        }

        public static void circleFilled( int x, int y, int radius ) {
            graphics.fillOval( x, y, radius*2, radius*2 );
        }

    }

    static class SoundEngine {

    }

    static class PhysicsEngine {

    }

    public static class InputManager {

        // private variables
        static HashMap<Integer, Boolean> keys = new HashMap<>();
        static int lastKeyPressed, lastKeyReleased;

        public static abstract class KeyListener extends KeyAdapter {

            public abstract void keyTyped(KeyEvent e);
            public abstract void keyPressed(KeyEvent e);
            public abstract void keyReleased(KeyEvent e);

        }

        public static abstract class MouseListener extends MouseAdapter implements MouseWheelListener {

            public abstract void mouseClicked(MouseEvent e);
            public abstract void mousePressed(MouseEvent e);
            public abstract void mouseReleased(MouseEvent e);
            public abstract void mouseWheelMoved(MouseWheelEvent e);

        }

        public static void createKeyListener( WindowEngine.Window window, KeyListener keyListener ) {
            window.addKeyListener( keyListener );
        }

        public static void createMouseListener( WindowEngine.Window window, MouseListener mouseListener ) {
            window.addMouseListener( mouseListener );
        }

        public static void setKey( int key, boolean pressed ) {
            keys.put( key, pressed );
        }

        public static void setLastKeyPressed( int key ) {
            lastKeyPressed = key;
        }

        public static int getLastKeyPressed() {
            return lastKeyPressed;
        }

        public static void setLastKeyReleased( int key ) {
            lastKeyReleased = key;
        }

        public static int getLastKeyReleased() {
            return lastKeyReleased;
        }

        public static boolean getKey( int key ) {
            try {
                return keys.get( key );
            } catch (Exception e) {
                return false;
            }
        }

        public static HashMap<Integer, Boolean> getKeys() {
            return keys;
        }

    }

    public static class WindowEngine {

        // default variables
        private static final int DEFAULT_WIDTH = 520;
        private static final int DEFAULT_HEIGHT = 520;
        private static final String DEFAULT_TITLE = "Untitled Window";
        private static final boolean DEFAULT_SHOW_WINDOW = true;
        private static final int DEFAULT_CLOSE_OPERATION = JFrame.EXIT_ON_CLOSE;

        // private variables
        private static HashMap<String, Window> windows = new HashMap<>();

        public static void createWindow( String windowName, Window window ) {
            windows.put( windowName, window );
        }

        public static Window getWindow( String windowName ) {
            return windows.get( windowName );
        }

        public static class Window {

            private int width;
            private int height;
            private String title;
            private boolean showWindow;
            private int defaultCloseOperation;
            private JFrame jFrame = new JFrame();
            private JPanel jPanel = new JPanel();
            private Canvas canvas = null;
            private boolean fullScreen = false;


            public Window() {
                this.width = DEFAULT_WIDTH;
                this.height = DEFAULT_HEIGHT;
                this.title = DEFAULT_TITLE;
                this.showWindow = DEFAULT_SHOW_WINDOW;
                this.defaultCloseOperation = DEFAULT_CLOSE_OPERATION;
                initialize();
            }

            public Window( String title ) {
                this.width = DEFAULT_WIDTH;
                this.height = DEFAULT_HEIGHT;
                this.title = title;
                this.showWindow = DEFAULT_SHOW_WINDOW;
                this.defaultCloseOperation = DEFAULT_CLOSE_OPERATION;
                initialize();
            }

            public Window( int width, int height, String title ) {
                this.width = width;
                this.height = height;
                this.title = title;
                this.showWindow = DEFAULT_SHOW_WINDOW;
                this.defaultCloseOperation = DEFAULT_CLOSE_OPERATION;
                initialize();
            }

            private void initialize() {
                fullScreen( true );
                jFrame.setSize( width, height );
                jFrame.setTitle( title );
                jFrame.setVisible( showWindow );
                jFrame.setDefaultCloseOperation( defaultCloseOperation );
                jFrame.setResizable( false );
                jPanel.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 0 ) );
                jPanel.setLayout( new GridLayout( 0, 1 ) );
                jFrame.add( jPanel );
            }

            public void addCanvas() {
                canvas = new Canvas();
                canvas.setPreferredSize( new Dimension( width, height ) );
                canvas.setMaximumSize( new Dimension( width, height ) );
                canvas.setMinimumSize( new Dimension( width, height ) );
                jPanel.add( canvas );
                jFrame.pack();
            }

            public Canvas getCanvas() {
                return canvas;
            }

            public void addKeyListener( KeyListener keyListener ) {
                canvas.addKeyListener( keyListener );
            }

            public void addMouseListener( MouseListener mouseListener ) { canvas.addMouseListener( mouseListener ); }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public void fullScreen( boolean fullScreen ) {
                this.fullScreen = fullScreen;
                jFrame.setExtendedState( fullScreen?JFrame.MAXIMIZED_BOTH:JFrame.NORMAL );
                jFrame.setUndecorated( fullScreen );
            }

        }

    }

    public static class GameStateManager {

        // default variables
        private static final long DEFAULT_TICK_SPEED = 1000/60;
        private static final long DEFAULT_RENDER_SPEED = 1000/240;

        // private variables
        private static HashMap<String, GameState> gameStates = new HashMap<>();
        private static GameState currentState = null;

        /**
         * Creates a new game state.
         * @param stateName The name of the game state.
         * @param gameState The game state.
         */
        public static void createState( String stateName, GameState gameState ) {
            gameStates.put( stateName, gameState );
        }

        /**
         * Sets the current state.
         * @param stateName The name of the state.
         */
        public static void setState( String stateName ) {
            currentState = gameStates.get( stateName );
        }

        /**
         * Gets the given game state.
         * @param stateName The name of the game state.
         * @return The game state.
         */
        public static GameState getState( String stateName ) {
            return gameStates.get( stateName );
        }

        /**
         * Sets the current game state.
         * @param stateName The name of the state.
         */
        public static void setCurrentState( String stateName ) {
            if ( currentState != null ) {
                currentState.setActiveState( false );
            }
            currentState = gameStates.get( stateName );
            currentState.setActiveState( true );
        }

        /**
         * Gets the current game state.
         * @return The current game state.
         */
        public static GameState getCurrentState() { return currentState; }

        public static abstract class GameState {

            // private variables
            private long tickSpeed = DEFAULT_TICK_SPEED;
            private long renderSpeed = DEFAULT_RENDER_SPEED;
            private boolean activeState = false;
            private Runnable tickTask = new Runnable() {
                @Override
                public void run() {
                    try {
                        tick();
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            };
            private Runnable renderTask = new Runnable() {
                @Override
                public void run() {
                    try {
                        render();
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            };

            public void setActiveState( boolean activeState ) {
                this.activeState = activeState;
            }

            public boolean getActiveState() {
                return activeState;
            }

            /**
             * Sets the tick speed of this game state.
             * @param tickSpeed The tick speed.
             */
            public void setTickSpeed( long tickSpeed ) {
                this.tickSpeed = tickSpeed;
                if ( activeState && GameEngine.running ) {
                    executor.shutdown();
                    executor = Executors.newScheduledThreadPool( 2 );
                    executor.scheduleAtFixedRate( tickTask, 0, tickSpeed, TimeUnit.MILLISECONDS );
                    executor.scheduleAtFixedRate( renderTask, 0, renderSpeed, TimeUnit.MILLISECONDS );
                }
            }

            /**
             * Gets the tick speed.
             * @return The tick speed.
             */
            public long getTickSpeed() {
                return tickSpeed;
            }

            /**
             * Sets the render speed.
             * @param renderSpeed The render speed.
             */
            public void setRenderSpeed( long renderSpeed ) {
                this.renderSpeed = renderSpeed;
            }

            /**
             * Gets the render speed.
             * @return The render speed.
             */
            public long getRenderSpeed() {
                return renderSpeed;
            }

            /**
             * Gets the tick task.
             * @return The tick task.
             */
            public Runnable getTickTask() {
                return tickTask;
            }

            /**
             * Gets the render task.
             * @return The render task.
             */
            public Runnable getRenderTask() {
                return renderTask;
            }

            public abstract void tick();
            public abstract void render();

        }

    }

    /**
     * Run with a specific game state.
     * @param gameState The game state.
     */
    public static void run( GameStateManager.GameState gameState ) {
        executor.scheduleAtFixedRate( gameState.getTickTask(), 0, gameState.getTickSpeed(), TimeUnit.MILLISECONDS );
        executor.scheduleAtFixedRate( gameState.getRenderTask(), 0, gameState.getRenderSpeed(), TimeUnit.MILLISECONDS );
        running = true;
    }

    /**
     * Runs with the current game state.
     */
    public static void run() {
        GameStateManager.GameState gameState = GameStateManager.getCurrentState();
        executor.scheduleAtFixedRate( gameState.getTickTask(), 0, gameState.getTickSpeed(), TimeUnit.MILLISECONDS );
        executor.scheduleAtFixedRate( gameState.getRenderTask(), 0, gameState.getRenderSpeed(), TimeUnit.MILLISECONDS );
        running = true;
    }

}
