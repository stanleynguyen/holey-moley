package zouyun.com.example.whackamole;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.socket.client.IO;

/**
 * Created by KaiLue on 11-Apr-17.
 */

public class TestServer extends ActivityInstrumentationTestCase2<GameActivity> {
    GameActivity activity;

    public TestServer() {
        super(GameActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }
    @Test
    public void testServer() throws Exception { // canStart stays false after 10 seconds because no one join the same game

        try {
            io.socket.client.Socket socket = IO.socket("http://holeymoley.herokuapp.com");
            socket.connect();
            TimeUnit.SECONDS.sleep(10);
            assertFalse(activity.getCanStart());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }



    }
}
