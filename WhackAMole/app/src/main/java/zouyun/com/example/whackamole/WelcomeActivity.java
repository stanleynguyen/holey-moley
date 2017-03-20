package zouyun.com.example.whackamole;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

    private ProgressBar startProgressBar;
    private ImageView startButton;
    private TextView tapToStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        startProgressBar = (ProgressBar) findViewById(R.id.startProgressBar);
        startButton = (ImageView) findViewById(R.id.startButton);
        tapToStart = (TextView) findViewById(R.id.tapToStart);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/bignoodletitling.ttf");
        tapToStart.setTypeface(custom_font);

//        tapToStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                this.finish();
//            }
//
//            private void finish() {
//            }
//        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(WelcomeActivity.this, TabsActivity.class));
                this.finish();
            }

            private void finish() {
            }
        });


//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_anim);
//        imageView.setAnimation(animation);

//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                finish();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startProgressBar.setVisibility(View.INVISIBLE);
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                this.finish();
                startButton.setVisibility(View.VISIBLE);
                tapToStart.setVisibility(View.VISIBLE);

            }



        }, 5000);
    }
}
