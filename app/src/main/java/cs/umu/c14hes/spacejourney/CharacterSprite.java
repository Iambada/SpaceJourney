package cs.umu.c14hes.spacejourney;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;


public class CharacterSprite {
    private Bitmap image;
    private Position position;
    private int xVelocity = 10;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public CharacterSprite(Bitmap bitmap) {
        this.image = bitmap;
        position = new Position();
        position.setY(screenHeight-240);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, position.getX(), position.getY(), null);
    }

    public void update() {

        position.setX(position.getX()+xVelocity);
        if ((position.getX() > screenWidth -image.getWidth()) || (position.getX() < 0)){
            xVelocity = xVelocity*-1;
        }
    }

    public Position getPosition() {
        return position;
    }
}
