package mattiaspernhult.com.originalmagic8ball;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMagicBall = (ImageView) findViewById(R.id.magic_ball);
        mShake = AnimationUtils.loadAnimation(this, R.anim.shake_anim);

        mRandom = new Random();

        registerAnimationListener();

        mMagicBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMagicBall.startAnimation(mShake);
            }
        });

    }

    private void registerAnimationListener() {
        mShake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                int random = mRandom.nextInt(100) % 3;
                if (random == 0) {
                    mAnswer = mPositive[mRandom.nextInt(mPositive.length)];
                } else if (random == 1) {
                    mAnswer = mNegative[mRandom.nextInt(mNegative.length)];
                } else {
                    mAnswer = mNeutral[mRandom.nextInt(mNeutral.length)];
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(MainActivity.this, mAnswer, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
