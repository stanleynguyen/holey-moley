package zouyun.com.example.whackamole;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import java.util.concurrent.TimeUnit;


/**
 * Created by KaiLue on 11-Apr-17.
 */

public class GameActivityTest extends ActivityInstrumentationTestCase2<GameActivity> {
    GameActivity activity;
    PlayerThread player;

    public GameActivityTest() {
        super(GameActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        player = new PlayerThread(activity.getBaseContext());
        activity.setCanStart(true);
    }

    public void testPlayerHealthOnCreate() { // player has 3 healths at the beginning of the game
        int health = player.getHealth();
        assertTrue(health == 3);
    }

    public void testPlayerManaOnCreate() { // player has 0 mana at the beginning of the game
        int energy = player.getMana();
        assertTrue(energy == 0);
    }

    public void testPlayerPointOnCreate() { // player has 0 point at the beginning of the game
        int point = player.getPoint();
        assertTrue(point == 0);
    }

    public void testPlayerKenaBomb() { // player lose health when pressed on a bomb
        player.kenaBomb();
        int health = player.getHealth();
        assertTrue(health == 2);
    }

    public void testPlayerHitMole() { // player gain 1 point and 10 mana when pressed on a mole
        player.hitMole();
        assertTrue(player.getPoint() == 1);
        assertTrue(player.getMana() == 10);
    }

    public void testPlayerManaLimit() { // player cannot have more than 100 mana
        player.gainMana(120);
        assertFalse(player.getMana() == 120);
        assertTrue(player.getMana() == 100);
    }

    public void testPlayerDeath() throws InterruptedException { // player status is -1 when health equal or less than 0
        for (int i = 0; i < 3; i++) {
            player.deductHealth();
        }
        assertTrue(player.getHealth() == 0);
        TimeUnit.SECONDS.sleep(3);
        assertTrue(player.getStatus() == -1);
    }

    public void testPlayerClickHealth() { // player lose mana and gain health when pressed on extra health power up
        player.gainHealth();
        player.loseMana(100);
        assertTrue(player.getHealth() == 4);
        assertTrue(player.getMana() == 0);
    }
}
