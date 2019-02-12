package cs.umu.c14hes.spacejourney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView astronaut;
    private ImageView meteor;

    private List<ImageView> obstacles;

    // Size
    private int frameWidth;
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    // Position
    private int astronautY;
    private int astronautX;
    private int meteorX;
    private int meteorY;

    // Speed
    private int astronautSpeed;
    private int meteorSpeed;

    // Score
    private int score = 0;
    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer;



    // Status Check
    private boolean action_flg = false;
    private boolean start_flg = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        timer = new Timer();
        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        astronaut = findViewById(R.id.astronaut);
        meteor = findViewById(R.id.meteor);
        obstacles = new ArrayList<>();
        obstacles.add(meteor);

        // Get screen size.
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        astronautSpeed = Math.round(screenWidth / 60);  // 1280 / 60 = 21.333... => 21
        meteorSpeed = 10;

        astronautY = frameHeight - boxSize;
        astronaut.setY(astronautY);
        // Move to out of screen.
        //meteor.setX(-80);
        //meteor.setY(-80);

        Runnable obstacleCreator = () -> {
            if (meteorSpeed <100)
                meteorSpeed +=1;
            if (meteorSpeed < 50) {
                obstacles.add(findViewById(R.id.meteor));

            } else if (meteorSpeed > 50 && meteorSpeed < 80) {
                for (int i =0; i< 2; i++) {
                    obstacles.add(findViewById(R.id.meteor));
                }
            } else {
                for (int i =0; i< 3; i++) {
                    obstacles.add(findViewById(R.id.meteor));
                }
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(obstacleCreator, 0, 1, TimeUnit.SECONDS);

        scoreLabel.setText("Score : 0");
    }


    public void changePos() {
        hitCheck();
        meteorY += meteorSpeed;

        if (meteorY < 0) {
            meteorX = (int) Math.floor(Math.random() * (screenWidth - meteor.getWidth()));
            meteorY = screenHeight + 10;
        }
        meteor.setX(meteorX);
        meteor.setY(meteorY);

        if (action_flg == true) {
            // Touching
            astronautX -= astronautSpeed;

        } else {
            // Releasing
            astronautX += astronautSpeed;
        }

        // Check astronaut position.
        if (astronautX < 0) astronautX = 0;

        if (astronautX > frameWidth - boxSize) astronautX = frameWidth - boxSize;
        astronaut.setX(astronautX);

        scoreLabel.setText("Score : " + score);

    }


    public void hitCheck() {
        int meteorCenterX = meteorX + meteor.getWidth() / 2;
        int meteorCenterY = meteorY + meteor.getHeight() / 2;
        if (0 <= meteorCenterY && meteorCenterY <= boxSize &&
                astronautY <= meteorCenterX && meteorCenterX <= astronautY + boxSize) {
            timer.cancel();
            timer = null;
            // Show Result
            Intent intent = new Intent(getApplicationContext(), Result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        } else {
            score +=1;
        }
    }



    public boolean onTouchEvent(MotionEvent me) {


        if (start_flg == false) {
            start_flg = true;
            FrameLayout frame = findViewById(R.id.frame);
            frameWidth = frame.getWidth();
            frameHeight = frame.getHeight();

            // The astronaut is a square.(height and width are the same.)
            boxSize = astronaut.getWidth();
            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> changePos());
                }
            }, 0, 20);

        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }

        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
