package com.mmr.marius.bulletplus;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

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

    public final static String TAG = "com.marius.main";
    private final static int REQUEST_CODE_LOAD = 42;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private GoalAdapterLongTerm mAdapterLongTerm;
    private GoalAdapterShortTerm mAdapterShortTerm;

    private PrefSingleton mPrefSingleton;

    private int tabPosition = 0;

    private View rootView;

    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //OR
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        */

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        PrefSingleton.getInstance().Initialize(getApplicationContext());
        mPrefSingleton = PrefSingleton.getInstance();

        if(savedInstanceState == null || savedInstanceState.getBoolean("first")){
            Intent i = new Intent(MainActivity.this, LoadingActivity.class);
            startActivityForResult(i, REQUEST_CODE_LOAD);
            first = false;
        }


        final View rootView = findViewById(R.id.main_content);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add);

                if(tabPosition == 2){
                    Charts.fetchAndUpdate(rootView, new FireBaseHandler().getUserID());
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_sync));
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton mButtonAddNote = findViewById(R.id.button_add);
        mButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.i(TAG, "clicked tabPos " + tabPosition);

                switch (tabPosition){
                    case 0:
                        //0 short term, 1 long term
                        startActivity(new Intent(MainActivity.this, NewGoalShortActivity.class));
                        break;
                    case 1:
                        Intent i = new Intent(MainActivity.this, NewGoalLongActivity.class);
                        i.putExtra(TAG, tabPosition);
                        startActivity(i);
                    case 2:
                        String uid = new FireBaseHandler().getUserID();
                        Charts.fetchAndUpdate(rootView, uid);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.action_logout) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean("first", first);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        mPrefSingleton.clear();
        Toast.makeText(MainActivity.this, "Logged out",
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, LoadingActivity.class));
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onStart() {
            super.onStart();
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    try{
                        mAdapterShortTerm.startListening();
                    }catch(Exception e){
                        //Log.e(TAG, "No mAdapterShortTerm found.");
                    }
                    break;
                case 2:
                    try{
                        mAdapterLongTerm.startListening();
                    }catch(Exception e){
                        //Log.e(TAG, "No mAdapterShortTerm found.");
                    }
                    break;
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    mAdapterShortTerm.stopListening();
                    break;
                case 2:
                    mAdapterLongTerm.stopListening();
                    break;
            }
        }

        private GoalAdapterShortTerm mAdapterShortTerm;
        private GoalAdapterLongTerm mAdapterLongTerm;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView;
            RecyclerView mRecyclerView;
            final Query query;

            String uid = new FireBaseHandler().getUserID();

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1: //Short Term Goals
                    rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
                    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

                    //Firestore RecyclerView doesn't accept where statements for live bound data -> reorganized database
                    query = new FireBaseHandler().getShortTermGoalsUndone(uid)
                            .orderBy("created", Query.Direction.ASCENDING);

                    FirestoreRecyclerOptions<ShortTermGoal> options_short = new FirestoreRecyclerOptions.Builder<ShortTermGoal>()
                            .setQuery(query, ShortTermGoal.class)
                            .build();

                    mAdapterShortTerm = new GoalAdapterShortTerm(options_short, getActivity());

                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(mAdapterShortTerm);

                    mAdapterShortTerm.startListening();
                    break;

                case 2: //Long Term Goals
                    rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
                    mRecyclerView= (RecyclerView) rootView.findViewById(R.id.recycler_view);

                    query = new FireBaseHandler().getLongTermGoalsUndone(uid)
                            .orderBy("created", Query.Direction.ASCENDING);

                    FirestoreRecyclerOptions<LongTermGoal> options_long = new FirestoreRecyclerOptions.Builder<LongTermGoal>()
                            .setQuery(query, LongTermGoal.class)
                            .build();

                    mAdapterLongTerm = new GoalAdapterLongTerm(options_long, getActivity());

                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(mAdapterLongTerm);

                    mAdapterLongTerm.startListening();
                    break;

                case 3: //Statistics
                    rootView = inflater.inflate(R.layout.fragment_statistic, container, false);

                    RadioGroup radioGroupType = (RadioGroup) rootView.findViewById(R.id.radioGroupChart0);
                    RadioGroup radioGroupData = (RadioGroup) rootView.findViewById(R.id.radioGroupChart1);

                    RadioGroup.OnCheckedChangeListener ocListener = new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            Charts.updateChartViewed(rootView);
                        }
                    };

                    radioGroupData.setOnCheckedChangeListener(ocListener);
                    radioGroupType.setOnCheckedChangeListener(ocListener);

                    Charts.fetchAndUpdate(rootView, uid);
                    break;

                default:
                    rootView = null;
                    break;
            }
            return rootView;
        }

    }

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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE_LOAD:
                //Log.i(TAG, "returned from loading");
                //use data.getExtra(...) to retrieve the returned data
                break;
        }
    }

}
