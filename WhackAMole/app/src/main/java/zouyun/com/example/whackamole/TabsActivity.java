package zouyun.com.example.whackamole;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class TabsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tabs);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0, false);

//        mViewPager.setOnPageChangeListener(new CircularViewPagerHandler(mViewPager));


        int[] tabIcons = {R.drawable.home_play, R.drawable.home_inventory, R.drawable.home_shop, R.drawable.home_settings};
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.Battle, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 1:
                    Inventory inventory = new Inventory();
                    return inventory;
                case 2:
                    Shop shop = new Shop();
                    return shop;
                case 0:
                    Game game = new Game();
                    return game;
                case 3:
                    return new Settings();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "INVENTORY";
                case 2:
                    return "SHOP";
                case 0:
                    return "GAME";
                case 3:
                    return "SETTINGS";
            }
            return null;
        }
    }

//    public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
//        private ViewPager   mViewPager;
//        private int         mCurrentPosition;
//        private int         mScrollState;
//
//        public CircularViewPagerHandler(final ViewPager viewPager) {
//            mViewPager = viewPager;
//        }
//
//        @Override
//        public void onPageSelected(final int position) {
//            mCurrentPosition = position;
//        }
//
//        @Override
//        public void onPageScrollStateChanged(final int state) {
//            handleScrollState(state);
//            mScrollState = state;
//        }
//
//        private void handleScrollState(final int state) {
//            if (state == ViewPager.SCROLL_STATE_IDLE) {
//                setNextItemIfNeeded();
//            }
//        }
//
//        private void setNextItemIfNeeded() {
//            if (!isScrollStateSettling()) {
//                handleSetNextItem();
//            }
//        }
//
//        private boolean isScrollStateSettling() {
//            return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
//        }
//
//        private void handleSetNextItem() {
//            final int lastPosition = mViewPager.getAdapter().getCount() - 1;
//            if(mCurrentPosition == 0) {
//                mViewPager.setCurrentItem(lastPosition, false);
//            } else if(mCurrentPosition == lastPosition) {
//                mViewPager.setCurrentItem(0, false);
//            }
//        }
//
//        @Override
//        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
//        }
//    }
}
