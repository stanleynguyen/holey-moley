package zouyun.com.example.whackamole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private int status; // either -1, 0, 1 (lost, timeout, died)
    private int health = 3;
    private int energy = 0; // range 0 - 100
    private int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //timer for 1 minute
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //all the game code here
                start();
            }
        },1*60*1000);
        //timer
    }
    void start(){

    }


    int getHealth() {
        return health;
    }
    int getEnergy() {
        return energy;
    }
    int getPoint() {
        return points;
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
        points += amount;
    }

    void deductPoint(int amount) {
        points -= amount;
    }
    void gameOver(){

    }



}
