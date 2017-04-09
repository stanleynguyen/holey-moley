package zouyun.com.example.whackamole;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by KaiLue on 02-Mar-17.
 */

public class Game extends Fragment {
    private TextView gamemode;
    private TextView randomtxt;
    private TextView singletxt;
    private TextView localtxt;

    private ImageButton random;
    private ImageButton single;
    private ImageButton local;

    private ProgressBar randomPB;
    private ProgressBar singlePB;
    private ProgressBar localPB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game, container, false);

        gamemode = (TextView) rootView.findViewById(R.id.gamemode);
        randomtxt = (TextView) rootView.findViewById(R.id.randomtxt);
        singletxt = (TextView) rootView.findViewById(R.id.singletxt);
        localtxt = (TextView) rootView.findViewById(R.id.localtxt);

        random = (ImageButton) rootView.findViewById(R.id.random);
        single = (ImageButton) rootView.findViewById(R.id.single);
        local = (ImageButton) rootView.findViewById(R.id.local);

        randomPB = (ProgressBar) rootView.findViewById(R.id.randomPB);
        singlePB = (ProgressBar) rootView.findViewById(R.id.singlePB);
        localPB = (ProgressBar) rootView.findViewById(R.id.localPB);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/bignoodletitling.ttf");
        gamemode.setTypeface(custom_font);
        randomtxt.setTypeface(custom_font);
        singletxt.setTypeface(custom_font);
        localtxt.setTypeface(custom_font);

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                single.setClickable(false);
                local.setClickable(false);
                random.setVisibility(View.INVISIBLE);
                randomtxt.setVisibility(View.INVISIBLE);
                randomPB.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        randomPB.setVisibility(View.INVISIBLE);
                        random.setVisibility(View.VISIBLE);
                        randomtxt.setVisibility(View.VISIBLE);
                        single.setClickable(!false);
                        local.setClickable(!false);
                    }

                },5000);
            }
        });

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                random.setClickable(false);
                local.setClickable(false);
                single.setVisibility(View.INVISIBLE);
                singletxt.setVisibility(View.INVISIBLE);
                singlePB.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        singlePB.setVisibility(View.INVISIBLE);
                        single.setVisibility(View.VISIBLE);
                        singletxt.setVisibility(View.VISIBLE);
                        random.setClickable(!false);
                        local.setClickable(!false);
                    }

                },5000);
            }
        });

        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                random.setClickable(false);
                single.setClickable(false);
                local.setVisibility(View.INVISIBLE);
                localtxt.setVisibility(View.INVISIBLE);
                localPB.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        localPB.setVisibility(View.INVISIBLE);
                        local.setVisibility(View.VISIBLE);
                        localtxt.setVisibility(View.VISIBLE);
                        random.setClickable(true);
                        single.setClickable(true);
                        startActivity(new Intent(getActivity(), GameActivity.class));
                    }

                },5000);

            }
        });


        return rootView;
    }
}
