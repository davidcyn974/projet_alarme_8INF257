package uqac.dim.projet_alarme_8inf257;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Objects;

/**
 * Mini-Game that deactivate the alarm by shaking the phone
 */
public class ShakeActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        ImageView img = (ImageView)findViewById(R.id.phoneDraw);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        img.startAnimation(animation);

        Toast.makeText(getApplicationContext(), "WAKE UP", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "WAKE UP", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "WAKE UP", Toast.LENGTH_SHORT).show();

    }

    /**
     * create a listener to measure the shaking
     */
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if(mAccel > 40) {   // Shaking strong enough
                CommonMyMediaPlayer.player.stopMusic();
                Toast.makeText(getApplicationContext(), "Well done ;)", Toast.LENGTH_LONG).show();

                Intent i = new Intent(ShakeActivity.this, MainActivity.class);
                Bundle bundle  = ActivityOptions.makeSceneTransitionAnimation(ShakeActivity.this).toBundle();
                startActivity(i,bundle);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //nothing to do here
        }
    };

    //Enable & Disable listeners
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}