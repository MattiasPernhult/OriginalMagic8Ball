package mattiaspernhult.com.originalmagic8ball;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends WearableActivity implements ShakeDetector.OnShakeListener {

    private ImageView mMagicBall;
    private Animation mShake;
    private String mAnswer;

    private String[] mPositive = {"It is certain", "It is decidedly so", "Without a doubt",
            "Yes, definitely", "You may rely on it", "As I see it, yes", "Most likely",
            "Outlook good", "Yes", "Signs point to yes"};
    private String[] mNegative = {"Don't count on it", "My reply is no",
            "My sources say no", "Outlook not so good", "Very doubtful"};
    private String[] mNeutral = {"Reply hazy try again", "Ask again later",
            "Better not tell you now", "Cannot predict now", "Concentrate and ask again"};

    private Random mRandom;
    private int resourceId;
    private boolean mShakeIsOk;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMagicBall = (ImageView) findViewById(R.id.magic_ball);
        mShake = AnimationUtils.loadAnimation(this, R.anim.shake_anim);

        mShakeIsOk = true;

        mRandom = new Random();

        registerAnimationListener();

        mMagicBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMagicBall.startAnimation(mShake);
            }
        });

        setAmbientEnabled();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    private void registerAnimationListener() {
        mShake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mMagicBall.setImageResource(R.drawable.eight_ball_192);
                mShakeIsOk = false;
                int random = mRandom.nextInt(100) % 3;
                if (random == 0) {
                    mAnswer = mPositive[mRandom.nextInt(mPositive.length)];
                } else if (random == 1) {
                    mAnswer = mNegative[mRandom.nextInt(mNegative.length)];
                } else {
                    mAnswer = mNeutral[mRandom.nextInt(mNeutral.length)];
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

            @Override
            public void onAnimationEnd(Animation animation) {
                mMagicBall.setImageResource(resourceId);
                mShakeIsOk = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onShake() {
        if (mShakeIsOk) {
            mMagicBall.startAnimation(mShake);
        }
    }
}
