package zouyun.com.example.whackamole;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.*;
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
    final  Handler handler = new Handler();

//    Timer timer = new Timer();








    Runnable runnableCode = new Runnable() {
        @Override
        public synchronized void run() {
            int moleNumtemp = (int) (Math.random() * 9);
            final int moleNum;

            //no same mole one after another

            if(tempmole!=moleNumtemp){
             moleNum=moleNumtemp;}
            else if (moleNumtemp!=8){
                moleNum=moleNumtemp+1;}
            else{moleNum=0;}




//                        activeMoles[tempmole]=false;
//                        moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
                        activeMoles[moleNum]=true;
//                        score.setText(Integer.toString(player.getPoint()));
                        score.setText("Score: "+Integer.toString(player.getPoint())+" ,Active Mole at"+moleNum);
//                        ObjectAnimator animation = new ObjectAnimator();
//                        moles.get(moleNum).animate().translationYBy(-250).setDuration(1000);

                        // 0.04 s to rise up a
                        // nd go down, 0.5 s stay there
                        final Runnable endAction2 = new Runnable() {
                            public void run() {
                                moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
                            }
                        };
                        Runnable endAction = new Runnable() {
                            public void run() {
                                moles.get(moleNum).animate().translationYBy(250).setDuration(300).withEndAction(endAction2).setStartDelay(500);

                            }
                        };


                        moles.get(moleNum).animate().translationYBy(-250).setDuration(300).withEndAction(endAction);

            tempmole=moleNum;


            handler.postDelayed(runnableCode, 1000);

        }


    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,
                String.format("fonts/MoonFlowerBold.ttf"));


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
        score.setTypeface(typeface);
        moles.add(m1);
        moles.add(m2);
        moles.add(m3);
        moles.add(m4);
        moles.add(m5);
        moles.add(m6);
        moles.add(m7);
        moles.add(m8);
        moles.add(m9);

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
        handler.post(runnableCode);


//        timer.scheduleAtFixedRate(new TimerTask() {
//
//            @Override
//            public void run() {
//
//            //TODO: Fix runtime setClickale problem
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final int moleNum = (int) (Math.random() * 9);
////                        activeMoles[tempmole]=false;
//                        moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//                        activeMoles[moleNum]=true;
////                        score.setText(Integer.toString(player.getPoint()));
//                        score.setText("Score: "+Integer.toString(player.getPoint()));
////                        ObjectAnimator animation = new ObjectAnimator();
////                        moles.get(moleNum).animate().translationYBy(-250).setDuration(1000);
//
//                        // 0.04 s to rise up a
//                        // nd go down, 0.5 s stay there
//                        final Runnable endAction2 = new Runnable() {
//                            public void run() {
//                                moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//                            }
//                        };
//                        Runnable endAction = new Runnable() {
//                            public void run() {
//                                moles.get(moleNum).animate().yBy(250).setDuration(500).withEndAction(endAction2);
//
//                            }
//                        };
//
//
//                        moles.get(moleNum).animate().yBy(-250).setDuration(500).withEndAction(endAction);
//
//
//
//                    }
//                });
//
//            }
//        },1000,1500);


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
                    System.out.println("finalI is " + finalI);
                    System.out.println("activeMoles[finalI] is" + activeMoles[finalI]);

                    if(activeMoles[finalI]){

                    player.hitMole();
                        moles.get(finalI1).setImageDrawable(getResources().getDrawable(R.drawable.game_sadmole));
                        activeMoles[finalI]=false;


                    }

                }
            });
        }
    }


//TODO:BEAUTIFY THE SCORE AND HEALTH DISPLAY
    //TODO:Mole onClick --> sadMole



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

