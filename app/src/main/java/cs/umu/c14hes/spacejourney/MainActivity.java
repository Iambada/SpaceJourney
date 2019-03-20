package cs.umu.c14hes.spacejourney;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
        gameIntent = new Intent(this, StartGameActivity.class);
        startActivity(gameIntent);
    }

    public void onClickedHowToPlay (View view) {

        Intent htpIntent = new Intent(this, UserGuideActivity.class);
        startActivity(htpIntent);
    }

    public void onClickedAbout (View view) {

        Intent aboutIntent = new Intent(this, About.class);
        startActivity(aboutIntent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("MainActivity", "Back button pressed");

        //startActivity(new Intent(this, MainActivity.class));
    }
}
