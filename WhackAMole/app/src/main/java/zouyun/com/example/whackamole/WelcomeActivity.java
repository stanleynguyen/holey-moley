package zouyun.com.example.whackamole;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONObject;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WelcomeActivity extends Activity {

    private ProgressBar startProgressBar;
    private ImageView startButton;
    private TextView tapToStart;

    private EditText id;
    private EditText key;
    private Button login;
    private Button cancel;
    private TextView token;
    private LinearLayout popupview;
    private ImageView startImage;

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


        id = (EditText) findViewById(R.id.username);
        key = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginbtn);
        cancel = (Button) findViewById(R.id.cancelbtn);
        token = (TextView) findViewById(R.id.token);
        startImage = (ImageView) findViewById(R.id.startImage);
        popupview = (LinearLayout) findViewById(R.id.popup_form);

        id.setTypeface(custom_font);
        key.setTypeface(custom_font);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("start button");
                popupview.setVisibility(View.VISIBLE);
                startImage.animate().scaleY(0.6f).scaleX(0.6f);
                popupview.animate().translationY(0).alpha(1.0f);
//                startActivity(new Intent(WelcomeActivity.this, Login.class));
//                this.finish();
            }

            private void finish() {
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = id.getText().toString();
                String password = key.getText().toString();

                new WelcomeActivity.AsyncLogin().execute(username, password);
                startActivity(new Intent(WelcomeActivity.this, TabsActivity.class));
                this.finish();
            }
            private void finish() {
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("cancel");
                startImage.animate().scaleY(1.0f).scaleX(1.0f);
                popupview.animate().translationY(popupview.getHeight()).alpha(0.0f).setListener(
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            System.out.println("set invisible");
                            popupview.setVisibility(View.INVISIBLE);
                            popupview.animate().setListener(null);
                        }});

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
                popupview.setAlpha(0.0f);
                popupview.setTranslationY(popupview.getHeight());
            }



        }, 5000);
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        public static final int CONNECTION_TIMEOUT=10000;
        public static final int READ_TIMEOUT=15000;

        ProgressDialog pdLoading = new ProgressDialog(zouyun.com.example.whackamole.WelcomeActivity.this);
        HttpURLConnection conn;
        URL url = null;

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //this method will be running on UI t+hread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
//
//        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://holeymoley.herokuapp.com/api/user/login");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();
//                System.out.println(response_code);
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

//            token.setText(result);

            try {
                JSONObject obj = new JSONObject(result);
                token.setText(obj.getString("token"));

            } catch (Exception e) {
                e.printStackTrace();
            }




//            if(result.equalsIgnoreCase("true"))
//            {
//                /* Here launching another activity when login successful. If you persist login state
//                use sharedPreferences of Android. and logout button to clear sharedPreferences.
//                 */
//
//                Intent intent = new Intent(Login.this,TabsActivity.class);
//                startActivity(intent);
//                Login.this.finish();
//
//            }else if (result.equalsIgnoreCase("false")){
//
//                // If username and password does not match display a error message
//                Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
//
//            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
//
//                Toast.makeText(getApplicationContext(), "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
//
//            }
        }

    }
}

