package hk.hku.cs.seemycourse.Tetris;

import android.util.Log;

import java.util.ArrayList;

public abstract class TetrisBlock {
    protected Tetris ctx;
    protected ArrayList<TetrisPoint> points;

    public TetrisBlock(Tetris ctx) {
        this.ctx = ctx;
        points = new ArrayList<>();
    }

    abstract public boolean move(TetrisDirection direction);
    protected void doMove(TetrisDirection direction) {
        ctx.move(points, direction);
        for (int i = 0; i < points.size(); ++i) {
            TetrisPoint p = points.get(i);
            switch (direction) {
                case DOWN:
                    points.get(i).setY(p.getY() + 1);
                    break;
                case LEFT:
                    points.get(i).setX(p.getX() - 1);
                    break;
                case RIGHT:
                    points.get(i).setX(p.getX() + 1);
                    break;
            }
        }
    }

    public static TetrisBlock createBlock(int id, Tetris ctx, TetrisPoint base) {
        switch (id) {
            case 0b000: // Line
                return new TetrisBlock.Line(ctx, base);
            case 0b001: // L
                return new TetrisBlock.L(ctx, base);
            case 0b010: // Cube
                return new TetrisBlock.Cube(ctx, base);
        }
        return new TetrisBlock.Line(ctx, base);
    }

    /**
     * Line Block
     *
     * ■■■■
     */
    public static class Line extends TetrisBlock {
        public Line(Tetris ctx, TetrisPoint base) {
            super(ctx);

            for (int i = 0; i < 4; ++i) {
                int x = base.getX() + i;
                int y = base.getY();
                points.add(new TetrisPoint(x, y));
                ctx.setPoint(x, y, true);
            }
        }

        @Override
        public boolean move(TetrisDirection direction) {
            // Check whether can move down
            boolean canMove;
            switch (direction) {
                case DOWN: {
                    for (int i = 0; i < points.size(); ++i) {
                        canMove = ctx.canMove(points.get(i), direction);
                        if (!canMove) return false;
                    }
                } break;
                case LEFT: {
                    if (!ctx.canMove(points.get(0), direction)) return false;
                } break;
                case RIGHT: {
                    if (!ctx.canMove(points.get(points.size() - 1), direction)) return false;
                } break;
            }
            // OK
            doMove(direction);
            return true;
        }
    }

    /**
     * L Block
     *
     * ■
     * ■■■
     */
    public static class L extends TetrisBlock {
        public L(Tetris ctx, TetrisPoint base) {
            super(ctx);

            int x = base.getX();
            int y = base.getY();
            TetrisPoint[] p = new TetrisPoint[]{
                    new TetrisPoint(x, y),
                    new TetrisPoint(x, y + 1),
                    new TetrisPoint(x + 1, y + 1),
                    new TetrisPoint(x + 2, y + 1)
            };
            for (int i = 0; i < 4; ++i) {
                points.add(p[i]);
                ctx.setPoint(p[i], true);
            }
        }

        @Override
        public boolean move(TetrisDirection direction) {
            // Check whether can move down
            boolean canMove;
            switch (direction) {
                case DOWN: {
                    for (int i = 1; i < points.size(); ++i) {
                        canMove = ctx.canMove(points.get(i), direction);
                        if (!canMove) return false;
                    }
                } break;
                case LEFT: {
                    if (!ctx.canMove(points.get(0), direction)) return false;
                    if (!ctx.canMove(points.get(1), direction)) return false;
                } break;
                case RIGHT: {
                    if (!ctx.canMove(points.get(points.size() - 1), direction)) return false;
                } break;
            }
            // OK
            doMove(direction);
            return true;
        }
    }
    /**
     * Revert-L Block
     *
     *   ■
     * ■■■
     */
    public static class RevertL extends TetrisBlock {
        public RevertL(Tetris ctx, TetrisPoint base) {
            super(ctx);

            int x = base.getX();
            int y = base.getY();
            TetrisPoint[] p = new TetrisPoint[]{
                    new TetrisPoint(x, y + 1),
                    new TetrisPoint(x + 1, y + 1),
                    new TetrisPoint(x + 2, y + 1),
                    new TetrisPoint(x + 2, y)
            };
            for (int i = 0; i < 4; ++i) {
                points.add(p[i]);
                ctx.setPoint(p[i], true);
            }
        }

        @Override
        public boolean move(TetrisDirection direction) {
            // Check whether can move down
            boolean canMove;
            switch (direction) {
                case DOWN: {
                    for (int i = 0; i < 3; ++i) {
                        canMove = ctx.canMove(points.get(i), direction);
                        if (!canMove) return false;
                    }
                } break;
                case LEFT: {
                    if (!ctx.canMove(points.get(0), direction)) return false;
                } break;
                case RIGHT: {
                    if (!ctx.canMove(points.get(2), direction)) return false;
                    if (!ctx.canMove(points.get(3), direction)) return false;
                } break;
            }
            // OK
            doMove(direction);
            return true;
        }
    }

    /**
     * Cube Block
     *
     * ■■
     * ■■
     */
    public static class Cube extends TetrisBlock {
        public Cube(Tetris ctx, TetrisPoint base) {
            super(ctx);

            int x = base.getX();
            int y = base.getY();
            TetrisPoint[] p = new TetrisPoint[]{
                    new TetrisPoint(x, y),
                    new TetrisPoint(x, y + 1),
                    new TetrisPoint(x + 1, y + 1),
                    new TetrisPoint(x + 1, y)
            };
            for (int i = 0; i < 4; ++i) {
                points.add(p[i]);
                ctx.setPoint(p[i], true);
            }
        }

        @Override
        public boolean move(TetrisDirection direction) {
            // Check whether can move down
            boolean canMove;
            switch (direction) {
                case DOWN: {
                    for (int i = 1; i < 3; ++i) {
                        canMove = ctx.canMove(points.get(i), direction);
                        if (!canMove) return false;
                    }
                } break;
                case LEFT: {
                    if (!ctx.canMove(points.get(0), direction)) return false;
                    if (!ctx.canMove(points.get(1), direction)) return false;
                } break;
                case RIGHT: {
                    if (!ctx.canMove(points.get(2), direction)) return false;
                    if (!ctx.canMove(points.get(3), direction)) return false;
                } break;
            }
            // OK
            doMove(direction);
            return true;
        }
    }


}
