package zouyun.com.example.whackamole;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

//TODO:implement bomb mole and set OnclickListener that relates to the heart displayed
//TODO:display game over when time is up and show score
//TODO: fix the issue when there are too many moles popping out after using the special power

//TODO:implement immunity
//TODO:adjust the use of thread to improve performance

//TODO: optimise and enable faster mole popping and response(currently 0.04s for moving up.down mess things up)

//TODO: lose all lives-->dead
//TODO:add sound when pressing
//TODO: in case the player died



public class GameActivity extends AppCompatActivity {
    private static ArrayList<ImageView> moles = new ArrayList<>();
    private static ArrayList<ImageView> hearts=new ArrayList<>();
    private final PlayerThread player = new PlayerThread(this.getBaseContext());
    private ImageView m1, m2, m3, m4, m5, m6, m7, m8, m9,bm1,bm2,bm3,bm4,bm5,bm6,bm7,bm8,bm9;//moles
    private ImageView sm1,sm2,sm3,sm4,sm5,sm6,sm7,sm8,sm9;//sadmoles
    private ImageView bomb,deadbomb,freeze,deadfreeze,health,deadhealth;
    private ImageView h1,h2,h3,h4;//hearts
    private TextView score,timeLeft,freezing,mana;
    private ProgressBar manaprogress,spinningwheel,playerprogress,oppprogress;

    private static boolean[] activeMoles=new boolean[9];
    private int tempmole=0;
    private int moleNumtemp;
    private io.socket.client.Socket socket = null;
    private String item="none";
    private int opponentScore=0;
    private boolean isOver=false;
    private Object lock=new Object();
    private Object startlock=new Object();
    private boolean canStart=false;
//    final  Handler handler = new Handler();

//    Timer timer = new Timer();









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
                String.format("fonts/MoonFlowerBold.ttf"));
        Typeface typeface2 = Typeface.createFromAsset(am,
                String.format("fonts/big_noodle_titling.ttf"));


        setContentView(R.layout.activity_game);
        spinningwheel=(ProgressBar)findViewById(R.id.spinningwheel);

///set up socket
        try {
//            socket = IO.socket("http://holeymoley.herokuapp.com");
            socket = IO.socket("http://holeymoley.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.emit("join");
        final GameActivity _this = this;
        socket.on("start", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        spinningwheel.setVisibility(View.GONE);}});

                        System.out.println("game start");
                        canStart=true;
                synchronized (startlock)
                {startlock.notifyAll();}





//                        Toast.makeText(_this.getApplicationContext(),"hello1",Toast.LENGTH_SHORT).show();
                    }

                });

        socket.on("mole", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                int molenumber=Integer.parseInt(args[0].toString());
                if(item.equals("bomb")){
                    bombMolePopping(molenumber);
                    item="none";
                }
                else{molePopping(molenumber);}
                System.out.println(args[0].toString());
            }
        });//
        socket.on("item", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                item=args[0].toString();

                if(item.equals("freeze")){
                    freezeEffect();
                }

                System.out.println(args[0].toString());
            }
        });
        socket.on("score", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                opponentScore++;
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
        freezing=(TextView) findViewById(R.id.freezing);
        mana=(TextView) findViewById(R.id.mana);
        score.setTypeface(typeface2);
        freezing.setTypeface(typeface2);
        freezing.setVisibility(View.INVISIBLE);
        mana.setTypeface(typeface2);


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
        hearts.add(h1);
        hearts.add(h2);
        hearts.add(h3);
        hearts.add(h4);

        manaprogress=(ProgressBar)findViewById(R.id.manaprogress);
        playerprogress=(ProgressBar)findViewById(R.id.myprogress);
        oppprogress=(ProgressBar)findViewById(R.id.opponentprogress);
        bomb.setVisibility(View.INVISIBLE);
        freeze.setVisibility(View.INVISIBLE);
        health.setVisibility(View.INVISIBLE);
        h4.setVisibility(View.INVISIBLE);// the extra heart will only be there if the user click the heart


//        Toast.makeText(this.getApplicationContext(),"hello1",Toast.LENGTH_SHORT).show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (startlock){
                    while (!canStart){
                        try {
                            startlock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    start();
                }

            }
        }).start();







        new Thread(new Runnable() {
            @Override
            public void run() {

                //sychronise?
                while(!isOver){
                    synchronized (this){
                        synchronized (lock){
                while (player.getMana()<80) {
                    try {
                        System.out.println(player.getMana());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                freeze.setVisibility(View.INVISIBLE);
                                deadfreeze.setVisibility(View.VISIBLE);}});
                        lock.wait();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }}
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {



                                    freeze.setVisibility(View.VISIBLE);
                                    deadfreeze.setVisibility(View.INVISIBLE);}});
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        freeze.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                socket.emit("item","freeze");
                                player.loseMana(80);

                            }
                        });}}

                }
        }


        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isOver){
                    synchronized (this){
                    synchronized (lock){
                    while (player.getMana()<100) {
                        try {
//                            System.out.println(player.getMana());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {



                                    health.setVisibility(View.INVISIBLE);
                                    deadhealth.setVisibility(View.VISIBLE);}});
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }}
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                health.setVisibility(View.VISIBLE);
                                deadhealth.setVisibility(View.INVISIBLE);}});
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    health.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            hearts.get(player.getHealth()).setVisibility(View.VISIBLE);}});
                            player.gainHealth();
                            player.loseMana(100);

                        }
                    });

                }}
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {



while(!isOver){
                    synchronized (this){
                    synchronized (lock){
                    while (player.getMana()<50) {
                        try {
//                            System.out.println(player.getMana());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {



                                    bomb.setVisibility(View.INVISIBLE);
                                    deadbomb.setVisibility(View.VISIBLE);}});
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }}
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                    bomb.setVisibility(View.VISIBLE);
                    deadbomb.setVisibility(View.INVISIBLE);}});
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        bomb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            socket.emit("item","bomb");
                            player.loseMana(50);

                        }
                    });}


                }
            }}
        }).start();




//        health.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                health.setVisibility(View.GONE);
//                hearts.get(player.getHealth()).setVisibility(View.VISIBLE);
//                player.gainHealth();
//                deadhealth.setVisibility(View.VISIBLE);
//            }
//        });
        //        //timer for 1 minute
//        handler.post(runnableCode);
        score.setText("Score: "+Integer.toString(player.getPoint())+"\nopponent Score:"+opponentScore);




////this part is for moles popping
//        timer.scheduleAtFixedRate(new TimerTask() {
//
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final int moleNum=moleNumtemp;
//
//                        //no same mole consecutively
//
////                        if(tempmole!=moleNumtemp){
////                            moleNum=moleNumtemp;}
////                        else if (moleNumtemp!=8){
////                            moleNum=moleNumtemp+1;}
////                        else{moleNum=0;}
//
//
//                            if (item.equals("freeze")){
//                                freezing.setVisibility(View.VISIBLE);
//
//                                try {
//                                    System.out.println("freeeeeeezzzzzzinggg");
//                                    Thread.sleep(1500);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
////                                freezing.setVisibility(View.INVISIBLE);
//                                item="none";
//                            }
//                            else if(item.equals("bomb")){
//                                moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_bombmole));
//                                activeMoles[moleNum]=true;
//                                final Runnable endAction2 = new Runnable() {
//                                    public void run() {
//                                        moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//
//                                    }
//                                };
//                                Runnable endAction = new Runnable() {
//                                    public void run() {
//                                        moles.get(moleNum).animate().translationYBy(250).setDuration(500).withEndAction(endAction2).setStartDelay(500);
////                    moles.get(moleNum).animate().translationYBy(250).setDuration(500).withEndAction(endAction2).setStartDelay(500);
//
//                                    }
//                                };
//
//
//                                moles.get(moleNum).animate().translationYBy(-250).setDuration(500).withEndAction(endAction);
////            moles.get(moleNum).setClickable(false);
//                                score.setText("Score: "+Integer.toString(player.getPoint())+"\nopponent Score:"+opponentScore);
//                                moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//
//
//                                tempmole=moleNum;
//
//
//                                item="none";
//                            }else{
//
////                        int moleNumtemp = (int) (Math.random() * 9);
//
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
////                    moles.get(moleNum).animate().translationYBy(250).setDuration(500).withEndAction(endAction2).setStartDelay(500);
//
//                }
//            };
//
//
//            moles.get(moleNum).animate().translationYBy(-250).setDuration(500).withEndAction(endAction);
////            moles.get(moleNum).setClickable(false);
//                        score.setText("Score: "+Integer.toString(player.getPoint())+"\nopponent Score:"+opponentScore);
//            moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));
//                                moles.get(moleNum).clearColorFilter();
//
//
//            tempmole=moleNum;
//                            }
//
//
//
//                    }
//                });
//
//            }
//        },0,1500);





    }

    // Start the initial runnable task by posting through the handler

    void start() {

        player.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        timeLeft.setText("seconds remaining: " + millisUntilFinished / 1000);

                    }

                    public void onFinish() {
                        socket.disconnect();
//                timer.cancel();
                        timeLeft.setText("Time is up!");

                        isOver=true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), TabsActivity.class));//testing
                            }
                        },2000);

//                handler.removeCallbacks(runnableCode);
//
//                try {
//                    player.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                    }
                }.start();

            }
        });


        for (int i=0;i<9;i++) {
//            sadmoles.get(i).setVisibility(View.INVISIBLE);
            final int finalI = i;
            final int finalI1 = i;
            moles.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(activeMoles[finalI]){
                        if(item.equals("bomb")){
                            player.kenaBomb();
                            moles.get(finalI).getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.ADD );;
                        }
                        else if(item.equals("none")){
                            player.hitMole();
                            moles.get(finalI1).setImageDrawable(getResources().getDrawable(R.drawable.game_sadmole));
                            socket.emit("score");
                        }
//                        activeMoles[finalI]=false;

//                    moles.get(finalI).setClickable(false);



                    }

                }
            });
        }
    }

    void molePopping(final int moleNum){
        final Runnable endAction2 = new Runnable() {
            public void run() {
                moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_mole));

            }
        };

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /////
                activeMoles[moleNum]=true;
                /////

                // 0.5 s to rise up and go down, 0.5 s stay there


                Runnable endAction = new Runnable() {
                    public void run() {
                        moles.get(moleNum).animate().translationYBy(250).setDuration(500).withEndAction(endAction2).setStartDelay(500);
//                    moles.get(moleNum).animate().translationYBy(250).setDuration(500).withEndAction(endAction2).setStartDelay(500);

                    }
                };


                moles.get(moleNum).animate().translationYBy(-250).setDuration(500).withEndAction(endAction);
                playerprogress.setProgress(player.getPoint()*2);
                oppprogress.setProgress(opponentScore*2);
                score.setText("ME: "+Integer.toString(player.getPoint())+"\nOpponent:"+opponentScore);
                manaprogress.setProgress(player.getMana());






            }
        });


            synchronized (lock){
                if(player.getMana()>=50){
                    System.out.println("i");lock.notifyAll();}}
        System.out.println("am");




//            moles.get(moleNum).setClickable(false);


    }
    void bombMolePopping(final int moleNum){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                moles.get(moleNum).setImageDrawable(getResources().getDrawable(R.drawable.game_bombmole));
                activeMoles[moleNum]=true;

                // 0.5 s to rise up and go down, 0.5 s stay there

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
                moles.get(moleNum).animate().translationYBy(-250).setDuration(500).withEndAction(endAction);
                playerprogress.setProgress(player.getPoint()*1);
                oppprogress.setProgress(opponentScore*1);
                score.setText("ME: "+Integer.toString(player.getPoint())+"\nOpponent:"+opponentScore);
//            moles.get(moleNum).setClickable(false);

            }
        });





    }
    void freezeEffect(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                freezing.setVisibility(View.VISIBLE);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



    }






    void gameOver() {

    }

    public boolean getCanStart() {
        return canStart;
    }

    public void setCanStart(boolean state) {
        canStart = state;
    }




}

class PlayerThread extends Thread{
    private int point;
    private int mana;
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
        this.mana=0;
        this.health=3;
        this.status=0;
    }
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
    int getMana() {
        return mana;
    }
    int getPoint() {
        return point;
    }



    void hitMole() {


        gainPoint(1);
        gainMana(10);
    }

    void kenaBomb() {
        deductHealth();
    }
    void gainHealth(){
        this.health++;
    }


    ///////////////////////
    /// utility functions
    /////////////////////

    void deductHealth() {
        if(this.health>0)
        this.health --;
    }

    void gainMana(int amount) {
        if (this.mana + amount > 100) { this.mana = 100; }
        else { this.mana += amount; }
    }
    void loseMana(int amount){
        if(this.mana>amount){
        this.mana-=amount;}
    }

    void gainPoint(int amount) {
        point += amount;
    }
    int getStatus(){
        return this.status;
    }

    void deductPoint(int amount) {
        point -= amount;
    }
}



