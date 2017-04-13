package zouyun.com.example.whackamole;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Arrays;

/**
 * Created by woshibiantai on 12/4/17.
 */

public class InventoryAdapter extends BaseAdapter {
    private Context context;
    private final String[] inventory;
    private final String[] equipped;

    public InventoryAdapter(Context context, String[] inventory, String[] equipped) {
        this.context = context;
        this.inventory = inventory;
        this.equipped = equipped;
    }

    @Override
    public int getCount() {
        return inventory.length;
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (view == null) {
            gridView = new View(context);

            // get layout from inventory_item.xml
            gridView = inflater.inflate(R.layout.inventory_item, null);

            // set value into textview
            TextView item_equipped = (TextView) gridView.findViewById(R.id.item_equipped);
            item_equipped.setVisibility(View.INVISIBLE); // initial status is unequipped
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/bignoodletitling.ttf");
            item_equipped.setTypeface(custom_font);

            // set image based on selected text
            ImageView itemIcon = (ImageView) gridView.findViewById(R.id.item_icon);
            String item = inventory[i];

            int resource;
            switch (item) {
                case "bomb":
                    resource = R.drawable.game_bomb;
                    break;
                case "freeze":
                    resource = R.drawable.game_freeze;
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

            // set item equipped status. Since it's derived from a hashmap, these items are unordered.

            if (Arrays.asList(equipped).contains(item)) {
                item_equipped.setVisibility(View.VISIBLE);
            }

        } else {
            gridView = (View) view;
        }

        return gridView;
    }
}
