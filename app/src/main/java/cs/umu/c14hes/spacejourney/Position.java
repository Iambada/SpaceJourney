package cs.umu.c14hes.spacejourney;

import android.os.Parcel;
import android.os.Parcelable;

public class Position {
    private float x;
    private float y;
    public Position(){
        x = 0;
        y = 0;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}