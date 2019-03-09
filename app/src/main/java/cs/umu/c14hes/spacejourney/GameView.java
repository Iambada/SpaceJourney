package cs.umu.c14hes.spacejourney;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private GameThread thread;
    private CharacterSprite characterSprite;
    private List<Obstacle> obstacles;
    private long gameStart;
    private String scoreField;
    private int speed;
    private double score;
    private Context context;
    private Bitmap background;
    private Display display;
    private Point point;
    private ScheduledExecutorService executor;
    private Orientation orientation;
    private long frameTime;
    int dWidht, dHeight;
    Rect rect;

    public GameView(Context context) {
        super(context);
        Constants.CURRENT_CONTEXT = context;
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(),
                R.drawable.space_background);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidht = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidht,dHeight);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);

        setFocusable(true);
        score = 0.0;
        speed = 10;
        scoreField = new String("Score: ");
        gameStart = System.currentTimeMillis();
        obstacles = new ArrayList<>();
        orientation = new Orientation();
        orientation.register();
        frameTime = System.currentTimeMillis();
        Constants.SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        Constants.SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Constants.INIT_TIME = System.currentTimeMillis();
        orientation.newGame();
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceman));
        thread = new GameThread(getHolder(), this);
        if (!thread.isAlive()) {
            thread.start();
        }
        gameStart = System.currentTimeMillis();
        Runnable obstacleCreator = () -> {
            if (speed <100)
                speed +=1;
            if (speed < 50) {
                obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                        R.drawable.meteor), speed));

            } else if (speed > 50 && speed < 80) {
                for (int i =0; i< 3; i++) {
                    obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                            R.drawable.meteor), speed));
                }
            } else {
                for (int i =0; i< 4; i++) {
                    obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                            R.drawable.meteor), speed));
                }
            }
            System.out.println("---------------------------------------------------");
        };
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(obstacleCreator, 0, 1, TimeUnit.SECONDS);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) { }
            retry = false;
        }
    }

    private void endGame() {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) { }
            retry = false;
        }
    }

    public void update() {
        if (frameTime < Constants.INIT_TIME)
            frameTime = Constants.INIT_TIME;
        int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
        frameTime = System.currentTimeMillis();
        if (orientation.getOrientation() != null && orientation.getStartrOrientation() !=null){
            float roll = orientation.getOrientation()[2] - orientation.getStartrOrientation()[2];
            float xSpeed  = roll * Constants.SCREEN_WIDTH/1000f;
            int nextXPos = characterSprite.getPosition().getX();
            nextXPos += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
            characterSprite.getPosition().setX(nextXPos);
        }

        characterSprite.update();
        for (Obstacle obs: obstacles) {
            if (collision(obs.getPosition(),characterSprite.getPosition())){
                System.err.println("ENDING GAME");
                thread.interrupt();
                executor.shutdown();
                Intent intent = new Intent(Constants.CURRENT_CONTEXT, Result.class);
                intent.putExtra("SCORE", (int)score);
                Constants.CURRENT_CONTEXT.startActivity(intent);
                endGame();
            }
            obs.update();
        }

        score +=0.1;
    }

    public void pauseGame(){
        thread.pauseGame();
        executor.shutdown();
    }

    public boolean collision(Position obstacle, Position mooncake) {
        if(Math.abs(obstacle.getX() - mooncake.getX()) < 50) {
            if (Math.abs(obstacle.getY()- mooncake.getY()) < 200){
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas !=null) {
            canvas.drawBitmap(background , null, rect, null);
            characterSprite.draw(canvas);
            for (Obstacle ob : obstacles)
                ob.draw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(46);
            canvas.drawText(scoreField+(int)score, 10 ,40 , paint);

        }
    }
}
