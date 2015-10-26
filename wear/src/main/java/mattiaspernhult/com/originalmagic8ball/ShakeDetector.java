package mattiaspernhult.com.originalmagic8ball;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by mattiaspernhult on 2015-10-21.
 */
public class ShakeDetector implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 1.7F;
    private static final int SHAKE_WAIT_TIME_MS = 250;

    private long mShakeTime = 0;

    private OnShakeListener mListener;


    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event);
        }

    }

    private void detectShake(SensorEvent event) {

        if (mListener != null) {
            long now = System.currentTimeMillis();

            if ((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
                mShakeTime = now;

                float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
                float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
                float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

                float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

                if (gForce > SHAKE_THRESHOLD) {
                    mListener.onShake();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnShakeListener {
        void onShake();
    }
}
