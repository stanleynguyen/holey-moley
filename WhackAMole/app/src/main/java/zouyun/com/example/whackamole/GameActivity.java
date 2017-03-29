package zouyun.com.example.whackamole;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.*;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

import io.socket.backo.Backoff;
import io.socket.client.IO;
import io.socket.emitter.Emitter;
import io.socket.parser.Packet;
import io.socket.parser.Parser;
import io.socket.thread.EventThread;
//TODO:get player status (tools the play bought) from server at the start and show the icon accordingly
//TODO: get mole number for popping out from server at runtime, get opponent data at run time
//TODO: send mena data to the server
//TODO:implement bomb mole and set OnclickListener that relates to the heart displayed
//TODO:implement timer to time the game
//TODO:display game over when time is up and show score



//TODO:bugs to fix:pressing on static mole still will change mole(esp when the time interval for changing mole is fast)
//TODO: optimise and enable faster mole popping and response(currently 0.04s for moving up.down mess things up)

//TODO:adjust the size of the sadmole
//TODO:add sound when pressing
//TODO: progress bar or something



public class GameActivity extends AppCompatActivity {
    public static ArrayList<ImageView> moles = new ArrayList<>();
    ArrayList<ImageView> sadmoles= new ArrayList<>();
    public static boolean isComplete = false;
    public final PlayerThread player = new PlayerThread(this.getBaseContext());
    private ImageView m1, m2, m3, m4, m5, m6, m7, m8, m9,bm1,bm2,bm3,bm4,bm5,bm6,bm7,bm8,bm9;//moles
    private ImageView sm1,sm2,sm3,sm4,sm5,sm6,sm7,sm8,sm9;//sadmoles
    private ImageView bomb,deadbomb,freeze,deadfreeze,health,deadhealth;
    private ImageView h1,h2,h3,h4;//hearts
    public TextView score,timeLeft;
    public static boolean[] activeMoles=new boolean[9];
    public int tempmole=0;
    public int moleNumtemp;
    public io.socket.client.Socket socket = null;
    public String item=null;
    public int oppoentScore=0;
//    final  Handler handler = new Handler();

    Timer timer = new Timer();









//    Runnable runnableCode = new Runnable() {
//        @Override
//        public synchronized void run() {
//            int moleNumtemp = (int) (Math.random() * 9);
//            final int moleNum;
//
//            //no same mole consecutively
//            if(tempmole!=moleNumtemp){
//                moleNum=moleNumtemp;}
//            else if (moleNumtemp!=8){
//                moleNum=moleNumtemp+1;}
//            else{moleNum=0;}
//
//
//
//
////                        activeMoles[tempmole]=false;
////                        moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//            /////
//            activeMoles[moleNum]=true;
//            /////
////                        score.setText(Integer.toString(player.getPoint()));
//
////                        ObjectAnimator animation = new ObjectAnimator();
////                        moles.get(moleNum).animate().translationYBy(-250).setDuration(1000);
//
//            // 0.04 s to rise up a
//            // nd go down, 0.5 s stay there
//
//            final Runnable endAction2 = new Runnable() {
//                public void run() {
//                    moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//
//                }
//            };
//            Runnable endAction = new Runnable() {
//                public void run() {
//                    moles.get(moleNum).animate().translationYBy(250).setDuration(500).withEndAction(endAction2).setStartDelay(500);
//
//                }
//            };
//
//
//            moles.get(moleNum).animate().translationYBy(-250).setDuration(500).withEndAction(endAction);
////            moles.get(moleNum).setClickable(false);
//            moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//            score.setText("Score: "+Integer.toString(player.getPoint()));
//
//            tempmole=moleNum;
//
//            handler.postDelayed(runnableCode, 50);
//
//        }
//
//
//    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface1 = Typeface.createFromAsset(am,
                String.format("fonts/moonflowerbold.ttf"));
        Typeface typeface2 = Typeface.createFromAsset(am,
                String.format("fonts/big_noodle_titling.ttf"));


        setContentView(R.layout.activity_game);

        try {
            socket = IO.socket("http://holeymoley.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        socket.emit("join");
        final GameActivity _this = this;
        socket.on("start", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
//
                System.out.println("connecting");
//                        Toast.makeText(_this.getApplicationContext(),"hello1",Toast.LENGTH_SHORT).show();
            }
        });
        socket.on("mole", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                moleNumtemp=Integer.parseInt(args[0].toString());
                System.out.println(args[0].toString());
            }
        });//
        socket.on("item", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                item=args[0].toString();
                System.out.println(args[0].toString());
            }
        });
        socket.on("score", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                oppoentScore++;
                System.out.println(args[0].toString());
            }
        });

        socket.connect();


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
        timeLeft=(TextView) findViewById(R.id.time);
        score.setTypeface(typeface2);
        timeLeft.setTypeface(typeface1);
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
        //        //timer for 1 minute
//        handler.post(runnableCode);



////this part is for moles popping
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        int moleNumtemp = (int) (Math.random() * 9);
            final int moleNum;

            //no same mole consecutively
            if(tempmole!=moleNumtemp){
                moleNum=moleNumtemp;}
            else if (moleNumtemp!=8){
                moleNum=moleNumtemp+1;}
            else{moleNum=0;}




//                        activeMoles[tempmole]=false;
//                        moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
            /////
            activeMoles[moleNum]=true;
            /////
//                        score.setText(Integer.toString(player.getPoint()));

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
                    moles.get(moleNum).animate().translationYBy(250).setDuration(500).withEndAction(endAction2).setStartDelay(500);
//                    moles.get(moleNum).animate().translationYBy(250).setDuration(500).withEndAction(endAction2).setStartDelay(500);

                }
            };


            moles.get(moleNum).animate().translationYBy(-250).setDuration(500).withEndAction(endAction);
//            moles.get(moleNum).setClickable(false);
                        score.setText("Score: "+Integer.toString(player.getPoint())+"\nOppoent Score:"+oppoentScore);
            moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));


            tempmole=moleNum;



                    }
                });

            }
        },0,1500);





    }

    // Start the initial runnable task by posting through the handler

    synchronized void start() {


        player.start();
        h4.setVisibility(View.INVISIBLE);// the extra heart will only be there if the user click the heart
        deadbomb.setVisibility(View.INVISIBLE);
        deadfreeze.setVisibility(View.INVISIBLE);
        deadhealth.setVisibility(View.INVISIBLE);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timeLeft.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timeLeft.setText("Time is up!");
                timer.cancel();
                socket.disconnect();
//                handler.removeCallbacks(runnableCode);
//
//                try {
//                    player.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //TODO:move to the next page or something

            }
        }.start();

        for (int i=0;i<9;i++) {
//            sadmoles.get(i).setVisibility(View.INVISIBLE);
            final int finalI = i;
            final int finalI1 = i;
            moles.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(activeMoles[finalI]){
//                        activeMoles[finalI]=false;
                        player.hitMole();
                        moles.get(finalI1).setImageDrawable(getResources().getDrawable(R.drawable.game_sadmole));
                        socket.emit("score");
//                    moles.get(finalI).setClickable(false);



                    }

                }
            });
        }
    }






    void gameOver() {

    }




}

class PlayerThread extends Thread{
    private int point;
    private int energy;
    private int health;
    private int status;
//    int tempmole=0;
//    final  Handler handler = new Handler();
//    final Context context;
//    Runnable runnableCode;
//    Runnable runnableCode = new Runnable() {
//
//
//        @Override
//        public synchronized void run() {
//            int moleNumtemp = (int) (Math.random() * 9);
//            final int moleNum;
//
//            //no same mole consecutively
//            if(tempmole!=moleNumtemp){
//                moleNum=moleNumtemp;}
//            else if (moleNumtemp!=8){
//                moleNum=moleNumtemp+1;}
//            else{moleNum=0;}
//
//
////                        activeMoles[tempmole]=false;
////                        moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//            MainActivity.activeMoles[moleNum]=true;
////                        score.setText(Integer.toString(player.getPoint()));
//
////                        ObjectAnimator animation = new ObjectAnimator();
////                        moles.get(moleNum).animate().translationYBy(-250).setDuration(1000);
//
//            // 0.04 s to rise up a
//            // nd go down, 0.5 s stay there
//            final Runnable endAction2 = new Runnable() {
//                public void run() {
//                    MainActivity.moles.get(moleNum).setImageDrawable(context.getResources().getDrawable(R.drawable.game_mole));
//                }
//            };
//            Runnable endAction = new Runnable() {
//                public void run() {
//                    MainActivity.moles.get(moleNum).animate().translationYBy(250).setDuration(200).withEndAction(endAction2).setStartDelay(500);
//
//                }
//            };
//
//
//            MainActivity.moles.get(moleNum).animate().translationYBy(-250).setDuration(200).withEndAction(endAction);
//            MainActivity.moles.get(moleNum).setImageDrawable(context.getResources().getDrawable(R.drawable.game_mole));
//            MainActivity.score.setText("Score: "+point);
//
//            tempmole=moleNum;
//
//            handler.postDelayed(runnableCode, 1100);
//
//        }
//
//
//    };

    public PlayerThread(final Context context){
        this.point=0;
        this.energy=0;
        this.health=3;}
//        this.context=context;
//        this.runnableCode= new Runnable() {
//
//
//            @Override
//            public synchronized void run() {
//                int moleNumtemp = (int) (Math.random() * 9);
//                final int moleNum;
//
//                //no same mole consecutively
//                if(tempmole!=moleNumtemp){
//                    moleNum=moleNumtemp;}
//                else if (moleNumtemp!=8){
//                    moleNum=moleNumtemp+1;}
//                else{moleNum=0;}
//
//
////                        activeMoles[tempmole]=false;
////                        moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//                MainActivity.activeMoles[moleNum]=true;
////                        score.setText(Integer.toString(player.getPoint()));
//
////                        ObjectAnimator animation = new ObjectAnimator();
////                        moles.get(moleNum).animate().translationYBy(-250).setDuration(1000);
//
//                // 0.04 s to rise up a
//                // nd go down, 0.5 s stay there
//                final Runnable endAction2 = new Runnable() {
//                    public void run() {
//                        MainActivity.moles.get(moleNum).setImageDrawable(context.getResources().getDrawable(R.drawable.game_mole));
//                    }
//                };
//                Runnable endAction = new Runnable() {
//                    public void run() {
//                        MainActivity.moles.get(moleNum).animate().translationYBy(250).setDuration(200).withEndAction(endAction2).setStartDelay(500);
//
//                    }
//                };
//
//
//                MainActivity.moles.get(moleNum).animate().translationYBy(-250).setDuration(200).withEndAction(endAction);
//                MainActivity.moles.get(moleNum).setImageDrawable(context.getResources().getDrawable(R.drawable.game_mole));
//                MainActivity.score.setText("Score: "+point);
//
//                tempmole=moleNum;
//
//                handler.postDelayed(runnableCode, 1100);
//
//            }
//
//
//        };
//
//    }



    @Override
    public void run() {
//        this.handler.post(runnableCode);

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



