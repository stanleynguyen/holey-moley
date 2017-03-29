package zouyun.com.example.whackamole;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by KaiLue on 02-Mar-17.
 */

public class Settings extends Fragment {

    ViewPager viewPager;

    private TextView settingstxt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);

        viewPager = (ViewPager) getActivity().findViewById(R.id.container);

        settingstxt = (TextView) rootView.findViewById(R.id.settingstxt);


        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/bignoodletitling.ttf");
        settingstxt.setTypeface(custom_font);

        return rootView;
    }
}
