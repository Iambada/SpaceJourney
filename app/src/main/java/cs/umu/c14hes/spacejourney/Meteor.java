package cs.umu.c14hes.spacejourney;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Meteor {
    private Bitmap image;
    private Position position;
    private float yVelocity;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public Meteor(Bitmap bitmap, float velocity) {
        this.image = bitmap;
        position = new Position();

        position.setX(new Random().nextInt(screenWidth));
        yVelocity = velocity;
        position.setY(0);
    }

    public void draw(Canvas canvas) {

        float x =0;
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

    public boolean collision(CharacterSprite player) {
        Position mooncake = player.getPosition();
        if(Math.abs(position.getX() - mooncake.getX()) < 50) {
            if (Math.abs(position.getY()- mooncake.getY()) < 150){
                return true;
            }
        }
        return false;
    }
}