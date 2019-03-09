package cs.umu.c14hes.spacejourney;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;


public class CharacterSprite {
    private Bitmap image;
    private Position position;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public CharacterSprite(Bitmap bitmap) {
        this.image = bitmap;
        position = new Position();
        position.setY(screenHeight-240);
        position.setX((Constants.SCREEN_WIDTH - image.getWidth())/2);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, position.getX(), position.getY(), null);
    }

    public void update() {
        if ((position.getX() > Constants.SCREEN_WIDTH -image.getWidth())) {
            position.setX(Constants.SCREEN_WIDTH - image.getWidth());
        } else if (position.getX() < 0){
            position.setX(0);
        }
    }

    public Position getPosition() {
        return position;
    }
}
