package zouyun.com.example.whackamole;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KaiLue on 02-Mar-17.
 */

public class Shop extends Fragment {

    private ProgressBar shopProgress;
    private GridView shopGrid;
    private String loginToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shop, container, false);

        loginToken = getArguments().getString("token");

        TextView shoptxt = (TextView) rootView.findViewById(R.id.shoptxt);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/bignoodletitling.ttf");
        shoptxt.setTypeface(custom_font);
        shopProgress = (ProgressBar) rootView.findViewById(R.id.shopProgress);
        shopGrid = (GridView) rootView.findViewById(R.id.shopGrid);
        shopGrid.setVisibility(View.INVISIBLE);

        new Shop.AsyncShop(loginToken,shopGrid).execute();

        return rootView;
    }

    public class AsyncShop extends AsyncTask<String,String,String> {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        private String loginToken;
        private GridView gridView;

        HttpURLConnection connection;
        URL url = null;

        AsyncShop(String loginToken, GridView gridView) {
            this.loginToken = loginToken;
            this.gridView = gridView;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                // URL address to retrieve inventory information
                url = new URL("http://holeymoley.herokuapp.com/api/item/all?token=" + loginToken);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestMethod("GET");

                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Pass data to onPostExecute method
                return (result.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Parser p = new Parser();
            try {
                JSONArray object = new JSONArray(result);

                shopProgress.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);

                final ArrayList<HashMap<String,Object>> itemArray = p.arrayToHashmap(object);
                System.out.println("Items: " + itemArray);
                final String gold = ((TabsActivity) getActivity()).getGold();

                final ShopAdapter shopAdapter = new ShopAdapter(getContext(),itemArray,gold);
                gridView.setAdapter(shopAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        HashMap<String,Object> item = itemArray.get(i);
                        try {
                            if (Integer.parseInt(item.get("price").toString()) > Integer.parseInt(gold)) {
                                Toast.makeText(getContext(), "Not enough gold!", Toast.LENGTH_SHORT).show();
                            } else {
                                String id = item.get("_id").toString();
                                new AsyncBuy(shopAdapter).execute(id.substring(1,id.length()-1),loginToken);
                                new AsyncShop(loginToken,gridView).execute();
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class AsyncBuy extends AsyncTask<String,String,String> {
        private String itemId;
        private String token;
        private boolean pass = false;
        ShopAdapter shopAdapter;

        public AsyncBuy(ShopAdapter shopAdapter) {
            this.shopAdapter = shopAdapter;
        }

        @Override
        protected String doInBackground(String... strings) {
            URL shopUrl = null;
            try {
                shopUrl = new URL("http://holeymoley.herokuapp.com/api/user/buy");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            itemId = strings[0];
            token = strings[1];

            String urlParameters = "item="+itemId+"&token="+token;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) shopUrl.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            try {
                connection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty( "charset", "utf-8");
            connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            connection.setUseCaches( false );

            try {
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.write(postData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                pass = false;
                int response = connection.getResponseCode();
                System.out.println("Server response: " + response);

                // check if successful connection made
                if (response == HttpURLConnection.HTTP_OK) {
                    // Read data from server
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    pass = true;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // pass data to onPostExecuteMethod
                    return result.toString();
                } else {
                    return ("You already own the item");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!pass) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
