package cs.umu.c14hes.spacejourney;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.KeyEvent;
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
    private int score, speed;
    private Context context;
    private Bitmap background;
    private Display display;
    private Point point;
    int dWidht, dHeight;
    Rect rect;

    public GameView(Context context) {
        super(context);
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
        score = 0;
        speed = 10;
        scoreField = new String("Score: ");
        gameStart = System.currentTimeMillis();


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceman));
        obstacles = new ArrayList<>();

        thread.setRunning(true);
        thread.start();

        gameStart = System.currentTimeMillis();
        Runnable obstacleCreator = () -> {
            if (speed <100)
                speed +=1;
            if (speed < 50) {
                obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                        R.drawable.meteor), speed));

            } else if (speed > 50 && speed < 80) {
                for (int i =0; i< 2; i++) {
                    obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                            R.drawable.meteor), speed));
                }
            } else {
                for (int i =0; i< 3; i++) {
                    obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                            R.drawable.meteor), speed));
                }
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("----------------------------------------------------------------");
        System.out.println("----------------------------------------------------------------");
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                System.out.println("BACKBUTTON PRESSED!!");
                pauseGame();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void update() {
        characterSprite.update();
        for (Obstacle obs: obstacles) {
            obs.update();
            if (collision(obs.getPosition(),characterSprite.getPosition())){
                System.out.println("ENDING GAME");
                System.out.println("SCORE: "+score);
                //endScreen();
                surfaceDestroyed(getHolder());
                Intent intent = new Intent(context, Result.class);
                intent.putExtra("SCORE", score);
                context.startActivity(intent);
            }
        }
        score = (int) (System.currentTimeMillis() - gameStart)/100;
    }

    private void pauseGame(){
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void resumeGame(){
        thread.setRunning(true);
        thread.start();
        gameStart = System.currentTimeMillis();// - gameStart;
        Runnable obstacleCreator = () -> {
            if (speed <100)
                speed +=1;
            if (speed < 50) {
                obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                        R.drawable.meteor), speed));

            } else if (speed > 50 && speed < 80) {
                for (int i =0; i< 2; i++) {
                    obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                            R.drawable.meteor), speed));
                }
            } else {
                for (int i =0; i< 3; i++) {
                    obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),
                            R.drawable.meteor), speed));
                }
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(obstacleCreator, 0, 1, TimeUnit.SECONDS);
    }

    public boolean collision(Position obstacle, Position mooncake) {
        if(Math.abs(obstacle.getX()- mooncake.getX()) < 10) {
            if (Math.abs(obstacle.getY()- mooncake.getY()) < 170){
                System.out.println("COLLISION");
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
            obstacles.forEach(elem -> {
                elem.draw(canvas);
            });
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(46);
            canvas.drawText(scoreField+score, 10 ,40 , paint);

        }
    }
}
