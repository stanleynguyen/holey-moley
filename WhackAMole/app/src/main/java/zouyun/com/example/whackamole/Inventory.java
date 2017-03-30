package zouyun.com.example.whackamole;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by KaiLue on 02-Mar-17.
 */

public class Inventory extends Fragment {

    private TextView inventorytxt;
    private TextView bombtxt;
    private TextView freezetxt;
    private ImageButton freeze;
    private ImageButton bomb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inventory, container, false);

        inventorytxt = (TextView) rootView.findViewById(R.id.inventorytxt);
        bombtxt = (TextView) rootView.findViewById(R.id.bombtxt);
        freezetxt = (TextView) rootView.findViewById(R.id.freezetxt);

        freeze = (ImageButton) rootView.findViewById(R.id.itemLeft);
        bomb = (ImageButton) rootView.findViewById(R.id.itemRight);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/bignoodletitling.ttf");
        inventorytxt.setTypeface(custom_font);
        bombtxt.setTypeface(custom_font);
        freezetxt.setTypeface(custom_font);

        freeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Equipped Freeze", Toast.LENGTH_SHORT).show();
                // // TODO: 30/3/17 send signal to server 
            }
        });

        bomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Equipped Bomb", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), GameActivity.class));
            }
        });

        return rootView;
    }
}
