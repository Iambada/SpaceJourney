package cs.umu.c14hes.spacejourney;

import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    public static final int MAX_FPS = 30;
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder,GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (canvas !=null ) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() -  startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try {
                if(waitTime >0 ){
                    this.sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() -startTime;
            frameCount++;
            if (frameCount == MAX_FPS){
                frameCount = 0;
                totalTime = 0;

            }
        }
    }
}
