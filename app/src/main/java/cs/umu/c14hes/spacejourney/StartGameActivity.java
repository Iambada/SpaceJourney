package cs.umu.c14hes.spacejourney;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class StartGameActivity extends Activity {
    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GameViewActivity", "Game Create");
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("GameViewActivity", "Game paused");
        gameView.pauseGame();
        setContentView(gameView);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GameViewActivity", "Game resumed");
        setContentView(gameView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("GameViewActivity", "Back button pressed");
        gameView.pauseGame();
        setContentView(R.layout.activity_main);
    }
}
