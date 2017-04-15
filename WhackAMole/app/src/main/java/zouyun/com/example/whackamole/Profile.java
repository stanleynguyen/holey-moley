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

        String username_get = ((TabsActivity) getActivity()).getUsername();
        String gold_get = ((TabsActivity) getActivity()).getGold();
        String level_get = ((TabsActivity) getActivity()).getLevel();
        String experience_get = ((TabsActivity) getActivity()).getExp_needed();

        System.out.println("username: " + username_get);
        System.out.println("gold_get: " + gold_get);
        System.out.println("level get: " + level_get);
        System.out.println("experience get:" + experience_get);

        username.setText(username_get);
        gold.setText("GOLD: " + gold_get);
        level.setText("LEVEL: " + level_get);

        int maxExp = 0;
        try {
            maxExp = Integer.parseInt(level_get) * 200;
            experience.setText(experience_get + " more to level up!");
            exp_bar.setMax(maxExp);
            exp_bar.setProgress(maxExp - Integer.parseInt(experience_get));
        } catch (NumberFormatException w) {
            System.out.println(w);
            experience.setText("Error fetching exp...");
        }




        return rootView;
    }
}
