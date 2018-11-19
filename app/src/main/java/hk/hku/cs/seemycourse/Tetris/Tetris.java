package hk.hku.cs.seemycourse.Tetris;

import android.util.Log;

import java.util.ArrayList;

public class Tetris {
    private boolean[][] board;
    private int width;
    private int height;

    public void init(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new boolean[width][height];
        clear();
    }

    /**
     * Check whether it's possible to move a pixel
     * @param point target point
     * @param direction direction to move
     * @return success or not
     */
    public boolean canMove(TetrisPoint point, TetrisDirection direction) {
        int x = point.getX();
        int y = point.getY();
        switch (direction) {
            case DOWN:
                if (y + 1 > height - 1|| board[x][y + 1]) return false;
                break;
            case LEFT:
                if (x - 1 < 0 || board[x - 1][y]) return false;
                Log.d("surface", String.format("[%d %d] => {%b}", x - 1, y, board[x - 1][y]));
                break;
            case RIGHT:
                if (x + 1 > width - 1 || board[x + 1][y]) return false;
                break;
        }
        return true;
    }

    /**
     * Move pixel
     * @param points target point
     * @param direction direction to move
     */
    public void move(ArrayList<TetrisPoint> points, TetrisDirection direction) {
        for (int i = 0; i < points.size(); ++i) {
            setPoint(points.get(i).getX(), points.get(i).getY(), false);
        }
        for (int i = 0; i < points.size(); ++i) {
            int x = points.get(i).getX();
            int y = points.get(i).getY();
            switch (direction) {
                case DOWN:
                    setPoint(x, y + 1, true);
                    break;
                case LEFT:
                    setPoint(x - 1, y, true);
                    break;
                case RIGHT:
                    setPoint(x + 1, y, true);
                    break;
            }
        }
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPoint(TetrisPoint p, boolean v) {
        this.board[p.getX()][p.getY()] = v;
    }
    public void setPoint(int x, int y, boolean v) {
        this.board[x][y] = v;
    }
    public void setLine(int y, boolean v) {
        for (int x = 0; x < width; ++x) {
            this.board[x][y] = v;
        }
    }

    public boolean getPointValue(int x, int y) {
        return this.board[x][y];
    }

    /**
     * Pull down all pixels to next line
     * @param baseLine base line index
     */
    public void pullDownAllPixels(int baseLine) {
        for (int y = baseLine; y > 0; --y) {
            for (int x = 0; x < width; ++x) {
                board[x][y] = board[x][y - 1];
            }
        }
    }

    /**
     * Check whether where is at least one pixel is filled
     * @param y line index
     * @return exist or not
     */
    public boolean existFilled(int y) {
        for (int x = 0; x < width; ++x) {
            if (board[x][y]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether there is a line that is fully filled
     * @return line index
     */
    public ArrayList<Integer> checkLineIsFullyFilled() {
        ArrayList<Integer> lines = new ArrayList<>();
        for (int y = 0; y < height; ++y) {
            boolean isFull = true;
            for (int x = 0; x < width; ++x) {
                isFull &= getPointValue(x, y);
            }
            if (isFull) {
                lines.add(y);
            }
        }
        return lines;
    }
    
    /**
     * Clear the board
     */
    public void clear() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                board[x][y] = false;
            }
        }
    }
}
