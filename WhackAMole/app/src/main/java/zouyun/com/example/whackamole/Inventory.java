package zouyun.com.example.whackamole;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

public class Inventory extends Fragment {

    private ProgressBar startProgressbar;
    private TextView emptyInventory;
    private GridView inventoryGrid;
    private String loginToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inventory, container, false);

        TextView inventorytxt = (TextView) rootView.findViewById(R.id.inventorytxt);
        startProgressbar = (ProgressBar) rootView.findViewById(R.id.startProgressBar);
        inventoryGrid = (GridView) rootView.findViewById(R.id.inventoryGrid);
        emptyInventory = (TextView) rootView.findViewById(R.id.empty_inventory);
        emptyInventory.setVisibility(View.INVISIBLE);
        inventoryGrid.setVisibility(View.INVISIBLE);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bignoodletitling.ttf");
        inventorytxt.setTypeface(custom_font);
        emptyInventory.setTypeface(custom_font);

        loginToken = getArguments().getString("token");

        new Inventory.AsyncInventory(loginToken, inventoryGrid).execute();

        return rootView;
    }


    public class AsyncInventory extends AsyncTask<String, String, String> {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        private String loginToken;
        private GridView gridView;

        HttpURLConnection connection;
        URL url = null;

        AsyncInventory(String loginToken, GridView gridView) {
            this.loginToken = loginToken;
            this.gridView = gridView;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // URL address to retrieve inventory information
                url = new URL("http://holeymoley.herokuapp.com/api/user/info?token=" + loginToken);
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
                JSONObject obj = new JSONObject(result);

                final JSONArray inventory = obj.getJSONArray("inventory");
                JSONArray equipped = obj.getJSONArray("equipped");
                System.out.println(obj);
                ((TabsActivity) getActivity()).setGold(obj.getString("gold"));
                ((TabsActivity) getActivity()).setUsername(obj.getString("username"));
                ((TabsActivity) getActivity()).setExp_needed(obj.getString("exp_needed"));
                ((TabsActivity) getActivity()).setLevel(obj.getString("level"));

                startProgressbar.setVisibility(View.INVISIBLE);

                // in the case of an empty inventory, show a message saying it's empty
                if (inventory.toString().equals("[]")) {
                    gridView.setVisibility(View.INVISIBLE);
                    emptyInventory.setVisibility(View.VISIBLE);
                } else {
                    inventoryGrid.setVisibility(View.VISIBLE);

                    final String[] inventoryItems = p.getName(p.arrayToHashmap(inventory));
                    String[] equippedItems = p.getName(p.arrayToHashmap(equipped));
                    final String[] inventoryPostId = p.getPostId(p.arrayToHashmap(inventory));

                    final InventoryAdapter inventoryAdapter = new InventoryAdapter(getContext(), inventoryItems, equippedItems);
                    inventoryGrid.setAdapter(inventoryAdapter);

                    inventoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            new Inventory.AsyncEquipped(inventoryAdapter).execute(inventoryPostId[i],loginToken);
                            new Inventory.AsyncInventory(loginToken,gridView).execute();
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("Finished execution!");
        }
    }

    public class AsyncEquipped extends AsyncTask<String,String,String> {
        private String itemId;
        private String token;
        private boolean pass = false;
        InventoryAdapter inventoryAdapter;

        public AsyncEquipped(InventoryAdapter inventoryAdapter) {
            this.inventoryAdapter = inventoryAdapter;
        }

        @Override
        protected String doInBackground(String... strings) {
            URL equipUrl = null;
            try {
                equipUrl = new URL("http://holeymoley.herokuapp.com/api/user/equip");
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
                connection = (HttpURLConnection) equipUrl.openConnection();
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
                    return ("No such item");
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
