package zouyun.com.example.whackamole;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by woshibiantai on 14/4/17.
 */

public class Parser {
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

}
