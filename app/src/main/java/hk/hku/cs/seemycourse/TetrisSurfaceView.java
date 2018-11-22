package hk.hku.cs.seemycourse;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import hk.hku.cs.seemycourse.Tetris.Tetris;
import hk.hku.cs.seemycourse.Tetris.TetrisBlock;
import hk.hku.cs.seemycourse.Tetris.TetrisDirection;
import hk.hku.cs.seemycourse.Tetris.TetrisPoint;

public class TetrisSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private static int TOP_MARGIN = 3;

    /* Holds the surface frame */
    private SurfaceHolder holder;

    /* Draw thread */
    private Thread drawThread;

    /* True when the surface is ready to draw */
    private boolean surfaceReady = false;

    /* Drawing thread flag */
    private boolean drawingActive = false;

    /* Time per frame for 60 FPS */
    private static final int MAX_FRAME_TIME = (int) (1000.0 / 4.0);

    private static final String TAG = "surface";

    /* Gesture Detector */
    private GestureDetectorCompat gestureDetector;

    /* Paint Mesh */
    private Paint FillPaint;
    private Paint StrokePaint;

    /* Tetris Game Context */
    private Tetris gameCtx;
    private TetrisBlock currentBlock;


    public TetrisSurfaceView(Context context) {
        super(context);
        init(context);
    }
    public TetrisSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public TetrisSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    @TargetApi(21)
    public TetrisSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context ctx) {
        /* setup SurfaceView Holder */
        holder = getHolder();
        holder.addCallback(this);

        /* Screen do not sleep */
        setKeepScreenOn(true);
        setFocusable(true);

        gestureDetector = new GestureDetectorCompat(ctx, new GestureListener());

        /* Initialize Game */
        gameCtx = new Tetris();
        gameCtx.init(12, 36);

        FillPaint = new Paint();
        FillPaint.setColor(getResources().getColor(R.color.colorPrimary));
        FillPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        StrokePaint = new Paint();
        StrokePaint.setColor(Color.BLACK);
        StrokePaint.setStyle(Paint.Style.STROKE);
        StrokePaint.setStrokeWidth(2);
    }

    public void reload() {
        currentBlock = null;
        gameCtx.clear();
    }

    /**
     * Game rendering here
     * @param c Canvas to draw
     */
    public void render(Canvas c) {
        int canvasWidth = c.getWidth();
        int canvasHeight = c.getHeight();

        int boardWidth = gameCtx.getWidth();
        int boardHeight = gameCtx.getHeight();

        int gridWidth = Math.round((float)canvasWidth / boardWidth);
        int gridHeight = Math.round((float)canvasHeight / boardHeight);

        c.drawColor(Color.WHITE);
        for (int x = 0; x < boardWidth; ++x) {
            for (int y = 0; y < boardHeight; ++y) {
                // Stroke
                c.drawRect(
                        gridWidth * x,
                        gridHeight * y + TOP_MARGIN,
                        gridWidth * (x + 1),
                        gridHeight * (y + 1) + TOP_MARGIN,
                        StrokePaint
                );
                // If there is a block
                if (gameCtx.getPointValue(x, y)) {
                    // Fill
                    c.drawRect(
                            gridWidth * x,
                            gridHeight * y + TOP_MARGIN,
                            gridWidth * (x + 1),
                            gridHeight * (y + 1) + TOP_MARGIN,
                            FillPaint
                    );
                }
            }
        }
    }

    /**
     * Game logic here
     */
    public void tick() {
        // Win
        /*if () {
            reload();
        }*/

        // Lose
        if (currentBlock == null && gameCtx.existFilled(0)) {
            reload();
        }

        // Remove Filled Line
        ArrayList<Integer> fullLines = gameCtx.checkLineIsFullyFilled();
        for (int i = 0; i < fullLines.size(); ++i) {
            int y = fullLines.get(i);
            gameCtx.setLine(y, false);
            gameCtx.pullDownAllPixels(y);
            gameCtx.setLine(0, false);
        }
        
        if (currentBlock == null) {
            currentBlock = TetrisBlock.createBlock(
                    Math.round((float)Math.random() * 4),
                    gameCtx,
                    new TetrisPoint(4, 0)
            );
            return;
        }

        boolean canMoveDown = currentBlock.move(TetrisDirection.DOWN);
        if (!canMoveDown) {
            currentBlock = null;
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (width == 0 || height == 0) return;

        // resize your UI
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;

        if (drawThread != null){
            Log.d(TAG, "draw thread still active..");
            drawingActive = false;
            try{
                drawThread.join();
            } catch (InterruptedException ignored){}
        }

        surfaceReady = true;
        startDrawThread();
        Log.d(TAG, "Created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface is not used anymore - stop the drawing thread
        stopDrawThread();
        // and release the surface
        holder.getSurface().release();

        this.holder = null;
        surfaceReady = false;
        Log.d(TAG, "Destroyed");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * Stops the drawing thread
     */
    public void stopDrawThread() {
        if (drawThread == null) {
            Log.d(TAG, "DrawThread is null");
            return;
        }
        drawingActive = false;
        while (true) {
            try{
                Log.d(TAG, "Request last frame");
                drawThread.join(5000);
                break;
            } catch (Exception e) {
                Log.e(TAG, "Could not join with draw thread");
            }
        }
        drawThread = null;
    }

    /**
     * Creates a new draw thread and starts it.
     */
    public void startDrawThread() {
        if (surfaceReady && drawThread == null) {
            drawThread = new Thread(this, "Draw thread");
            drawingActive = true;
            drawThread.start();
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "Draw thread started");
        long frameStartTime;
        long frameTime;

        while (drawingActive) {
            if (holder == null) return;

            frameStartTime = System.nanoTime();
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                try {
                    synchronized (holder) {
                        tick();
                        render(canvas);
                    }
                } finally {

                    holder.unlockCanvasAndPost(canvas);
                }
            }

            // calculate the time required to draw the frame in ms
            frameTime = (System.nanoTime() - frameStartTime) / 1000000;

            if (frameTime < MAX_FRAME_TIME){
                try {
                    Thread.sleep(MAX_FRAME_TIME - frameTime);
                } catch (InterruptedException ignore) {}
            }

        }
        Log.d(TAG, "Draw thread finished");
    }

    /**
     * Gesture Detect
     * Swipe Left
     * Swipe Right
     * Double Tap
     */
    public class GestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onDown(MotionEvent e) { return true; }

        @Override
        public void onShowPress(MotionEvent e) {}

        @Override
        public boolean onSingleTapUp(MotionEvent e) { return true; }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return true; }

        @Override
        public void onLongPress(MotionEvent e) {}

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) return false;
            // left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if (currentBlock != null) { currentBlock.move(TetrisDirection.LEFT); }
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if (currentBlock != null) { currentBlock.move(TetrisDirection.RIGHT); }
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) { return true; }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) { return false; }
    }
}
