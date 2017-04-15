package zouyun.com.example.whackamole;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by woshibiantai on 14/4/17.
 */

public class ShopAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String,Object>> shop;
    String gold;

    public ShopAdapter(Context context, ArrayList<HashMap<String,Object>> shop, String gold) {
        this.context = context;
        this.shop = shop;
        this.gold = gold;
    }

    @Override
    public int getCount() {
        return shop.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (view == null) {
            gridView = new View(context);

            // get layout from inventory_item.xml
            gridView = inflater.inflate(R.layout.shop_item, null);

            // set image based on selected text
            ImageView itemIcon = (ImageView) gridView.findViewById(R.id.shop_icon);
            TextView shopPrice = (TextView) gridView.findViewById(R.id.shop_price);
            TextView shopLevel = (TextView) gridView.findViewById(R.id.shop_level);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/bignoodletitling.ttf");
            shopLevel.setTypeface(custom_font);
            shopPrice.setTypeface(custom_font);

            HashMap<String,Object> item = shop.get(i);
            String itemName = item.get("id").toString();
            String name = itemName.substring(1,itemName.length()-1);
            String itemPrice = item.get("price").toString() + " gold";
            String itemLevel = "level " + item.get("required_level").toString();

            int resource;
            switch (name) {
                case "freeze":
                    resource = R.drawable.game_freeze;
                    break;
                case "bomb":
                    resource = R.drawable.game_bomb;
                    break;
                case "health":
                    resource = R.drawable.game_health;
                    break;
                case "immunity":
                    resource = R.drawable.game_immunity;
                    break;
                default:
                    resource = R.drawable.game_bomb;
                    break;
            }
            itemIcon.setImageResource(resource);
            shopLevel.setText(itemLevel);
            shopPrice.setText(itemPrice);

            try {
                if (Integer.parseInt(item.get("price").toString()) > Integer.parseInt(gold)) {
                    itemIcon.setAlpha(0.6f);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        } else {
            gridView = (View) view;
        }

        return gridView;
    }
}
