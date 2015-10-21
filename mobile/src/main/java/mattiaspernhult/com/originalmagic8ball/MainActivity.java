package mattiaspernhult.com.originalmagic8ball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ShakeDetector.OnShakeListener {

    private ImageView mMagicBall;
    private Animation mShake;
    private Animation mRotate;
    private String mAnswer;
    private int mAnimationCount;
    private boolean mIsShakeOk;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private int test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMagicBall = (ImageView) findViewById(R.id.magic_ball);
        mShake = AnimationUtils.loadAnimation(this, R.anim.anim_shake_2);
        initializeRotationAnimation();
        registerAnimationListener();
        mIsShakeOk = true;

        mAnimationCount = 0;

        mMagicBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswer != null) {
                    mMagicBall.startAnimation(mRotate);
                } else {
                    Toast.makeText(MainActivity.this, "You need to shake before " +
                            "you can see an answer...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(this);

    }

    private void initializeRotationAnimation() {
        mRotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);
        mRotate.setInterpolator(new LinearInterpolator());
        mRotate.setRepeatCount(2);
        mRotate.setDuration(500);
    }

    private void registerAnimationListener() {
        mShake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(MainActivity.this, "Your answer is ready press, press the magic ball", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("MainActivity", "Animation börjar");
                mMagicBall.setImageResource(R.mipmap.magic_ball);
                mIsShakeOk = false;
                test = 0;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("MainActivity", "Animation slutade");
                mAnimationCount = 0;
                animation.setDuration(500);
                mIsShakeOk = true;
                Log.d("MainActivity", "Totala tiden: " + test);
                Toast.makeText(MainActivity.this, mAnswer, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                test += animation.getDuration();
                Log.d("MainActivity", "Animation repeteras " + mAnimationCount);
                Log.d("MainActivity", "duration: " + animation.getDuration());
                animation.setDuration(animation.getDuration() + 100);
                if (mAnimationCount == 1) {
                    mMagicBall.setImageResource(R.drawable.ball);
                }
                mAnimationCount++;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public void onShake(int count) {
        if (mIsShakeOk) {
            new MagicTask().execute();
        }
    }

    private class MagicTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            mMagicBall.setImageResource(R.mipmap.magic_ball);
            mMagicBall.startAnimation(mShake);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = HttpManager.getData("https://magic-8-ball-api.herokuapp.com/generate");
            try {
                JSONObject jsonObject = new JSONObject(result);
                String answer = jsonObject.getString("answer");
                return answer;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            mAnswer = s;
        }
    }
}
