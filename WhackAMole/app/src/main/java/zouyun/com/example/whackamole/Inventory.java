package zouyun.com.example.whackamole;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

        loginToken = getArguments().getString("inventoryInfo");

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

                System.out.println("RESULT: " + result.toString());

                // Pass data to onPostExecute method
                return (result.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);

                final JSONArray inventory = obj.getJSONArray("inventory");
                JSONArray equipped = obj.getJSONArray("equipped");


                startProgressbar.setVisibility(View.INVISIBLE);

                // in the case of an empty inventory, show a message saying it's empty
                if (inventory.toString().equals("[]")) {
                    gridView.setVisibility(View.INVISIBLE);
                    emptyInventory.setVisibility(View.VISIBLE);
                } else {
                    inventoryGrid.setVisibility(View.VISIBLE);

                    System.out.println("INVENTORY: " + inventory);
                    System.out.println("EQUIPPED: " + equipped);
                    System.out.println("LOGIN TOKEN:" + loginToken);

                    final String[] inventoryItems = getName(arrayToHashmap(inventory));
                    String[] equippedItems = getName(arrayToHashmap(equipped));
                    final String[] inventoryPostId = getPostId(arrayToHashmap(inventory));

                    final InventoryAdapter inventoryAdapter = new InventoryAdapter(getContext(), inventoryItems, equippedItems);
                    inventoryGrid.setAdapter(inventoryAdapter);

                    inventoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(getContext(), " " + inventoryItems[i], Toast.LENGTH_SHORT).show();
                            sendPost(inventoryPostId[i]);
//                            inventoryAdapter.notifyDataSetChanged();
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public ArrayList<HashMap<String,Object>> arrayToHashmap(JSONArray inventory) throws JSONException {
            ArrayList<HashMap<String,Object>> output = new ArrayList<>();

            // converting JSONArray to java's Array
            String[] list = null;
            if (inventory != null) {
                int len = inventory.length();
                list = new String[len];
                for (int i=0;i<len;i++){
                    list[i] = inventory.get(i).toString();
                }
            }

            // Changing each item in the array into a hashmap
            for (String item: list) {
                HashMap<String,Object> perItem = new HashMap<>();
                String withoutBrace = item.substring(1,item.length()-1);
                String[] values = withoutBrace.split(",");
                for (String properties:values) {
                    String[] identified = properties.split(":");
                    String key = identified[0];
                    perItem.put(key.substring(1,key.length()-1),identified[1]);
                }
                output.add(perItem);
            }
            return output;
        }

        public String[] getName (ArrayList<HashMap<String,Object>> originalList) {
            int listSize = originalList.size();
            String[] output = new String[listSize];
            for (int i = 0; i < listSize; i++) {
                String uncut = originalList.get(i).get("id").toString(); // original string has extra quotation marks
                output[i] = uncut.substring(1,uncut.length()-1);
            }
            return output;
        }

        public String[] getPostId (ArrayList<HashMap<String,Object>> originalList) {
            int listSize = originalList.size();
            String[] output = new String[listSize];
            for (int i = 0; i < listSize; i++) {
                String uncut = originalList.get(i).get("_id").toString(); // original string has extra quotation marks
                output[i] = uncut.substring(1,uncut.length()-1);
            }
            return output;
        }

        public boolean sendPost(String itemId) {
            URL url = null;
            try {
                url = new URL("http://holeymoley.herokuapp.com/api/user/equip");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;
            boolean pass = false;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestMethod("POST");
            } catch (Exception e) {
                System.out.println("Connection setting error");
            }

            // setDoInput and setDoOutput method depict handling of both send
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("token",loginToken)
                    .appendQueryParameter("item", itemId);
//            String query = builder.build().getEncodedQuery();

            String query = "\"token\"=\"" + loginToken + "\"&\"item\"=\""+itemId+"\"";
            System.out.println("QUERY: " + query);

            // Open connection for sending data
            try {
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println("write error");
            } catch (Exception e) {
                System.out.println("writing error");
            }

            int response_code = 0;
            try {
                response_code = connection.getResponseCode();
                System.out.println("Response code: " + response_code);
            } catch (IOException e) {
                System.out.println("Cannot connect to server");
            } catch (Exception e) {
                System.out.println("Response code error");
            }

            try {
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    pass = true;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println("Result: " + result);

                    input.close();
                }
            } catch (Exception e) {
                System.out.println("Input stream error");
            }

            return pass;
        }
    }
}
