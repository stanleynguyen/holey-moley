package zouyun.com.example.whackamole;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    private EditText id;
    private EditText key;
    private Button login;
    private TextView token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.username);
        key = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginbtn);
        token = (TextView) findViewById(R.id.token);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = id.getText().toString();
                String password = key.getText().toString();

                new AsyncLogin().execute(username, password);
            }
        });

    }
    private class AsyncLogin extends AsyncTask<String, String, String> {
        public static final int CONNECTION_TIMEOUT=10000;
        public static final int READ_TIMEOUT=15000;

        ProgressDialog pdLoading = new ProgressDialog(Login.this);
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
