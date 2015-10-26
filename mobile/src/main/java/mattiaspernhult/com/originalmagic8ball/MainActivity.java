package mattiaspernhult.com.originalmagic8ball;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private boolean hasShaked;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private int resourceId;

    private String whatToGenerate;

    private static final String GENERATE = "mattiaspernhult.com.generate";
    private static final String GENERATE_POS = "mattiaspernhult.com.generate_pos";
    private static final String GENERATE_NEG = "mattiaspernhult.com.generate_neg";
    private static final String GENERATE_NEU = "mattiaspernhult.com.generate_neu";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMagicBall = (ImageView) findViewById(R.id.magic_ball);
        mShake = AnimationUtils.loadAnimation(this, R.anim.anim_shake_2);
        initializeRotationAnimation();
        registerAnimationListener();
        mIsShakeOk = true;
        hasShaked = false;
        whatToGenerate = GENERATE;

        setClickListener();

        mAnimationCount = 0;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(this);

    }

    private void setClickListener() {
        mMagicBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasShaked) {
                    mMagicBall.startAnimation(mRotate);
                } else {
                    Toast.makeText(MainActivity.this, "You need to shake before " +
                            "you can see an answer...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initializeRotationAnimation() {
        mRotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);
        mRotate.setInterpolator(new LinearInterpolator());
        mRotate.setRepeatCount(2);
        mRotate.setDuration(500);
    }

    private void decideWhichId() {
        if (mAnswer == null) {
            mAnswer = "Ask again later";
        }
        switch (mAnswer) {
            case "It is certain":
                resourceId = R.drawable.eight_ball_it_is_certain_192;
                break;
            case "It is decidedly so":
                resourceId = R.drawable.eight_ball_it_is_decidedly_so_192;
                break;
            case "Without a doubt":
                resourceId = R.drawable.eight_ball_without_a_doubt_192;
                break;
            case "Yes, definitely":
                resourceId = R.drawable.eight_ball_yes_definitely_192;
                break;
            case "As I see it, yes":
                resourceId = R.drawable.eight_ball_as_i_see_it_yes_192;
                break;
            case "Most likely":
                resourceId = R.drawable.eight_ball_most_likely_192;
                break;
            case "Outlook good":
                resourceId = R.drawable.eight_ball_outlook_good_192;
                break;
            case "Yes":
                resourceId = R.drawable.eight_ball_yes_192;
                break;
            case "Signs point to yes":
                resourceId = R.drawable.eight_ball_signs_point_to_yes_192;
                break;
            case "Reply hazy try again":
                resourceId = R.drawable.eight_ball_reply_hazy_try_again_192;
                break;
            case "Ask again later":
                resourceId = R.drawable.eight_ball_ask_again_later_192;
                break;
            case "Cannot predict now":
                resourceId = R.drawable.eight_ball_cannot_predict_now_192;
                break;
            case "Concentrate and ask again":
                resourceId = R.drawable.eight_ball_concentrate_and_ask_again_192;
                break;
            case "Don't count on it":
                resourceId = R.drawable.eight_ball_dont_count_on_it_192;
                break;
            case "My reply is no":
                resourceId = R.drawable.eight_ball_my_reply_is_no_192;
                break;
            case "My sources say no":
                resourceId = R.drawable.eight_ball_my_sources_says_no_192;
                break;
            case "Very doubtful":
                resourceId = R.drawable.eight_ball_very_doubtful_192;
                break;
            default:
                resourceId = R.drawable.eight_ball_ask_again_later_192;
                break;
        }
    }

    private void registerAnimationListener() {
        mShake.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(MainActivity.this, "Your answer is ready, press the magic ball", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mMagicBall.setImageResource(R.drawable.eight_ball_192);
                decideWhichId();
                mIsShakeOk = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimationCount = 0;
                animation.setDuration(500);
                mIsShakeOk = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setDuration(animation.getDuration() + 100);
                if (mAnimationCount == 1) {
                    mMagicBall.setImageResource(resourceId);
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

        switch (id) {
            case R.id.action_generate:
                whatToGenerate = GENERATE;
                break;
            case R.id.action_generate_positive:
                whatToGenerate = GENERATE_POS;
                break;
            case R.id.action_generate_negative:
                whatToGenerate = GENERATE_NEG;
                break;
            case R.id.action_generate_neutral:
                whatToGenerate = GENERATE_NEU;
                break;

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
            hasShaked = true;
            new MagicTask().execute(whatToGenerate);
        }
    }

    private class MagicTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            mMagicBall.setImageResource(R.drawable.eight_ball_192);
            mMagicBall.startAnimation(mShake);
        }

        @Override
        protected String doInBackground(String... params) {
            String choice = params[0];
            String request;
            switch (choice) {
                case GENERATE:
                    request = "https://magic-8-ball-api.herokuapp.com/generate";
                    break;
                case GENERATE_POS:
                    request = "https://magic-8-ball-api.herokuapp.com/generate/positive";
                    break;
                case GENERATE_NEG:
                    request = "https://magic-8-ball-api.herokuapp.com/generate/negative";
                    break;
                case GENERATE_NEU:
                    request = "https://magic-8-ball-api.herokuapp.com/generate/neutral";
                    break;
                default:
                    request = "https://magic-8-ball-api.herokuapp.com/generate";
                    break;
            }
            String result = HttpManager.getData(request);
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
