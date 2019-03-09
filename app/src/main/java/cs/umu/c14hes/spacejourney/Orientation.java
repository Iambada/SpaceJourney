package cs.umu.c14hes.spacejourney;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;


/**
 * Created by Jonny on 2018-03-17.
 * Class for recognizing the tilt functionality and using that to move the player.
 */

public class Orientation implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;

    private float[] accelOutput;
    private float[] magOutput;
    private float[] orientation= new float[3];
    private float[] startOrientation = null;

    public Orientation(){
        manager = (SensorManager) Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    public void register(){
        manager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this,magnometer,SensorManager.SENSOR_DELAY_GAME);
    }
    public void pause(){
        manager.unregisterListener(this);
    }

    public float[] getOrientation(){
        return orientation;
    }
    public float[] getStartrOrientation(){
        return startOrientation;
    }
    public void newGame(){
        startOrientation = null;
    }


    /**
     * Detecting gyroscope/tilting change and how much the phone are tilting to determine the speed
     * of which the player should move.
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelOutput = sensorEvent.values;
        }else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magOutput = sensorEvent.values;
        }
        if (accelOutput != null && magOutput != null){
            float[] R = new float[9];
            float[] I = new float[9];
            Boolean success = SensorManager.getRotationMatrix(R,I,accelOutput,magOutput);
            if (success){
                SensorManager.getOrientation(R,orientation);
                if (startOrientation == null) {
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation,0,startOrientation,0,orientation.length);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }
}
