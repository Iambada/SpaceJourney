package cs.umu.c14hes.spacejourney;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import java.util.ArrayList;


/**
 *
 */
public class MeteorShower {

    private ArrayList<Meteor> meteors;
    private long startTime;

    private Resources resources;
    private float speed;
    private int updateTick;

    public MeteorShower(Resources resources) {

        startTime  = System.currentTimeMillis();

        meteors = new ArrayList<>();
        this.resources = resources;
        speed = 10;
        updateTick = 0;
    }

    public boolean playerCollide(CharacterSprite player) {
        for (Meteor ob : meteors) {
            if (ob.collision(player))
                return true;
        }
        return false;
    }

    public void update() {
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        startTime = System.currentTimeMillis();

        for (Meteor ob : meteors) {
            ob.update();
        }
        if (updateTick % 10 == 0) {
            speed += 1;
            meteors.add(new Meteor(BitmapFactory.decodeResource(resources,
                    R.drawable.meteor), speed));
        }
        updateTick++;

    }

    public void draw(Canvas canvas) {
        for (Meteor ob : meteors)
            ob.draw(canvas);
    }

}