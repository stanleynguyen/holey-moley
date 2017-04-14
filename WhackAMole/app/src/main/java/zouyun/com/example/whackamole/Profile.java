package zouyun.com.example.whackamole;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by KaiLue on 02-Mar-17.
 */

public class Profile extends Fragment {

    ViewPager viewPager;

    private TextView profiletxt;
    private TextView username;
    private TextView gold;
    private TextView level;
    private TextView experience;
    private ProgressBar exp_bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);

        viewPager = (ViewPager) getActivity().findViewById(R.id.container);

        profiletxt = (TextView) rootView.findViewById(R.id.profiletxt);
        username = (TextView) rootView.findViewById(R.id.profile_username);
        gold = (TextView) rootView.findViewById(R.id.profile_gold);
        level = (TextView) rootView.findViewById(R.id.profile_level);
        experience = (TextView) rootView.findViewById(R.id.profile_exp);
        exp_bar = (ProgressBar) rootView.findViewById(R.id.exp_bar);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bignoodletitling.ttf");
        profiletxt.setTypeface(custom_font);
        username.setTypeface(custom_font);
        gold.setTypeface(custom_font);
        level.setTypeface(custom_font);
        
        return rootView;
    }
}
