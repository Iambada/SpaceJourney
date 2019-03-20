package cs.umu.c14hes.spacejourney;

import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    public static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running, game;
    public static Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder,GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        running = true;
        game = true;

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
        game = isRunning;
    }

    public void pauseGame() { running = false; }

    public void resumeGame() { running = true; }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (game) {
            while (running) {
                startTime = System.nanoTime();
                canvas = null;
                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        this.gameView.update();
                        if(canvas != null)
                            this.gameView.draw(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                timeMillis = (System.nanoTime() - startTime) / 1000000;
                waitTime = targetTime - timeMillis;
                try {
                    if (waitTime > 0) {
                        this.sleep(waitTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                totalTime += System.nanoTime() - startTime;
                frameCount++;
                if (frameCount == MAX_FPS) {
                    averageFPS = 1000/((totalTime/frameCount)/1000000);
                    System.out.println("FPS: "+averageFPS);
                    frameCount = 0;
                    totalTime = 0;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }
    }
}
