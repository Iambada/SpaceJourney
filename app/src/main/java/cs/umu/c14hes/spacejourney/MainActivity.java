package cs.umu.c14hes.spacejourney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //setContentView(new GameView(this));
    }

    public void onClickedNewGame(View view) {

        Intent gameIntent = new Intent(this, StartGameActivity.class);
        startActivity(gameIntent);
    }
    public void onClickedHowToPlay (View view) {

        Intent htpIntent = new Intent(this, UserGuideActivity.class);
        startActivity(htpIntent);
    }
}
