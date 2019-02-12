package cs.umu.c14hes.spacejourney;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.util.Random;

public class Obstacle {
    private Bitmap image;
    private Position position;
    private int yVelocity;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public Obstacle(Bitmap bitmap, int velocity) {
        this.image = bitmap;
        position = new Position();

        position.setX(new Random().nextInt(screenWidth));
        yVelocity = velocity;
        position.setY(0);
    }

    public void draw(Canvas canvas) {

        int x =0;
        if (position.getX()> screenWidth -image.getWidth()) {
            x = screenWidth -image.getWidth();
        } else {
            x = position.getX();
        }
        canvas.drawBitmap(image, x, position.getY(), null);
    }

    public void update() {

        position.setY((position.getY()+yVelocity));
    }

    public Position getPosition() {
        return position;
    }
}
