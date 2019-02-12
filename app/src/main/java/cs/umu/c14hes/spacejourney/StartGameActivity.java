package cs.umu.c14hes.spacejourney;

import android.app.Activity;
import android.os.Bundle;

public class StartGameActivity extends Activity {
    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }
}
