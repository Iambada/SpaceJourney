package cs.umu.c14hes.spacejourney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

public class StartGameActivity extends Activity {
    GameView gameView;
    boolean saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GameViewActivity", "Game Create");
        gameView = new GameView(this);
        setContentView(gameView);


    }

    public void onStart() {
        super.onStart();
        Log.d("GameViewActivity", "Game OnStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("GameViewActivity", "Game paused");
        gameView.pauseGame();
        setContentView(gameView);

    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("GameViewActivity", "Game restart");
        gameView.resumeGame();
        setContentView(gameView);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GameViewActivity", "Game resumed");
        gameView.resumeGame();
        setContentView(gameView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("GameViewActivity", "Back button pressed");
        gameView.pauseGame();
        startActivity(new Intent(this, MainActivity.class));
    }
}
