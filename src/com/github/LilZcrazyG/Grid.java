package com.github.LilZcrazyG;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;

public class Grid {

    // private variables
    private static int x, y, width, height, rows, columns, cellSize;
    private static ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
    private static ArrayList<ArrayList<Cell>> oldCells = new ArrayList<>();
    private static boolean visible = false;

    public Grid( int x, int y, int rows, int columns, int cellSize ) {
        Grid.x = x;
        Grid.y = y;
        Grid.width = rows*cellSize;
        Grid.height = columns*cellSize;
        Grid.rows = rows;
        Grid.columns = columns;
        Grid.cellSize = cellSize;
        for ( int row = 0; row < rows; row++ ) {
            cells.add( new ArrayList<>() );
            for ( int column = 0; column < columns; column++ ) {
                cells.get( row ).add( new Cell( row, column, cellSize ) );
            }
        }
        oldCells = cells;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible( boolean visible ) {
        Grid.visible = visible;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCellSize() {
        return cellSize;
    }

    public static Cell getCell( int x, int y ) {
        return cells.get( x ).get( y );
    }

    public static class Cell {

        // private variables
        private static int size;
        private int pixelX, pixelY, indexX, indexY;
        private Color wallColor, fillColor, highlightColor;
        private boolean highlighted, borderless, alive, oldAlive;

        public Cell( int indexX, int indexY, int size ) {
            this.pixelX = indexX*size;
            this.pixelY = indexY*size;
            this.indexX = indexX;
            this.indexY = indexY;
            Cell.size = size;
            this.wallColor = Color.WHITE;
            this.fillColor = Color.BLACK;
            this.highlightColor = Color.WHITE;
            this.highlighted = false;
            this.borderless = false;
            this.alive = false;
        }

        public void setHighlighted( boolean highlighted ) {
            this.highlighted = highlighted;
        }

        public boolean getHighlighted() {
            return highlighted;
        }

        public boolean getBorderless() {
            return borderless;
        }

        public void setBorderless( boolean borderless ) {
            this.borderless = borderless;
        }

        public void setAlive( boolean alive ) {
            oldAlive = this.alive;
            this.alive = alive;
        }

        public boolean getAlive() {
            return alive;
        }

        public void setOldAlive( boolean oldAlive ) {
            this.oldAlive = oldAlive;
        }

        public boolean getOldAlive() {
            return oldAlive;
        }

        public ArrayList<Boolean> getNeighbors() {
            ArrayList<Boolean> neighbors = new ArrayList<>();
            if ( indexX - 1 != -1 && indexY - 1 != -1 ) {
                neighbors.add( Grid.getCell( indexX-1, indexY-1 ).getOldAlive() );
            } else {
                neighbors.add( false );
            }
            if ( indexY - 1 != -1 ) {
                neighbors.add( Grid.getCell( indexX, indexY-1 ).getOldAlive() );
            } else {
                neighbors.add( false );
            }
            if ( indexX + 1 != Grid.rows && indexY - 1 != -1 ) {
                neighbors.add( Grid.getCell( indexX+1, indexY-1 ).getOldAlive() );
            } else {
                neighbors.add( false );
            }
            if ( indexX - 1 != -1 ) {
                neighbors.add( Grid.getCell( indexX-1, indexY ).getOldAlive() );
            } else {
                neighbors.add( false );
            }
            if ( indexX + 1 != Grid.rows ) {
                neighbors.add( Grid.getCell( indexX+1, indexY ).getOldAlive() );
            } else {
                neighbors.add( false );
            }
            if ( indexX - 1 != -1 && indexY + 1 != Grid.columns ) {
                neighbors.add( Grid.getCell( indexX-1, indexY+1 ).getOldAlive() );
            } else {
                neighbors.add( false );
            }
            if ( indexY + 1 != Grid.columns ) {
                neighbors.add( Grid.getCell( indexX, indexY+1 ).getOldAlive() );
            } else {
                neighbors.add( false );
            }
            if ( indexX + 1 != Grid.rows && indexY + 1 != Grid.columns ) {
                neighbors.add( Grid.getCell( indexX+1, indexY+1 ).getOldAlive() );
            } else {
                neighbors.add( false );
            }
            return neighbors;
        }

        public void tick() {
            ArrayList<Boolean> neighbors = getNeighbors();
            if ( Collections.frequency( neighbors, true ) < 2 || Collections.frequency( neighbors, true ) > 3 ) {
                alive = false;
            }
            if ( Collections.frequency( neighbors, true ) == 3 ) {
                alive = true;
            }
        }

        public void render() {
            Color oldColor = GameEngine.GraphicsEngine.setColor( fillColor );
            if ( alive ) GameEngine.GraphicsEngine.setColor( Color.WHITE );
            if ( highlighted ) GameEngine.GraphicsEngine.setColor( highlightColor );
            if ( !borderless ) {
                GameEngine.GraphicsEngine.rectangleFilled( pixelX+( size/6 ), pixelY+( size/6 ), size - ( size/3 ), size - ( size/3 ) );
                GameEngine.GraphicsEngine.setColor( wallColor );
                GameEngine.GraphicsEngine.rectangle( pixelX, pixelY, size-1, size-1 );
            } else {
                GameEngine.GraphicsEngine.rectangleFilled( pixelX, pixelY, size, size );
            }
            GameEngine.GraphicsEngine.setColor( oldColor );
        }

    }

    public void tick() {
        for ( ArrayList<Cell> row : cells ) {
            for ( Cell cell : row ) {
                cell.tick();
            }
        }
        for ( ArrayList<Cell> row : cells ) {
            for ( Cell cell : row ) {
                cell.setOldAlive( cell.getAlive() );
            }
        }
    }

    public void render() {
        for ( ArrayList<Cell> row : cells ) {
            for ( Cell cell : row ) {
                if ( visible == cell.getBorderless() ) {
                    cell.setBorderless( !visible );
                }
                cell.render();
            }
        }
        GameEngine.GraphicsEngine.setColor( Color.WHITE );
        GameEngine.GraphicsEngine.rectangle( x, y, width, height );
    }
}
