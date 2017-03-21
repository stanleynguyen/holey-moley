package zouyun.com.example.whackamole;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
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
//TODO: get number from server
//TODO: send mena data to the server


public class MainActivity extends AppCompatActivity {
    ArrayList<ImageView> moles = new ArrayList<>();
    ArrayList<ImageView> sadmoles= new ArrayList<>();
    public static boolean isComplete = false;
    public final PlayerThread player = new PlayerThread();
    private ImageView m1, m2, m3, m4, m5, m6, m7, m8, m9,bm1,bm2,bm3,bm4,bm5,bm6,bm7,bm8,bm9;//moles
    private ImageView sm1,sm2,sm3,sm4,sm5,sm6,sm7,sm8,sm9;//sadmoles
    private ImageView bomb,deadbomb,freeze,deadfreeze,health,deadhealth;
    private ImageView h1,h2,h3,h4;//hearts
    public TextView score;
    public boolean[] activeMoles=new boolean[9];
    public int tempmole=0;

    Timer timer = new Timer();




//    final Handler handler = new Handler();
//
//    Runnable runnableCode = new Runnable() {
//        @Override
//        public void run() {
//            handler.postDelayed(runnableCode, 1000);
//
//
//            final int moleNum = (int) (Math.random() * 9);
//            activeMoles[tempmole]=false;
//            activeMoles[moleNum]=true;
//            //TODO: Fix runtime setClickale problem
//            score.setText(Integer.toString(player.getPoint()));
//            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -250);
//            animation.setDuration(1000);// 0.04 s to rise up and go down, 0.5 s stay there
//            TranslateAnimation animation2 = new TranslateAnimation(0, 0, -250, 0);
//            animation2.setDuration(1000);
//
////            if (!animation.hasEnded()||!animation2.hasEnded()){
////                moles.get(moleNum).setClickable(true);
////            }
////            else {
////                moles.get(moleNum).setClickable(false);
////            }
//
//
//            moles.get(moleNum).setAnimation(animation);
////            moles.get(moleNum).setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    if(activeMoles[moleNum]){
////                        moles.get(moleNum).setVisibility(View.INVISIBLE);
////                        sadmoles.get(moleNum).setVisibility(View.VISIBLE);
////                        player.hitMole();
////
////                    }
////
////                }
////            });
//
//            moles.get(moleNum).setAnimation(animation2);
//            if(animation2.hasEnded()){
//                moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//            }
//
//            tempmole=moleNum;
//
////            activeMoles[moleNum]=false;
////            moles.get(moleNum).setClickable(false);
//            System.out.println("active mole is" +moleNum);
////            Log.d("Handlers", "Called on main thread");
//            // Repeat this the same runnable code block again another 2 seconds
//
//        }
//
//    };
//




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
//        sm1 = (ImageView) findViewById(R.id.mole10);
//        sm2 = (ImageView) findViewById(R.id.mole11);
//        sm3 = (ImageView) findViewById(R.id.mole12);
//        sm4 = (ImageView) findViewById(R.id.mole13);
//        sm5 = (ImageView) findViewById(R.id.mole14);
//        sm6 = (ImageView) findViewById(R.id.mole15);
//        sm7 = (ImageView) findViewById(R.id.mole16);
//        sm8 = (ImageView) findViewById(R.id.mole17);
//        sm9 = (ImageView) findViewById(R.id.mole18);
//        sadmoles.add(sm1);
//        sadmoles.add(sm2);
//        sadmoles.add(sm3);
//        sadmoles.add(sm4);
//        sadmoles.add(sm5);
//        sadmoles.add(sm6);
//        sadmoles.add(sm7);
//        sadmoles.add(sm8);
//        sadmoles.add(sm9);
        bomb=(ImageView) findViewById(R.id.bomb);
        deadbomb=(ImageView) findViewById(R.id.deadbomb);
        freeze=(ImageView) findViewById(R.id.freeze);
        deadfreeze=(ImageView) findViewById(R.id.deadfreeze);
        health=(ImageView) findViewById(R.id.health);
        deadhealth=(ImageView) findViewById(R.id.deadhealth);
        h1=(ImageView) findViewById(R.id.heart1);
        h2=(ImageView) findViewById(R.id.heart2);
        h3=(ImageView) findViewById(R.id.heart3);
        h4=(ImageView) findViewById(R.id.heart4);

//        Toast.makeText(this.getApplicationContext(),"hello1",Toast.LENGTH_SHORT).show();




        start();
        bomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int id = getBaseContext().getResources().getIdentifier("game_deadbomb", "drawable", getPackageName());
//                bomb.setImageDrawable(getResources().getDrawable(R.drawable.game_deadbomb));
//                bomb.setImageResource(id);
                bomb.setVisibility(View.GONE);
                deadbomb.setVisibility(View.VISIBLE);

            }
        });
        freeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeze.setVisibility(View.GONE);
                deadfreeze.setVisibility(View.VISIBLE);
            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                health.setVisibility(View.GONE);
                h4.setVisibility(View.VISIBLE);//Need to change so that it can check once there is less than 3 lifes
                deadhealth.setVisibility(View.VISIBLE);
            }
        });
//        handler.post(runnableCode);


        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

            //TODO: Fix runtime setClickale problem
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final int moleNum = (int) (Math.random() * 9);
//                        activeMoles[tempmole]=false;

                        activeMoles[moleNum]=true;
//                        score.setText(Integer.toString(player.getPoint()));
                        score.setText("active mole is: "+Integer.toString(moleNum)+" score is "+Integer.toString(player.getPoint()));
                        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -250);
                        animation.setDuration(1000);// 0.04 s to rise up and go down, 0.5 s stay there
                        TranslateAnimation animation2 = new TranslateAnimation(0, 0, -250, 0);
                        animation2.setDuration(1000);

//            if (!animation.hasEnded()||!animation2.hasEnded()){
//                moles.get(moleNum).setClickable(true);
//            }
//            else {
//                moles.get(moleNum).setClickable(false);
//            }


                        moles.get(moleNum).setAnimation(animation);
//            moles.get(moleNum).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(activeMoles[moleNum]){
//                        moles.get(moleNum).setVisibility(View.INVISIBLE);
//                        sadmoles.get(moleNum).setVisibility(View.VISIBLE);
//                        player.hitMole();
//
//                    }
//
//                }
//            });
                        System.out.println("active mole is" +moleNum);
                        moles.get(moleNum).setAnimation(animation2);
                        if(animation2.hasEnded()){
                            moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
                        }



//            activeMoles[moleNum]=false;
//            moles.get(moleNum).setClickable(false);



                    }
                });


            }
        }, 1000, 1000);


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
    //TODO:TOOLS
    synchronized void start() {


        player.start();
        h4.setVisibility(View.INVISIBLE);// the extra heart will only be there if the user click the heart
        deadbomb.setVisibility(View.INVISIBLE);
        deadfreeze.setVisibility(View.INVISIBLE);
        deadhealth.setVisibility(View.INVISIBLE);

        for (int i=0;i<9;i++) {
//            sadmoles.get(i).setVisibility(View.INVISIBLE);
            final int finalI = i;
            final int finalI1 = i;
            moles.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activeMoles[finalI]){


//                        moles.get(finalI1).setVisibility(View.INVISIBLE);
//                    sadmoles.get(finalI1).setVisibility(View.VISIBLE);
                    player.hitMole();
                        moles.get(finalI1).setImageDrawable(getResources().getDrawable(R.drawable.game_sadmole));
                        activeMoles[finalI]=false;

//                        moles.get(finalI1).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));

//                        tempmole=finalI1;

                    }

                }
            });
        }
    }

    //trying without a listener
//    public void OnClick(View v){
//        int id=v.getId();
//        switch(id){
//            case R.id.mole1:
//                m1.setVisibility(View.INVISIBLE);
////                sm1.setVisibility(View.VISIBLE);
//                break;
//            case R.id.mole2:
//                m2.setVisibility(View.INVISIBLE);
////                sm2.setVisibility(View.VISIBLE);
//                break;
//            default:
//                break;
//        }
//
//
//    }
//TODO:BEAUTIFY THE SCORE AND HEALTH DISPLAY
    //TODO:Mole onClick --> sadMole
//    void MolePopOut() {
//        int moleNum = (int) (Math.random() * 9);
//        moles.get(moleNum).setClickable(true);
//        moles.get(moleNum).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                player.hitMole();
//                System.out.println(player.getPoint());
//            }
//        });
//        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -250);
//        animation.setDuration(8000);
//        animation.setFillAfter(true);
//
//
//        moles.get(moleNum).setAnimation(animation);
//
//        TranslateAnimation animation2 = new TranslateAnimation(0, 0, -250, 0);
//        animation2.setDuration(1000);
//        animation2.setFillAfter(true);
//        moles.get(moleNum).setAnimation(animation2);
//        moles.get(moleNum).setClickable(false);
//
//
//    }


    void gameOver() {

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

