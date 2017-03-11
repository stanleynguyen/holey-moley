package zouyun.com.example.whackamole;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {
    ArrayList<ImageView> moles = new ArrayList<>();
    public static boolean isComplete = false;
    public final PlayerThread player = new PlayerThread();
    private ImageView m1, m2, m3, m4, m5, m6, m7, m8, m9;
    public TextView score;
    final Handler handler = new Handler();
    // Define the code block to be executed
    Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            int moleNum = (int) (Math.random() * 9);
            moles.get(moleNum).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.hitMole();
                    System.out.println(player.getPoint());
                }
            });
            //TODO: Fix runtime setClickale problem

            score.setText(Integer.toString(player.getPoint()));
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -250);
            animation.setDuration(3000);
            if (!animation.hasEnded()){
                moles.get(moleNum).setClickable(true);
            }
            else{
                moles.get(moleNum).setClickable(false);
            }
            moles.get(moleNum).setAnimation(animation);


            TranslateAnimation animation2 = new TranslateAnimation(0, 0, -250, 0);
            animation2.setDuration(1000);
            if (!animation2.hasEnded()){
                moles.get(moleNum).setClickable(true);
            }
            else{
                moles.get(moleNum).setClickable(false);
            }

            moles.get(moleNum).setAnimation(animation2);

//            moles.get(moleNum).setClickable(false);
            Log.d("Handlers", "Called on main thread");
            // Repeat this the same runnable code block again another 2 seconds
            handler.postDelayed(runnableCode, 500);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private ImageView m2=(ImageView)findViewById(R.id.mole2);
//    private ImageView m3=(ImageView)findViewById(R.id.mole3);
//    private ImageView m4=(ImageView)findViewById(R.id.mole4);
//    private ImageView m5=(ImageView)findViewById(R.id.mole5);
//    private ImageView m6=(ImageView)findViewById(R.id.mole6);
//    private ImageView m7=(ImageView)findViewById(R.id.mole7);
//    private ImageView m8=(ImageView)findViewById(R.id.mole8);
//    private ImageView m9=(ImageView)findViewById(R.id.mole9);


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        m1 = (ImageView) findViewById(R.id.mole1);
        m2 = (ImageView) findViewById(R.id.mole2);
        m3 = (ImageView) findViewById(R.id.mole3);
        m4 = (ImageView) findViewById(R.id.mole4);
        m5 = (ImageView) findViewById(R.id.mole5);
        m6 = (ImageView) findViewById(R.id.mole6);
        m7 = (ImageView) findViewById(R.id.mole7);
        m8 = (ImageView) findViewById(R.id.mole8);
        m9 = (ImageView) findViewById(R.id.mole9);
        score = (TextView) findViewById(R.id.score);
        moles.add(m1);
        moles.add(m2);
        moles.add(m3);
        moles.add(m4);
        moles.add(m5);
        moles.add(m6);
        moles.add(m7);
        moles.add(m8);
        moles.add(m9);


//        Toast.makeText(this.getApplicationContext(),"hello1",Toast.LENGTH_SHORT).show();

        start();
        handler.post(runnableCode);


//        //timer for 1 minute
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        }, 5000);


    }

    // Start the initial runnable task by posting through the handler
//TODO:TIMER
    //TODO:show point
    void start() {


        player.start();
        for (ImageView mole : moles) {
            mole.setClickable(true);
//            mole.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // do something when the corky is clicked
//                }
//            });
        }
    }

    void MolePopOut() {
        int moleNum = (int) (Math.random() * 9);
        moles.get(moleNum).setClickable(true);
        moles.get(moleNum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.hitMole();
                System.out.println(player.getPoint());
            }
        });
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -250);
        animation.setDuration(8000);
        animation.setFillAfter(true);
        //TODO:SOMETIMES NOT CLICKABLE

        moles.get(moleNum).setAnimation(animation);

        TranslateAnimation animation2 = new TranslateAnimation(0, 0, -250, 0);
        animation2.setDuration(1000);
        animation2.setFillAfter(true);
        moles.get(moleNum).setAnimation(animation2);
        moles.get(moleNum).setClickable(false);


    }


    void gameOver() {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    ///

//    class OpponentThread extends Thread{
//        private int point;
//        private int energy;
//        private int health;
//        private int status;
//        public OpponentThread(){
//            this.point=0;
//            this.energy=0;
//            this.health=3;
//        }
//
//
//
//        @Override
//        public void run() {
//
////            for(ImageView mole: moles){
////                mole.setOnClickListener();
////            }
//
//            if(this.health==0){
//                this.status=-1;
//                System.out.println("You died");
//            }
//
//        }
//        int getHealth() {
//            return health;
//        }
//        int getEnergy() {
//            return energy;
//        }
//        int getPoint() {
//            return points;
//        }
//
//
//
//        void hitMole() {
//            gainPoint(1);
//            gainEnergy(20);
//        }
//
//        void kenaBomb() {
//            deductHealth(1);
//        }
//
//
//        ///////////////////////
//        /// utility functions
//        /////////////////////
//
//        void deductHealth(int amount) {
//            this.health -= amount;
//        }
//
//        void gainEnergy(int amount) {
//            if (this.energy + amount > 100) { this.energy = 100; }
//            else { this.energy += amount; }
//        }
//
//        void gainPoint(int amount) {
//            points += amount;
//        }
//
//        void deductPoint(int amount) {
//            points -= amount;
//        }
//    }
//    ///

}

class PlayerThread extends Thread{
    private int point;
    private int energy;
    private int health;
    private int status;

    public PlayerThread(){
        this.point=0;
        this.energy=0;
        this.health=3;
    }



    @Override
    public void run() {

        while (true) {
            if (this.health == 0) {
                this.status = -1;
                System.out.println("You died");
            }
        }

    }
    int getHealth() {
        return health;
    }
    int getEnergy() {
        return energy;
    }
    int getPoint() {
        return point;
    }



    void hitMole() {


        gainPoint(1);
        gainEnergy(20);
    }

    void kenaBomb() {
        deductHealth(1);
    }


    ///////////////////////
    /// utility functions
    /////////////////////

    void deductHealth(int amount) {
        this.health -= amount;
    }

    void gainEnergy(int amount) {
        if (this.energy + amount > 100) { this.energy = 100; }
        else { this.energy += amount; }
    }

    void gainPoint(int amount) {
        point += amount;
    }

    void deductPoint(int amount) {
        point -= amount;
    }
}

