package cs.umu.c14hes.spacejourney;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Intent gameIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onStart() {
        super.onStart();
        TextView highScore = (TextView) findViewById(R.id.highScore);
        gameIntent = new Intent(this, StartGameActivity.class);
        SharedPreferences settings = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        int score = settings.getInt("HIGH_SCORE", 0);
        highScore.setText("High Score : " + score);
    }
    public void onClickedNewGame(View view) {
        Intent gameIntent = new Intent(this, StartGameActivity.class);
        startActivity(gameIntent);
    }

    public void onClickedResume(View view) {
        startActivity(gameIntent);
    }

    public void onClickedHowToPlay (View view) {

        Intent htpIntent = new Intent(this, UserGuideActivity.class);
        startActivity(htpIntent);
    }
}
