package com.mmr.marius.bulletplus;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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

        Intent i = new Intent(MainActivity.this, LoadingActivity.class);
        startActivityForResult(i, REQUEST_CODE_LOAD);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add);

                if(tabPosition == 2){
                    //TODO fix fetch update issue 1 tab to 2 tab no update, 0 tab to 2 tab update
                    //fetch(tab.view, new FireBaseHandler().getUserID());
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
                    case 1:
                        //0 short term, 1 long term
                        Intent i = new Intent(MainActivity.this, NewGoalActivity.class);
                        i.putExtra(TAG, tabPosition);
                        startActivity(i);
                        break;
                    case 2:
                        String uid = new FireBaseHandler().getUserID();

                        View rootView = findViewById(R.id.linearStatistics);
                        fetch(rootView, uid);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO add signOut button
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
                            .orderBy("created", Query.Direction.DESCENDING);

                    FirestoreRecyclerOptions<ShortTermGoal> options_short = new FirestoreRecyclerOptions.Builder<ShortTermGoal>()
                            .setQuery(query, ShortTermGoal.class)
                            .build();

                    mAdapterShortTerm = new GoalAdapterShortTerm(options_short);

                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(mAdapterShortTerm);

                    mAdapterShortTerm.startListening();
                    break;

                case 2: //Long Term Goals
                    rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
                    mRecyclerView= (RecyclerView) rootView.findViewById(R.id.recycler_view);

                    query = new FireBaseHandler().getLongTermGoalsUndone(uid)
                            .orderBy("created", Query.Direction.DESCENDING);

                    FirestoreRecyclerOptions<LongTermGoal> options_long = new FirestoreRecyclerOptions.Builder<LongTermGoal>()
                            .setQuery(query, LongTermGoal.class)
                            .build();

                    mAdapterLongTerm = new GoalAdapterLongTerm(options_long);

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
                            updateChartViewed(rootView);
                        }
                    };

                    radioGroupData.setOnCheckedChangeListener(ocListener);
                    radioGroupType.setOnCheckedChangeListener(ocListener);

                    fetch(rootView, uid);
                    break;

                default:
                    rootView = null;
                    break;
            }
            return rootView;
        }

        private void updateChartViewed(View rootView){
            PieChart mPieLong = (PieChart) rootView.findViewById(R.id.pieChartLong);
            PieChart mPieShort = (PieChart) rootView.findViewById(R.id.pieChartShort);
            RadarChart mRadarLong = (RadarChart) rootView.findViewById(R.id.radarChartLong);
            RadarChart mRadarShort = (RadarChart) rootView.findViewById(R.id.radarChartShort);

            RadioGroup radioGroupType = (RadioGroup) rootView.findViewById(R.id.radioGroupChart0);
            RadioGroup radioGroupData = (RadioGroup) rootView.findViewById(R.id.radioGroupChart1);

            RadioButton radioGroupX = (RadioButton) rootView.findViewById(radioGroupData.getCheckedRadioButtonId());


            int dataId = radioGroupData.getCheckedRadioButtonId();
            int typeId = radioGroupType.getCheckedRadioButtonId();
            String swtch = dataId + "" + typeId;

            switch (swtch)
            {
                case (R.id.radioPie + "" + R.id.radioShort): //short pie
                    mPieShort.setVisibility(View.VISIBLE);
                    mPieLong.setVisibility(View.GONE);
                    mRadarShort.setVisibility(View.GONE);
                    mRadarLong.setVisibility(View.GONE);
                    break;

                case (R.id.radioRadar + "" + R.id.radioShort): //short radar
                    mPieShort.setVisibility(View.GONE);
                    mPieLong.setVisibility(View.GONE);
                    mRadarShort.setVisibility(View.VISIBLE);
                    mRadarLong.setVisibility(View.GONE);
                    break;

                case (R.id.radioPie + "" + R.id.radioLong): //long pie
                    mPieShort.setVisibility(View.GONE);
                    mPieLong.setVisibility(View.VISIBLE);
                    mRadarShort.setVisibility(View.GONE);
                    mRadarLong.setVisibility(View.GONE);
                    break;

                case (R.id.radioRadar + "" + R.id.radioLong): //long radar
                    mPieShort.setVisibility(View.GONE);
                    mPieLong.setVisibility(View.GONE);
                    mRadarShort.setVisibility(View.GONE);
                    mRadarLong.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }


    private static int LongTermGoalsUndoneSize = 0;
    private static int LongTermGoalsDoneSize = 0;
    private static int ShortTermGoalsUndoneSize = 0;
    private static int ShortTermGoalsDoneSize = 0;

    private static int[] LongTermGoalsCategories = new int[] {0, 0, 0, 0};
    private static int[] ShortTermGoalsCategories = new int[] {0, 0, 0, 0};

    final static String[] mCategories = new String[] {"Personal", "Social", "Health", "Work"};

    private static void fetch(final View rootView, String uid){

        Log.i(TAG, "fetching");

        LongTermGoalsCategories = new int[] {0, 0, 0, 0};
        ShortTermGoalsCategories = new int[] {0, 0, 0, 0};

        new FireBaseHandler().getLongTermGoalsDone(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                    for(DocumentSnapshot doc : documents){
                        int category = (int) Integer.parseInt(doc.get("category").toString());
                        LongTermGoalsCategories[category]++;
                    }

                    LongTermGoalsDoneSize = documents.size();

                    updateChartsLong(rootView);
                }else{

                }
            }
        });


        new FireBaseHandler().getLongTermGoalsUndone(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                    for(DocumentSnapshot doc : documents){
                        int category = (int) Integer.parseInt(doc.get("category").toString());
                        LongTermGoalsCategories[category]++;
                    }

                    LongTermGoalsUndoneSize = documents.size();

                    updateChartsLong(rootView);
                }else{

                }
            }
        });

        new FireBaseHandler().getShortTermGoalsDone(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                    for(DocumentSnapshot doc : documents){
                        int category = (int) Integer.parseInt(doc.get("category").toString());
                        ShortTermGoalsCategories[category]++;
                    }

                    ShortTermGoalsDoneSize = documents.size();

                    updateChartsShort(rootView);
                }else{

                }
            }
        });

        new FireBaseHandler().getShortTermGoalsUndone(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                    for(DocumentSnapshot doc : documents){
                        int category = (int) Integer.parseInt(doc.get("category").toString());
                        ShortTermGoalsCategories[category]++;
                    }

                    ShortTermGoalsUndoneSize = documents.size();

                    updateChartsShort(rootView);
                }else{

                }
            }
        });
    }

    private static void updateChartsShort(View rootView){
        Context context = rootView.getContext();

        List<PieEntry> entries_pie_short = new ArrayList<>();
        entries_pie_short.add(new PieEntry(ShortTermGoalsDoneSize, "Done"));
        entries_pie_short.add(new PieEntry(ShortTermGoalsUndoneSize, "Undone"));


        PieDataSet set_pie_short = new PieDataSet(entries_pie_short, "");
        set_pie_short.setColors(new int[] { R.color.colorPrimary, R.color.colorSecondary}, context);

        set_pie_short.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Integer.toString((int) value);
            }
        });

        PieChart pieChartShort = (PieChart) rootView.findViewById(R.id.pieChartShort);
        pieChartShort.setCenterText("Short Term Goals");
        PieData data_pie_short = new PieData(set_pie_short);
        pieChartShort.setData(data_pie_short);
        pieChartShort.invalidate(); // refresh

        /// -------

        ArrayList<RadarEntry> entries_short = new ArrayList<>();

        for (int i = 0; i < ShortTermGoalsCategories.length; i++){
            entries_short.add(new RadarEntry(ShortTermGoalsCategories[i]));
        }

        RadarDataSet set_radar_short = new RadarDataSet(entries_short, "Short Term");
        set_radar_short.setColors(new int[] {R.color.colorPrimary, R.color.colorPrimaryLight}, context);
        set_radar_short.setDrawFilled(true);
        set_radar_short.setFillAlpha(180);
        set_radar_short.setLineWidth(2f);
        set_radar_short.setDrawHighlightCircleEnabled(true);
        set_radar_short.setDrawHighlightIndicators(false);

        set_radar_short.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Integer.toString((int) value);
            }
        });

        ArrayList<IRadarDataSet> sets_short = new ArrayList<>();
        sets_short.add(set_radar_short);

        RadarChart radarChartShort = (RadarChart) rootView.findViewById(R.id.radarChartShort);
        RadarData data_radar_short = new RadarData(sets_short);

        radarChartShort.setData(data_radar_short);
        radarChartShort.getYAxis().setDrawLabels(false);
        radarChartShort.getXAxis().setDrawLabels(true);

        radarChartShort.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mCategories[(int) value % mCategories.length];
            }
        });

        radarChartShort.invalidate(); // refresh

        //----------------------

        TextView textViewDoneTotal = (TextView) rootView.findViewById(R.id.textViewDoneTotal);
        TextView textViewUndoneTotal = (TextView) rootView.findViewById(R.id.textViewUndoneTotal);
        int doneTotal = ShortTermGoalsDoneSize + LongTermGoalsDoneSize;
        int undoneTotal = ShortTermGoalsUndoneSize + LongTermGoalsUndoneSize;

        textViewDoneTotal.setText(rootView.getResources().getString(R.string.done_total) + Integer.toString(doneTotal));
        textViewUndoneTotal.setText(rootView.getResources().getString(R.string.undone_total) + Integer.toString(undoneTotal));
    }


    private static void updateChartsLong(View rootView){

        Context context = rootView.getContext();

        List<PieEntry> entries_pie_long = new ArrayList<>();
        entries_pie_long.add(new PieEntry(LongTermGoalsDoneSize, "Done"));
        entries_pie_long.add(new PieEntry(LongTermGoalsUndoneSize, "Undone"));

        PieDataSet set_pie_long = new PieDataSet(entries_pie_long, "");

        set_pie_long.setColors(new int[] { R.color.colorPrimary, R.color.colorSecondary}, context);
        set_pie_long.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Integer.toString((int) value);
            }
        });

        PieChart pieChartLong = (PieChart) rootView.findViewById(R.id.pieChartLong);
        PieData data_pie_long = new PieData(set_pie_long);
        pieChartLong.setCenterText("Long Term Goals");
        pieChartLong.setData(data_pie_long);
        pieChartLong.invalidate(); // refresh

        /// -------

        ArrayList<RadarEntry> entries_long = new ArrayList<>();

        for (int i = 0; i < LongTermGoalsCategories.length; i++){
            entries_long.add(new RadarEntry(LongTermGoalsCategories[i]));
        }

        RadarDataSet set_radar_long = new RadarDataSet(entries_long, "Long Term");
        set_radar_long.setColors(new int[] {R.color.colorSecondary, R.color.colorSecondaryLight}, context);
        set_radar_long.setDrawFilled(true);
        set_radar_long.setFillAlpha(180);
        set_radar_long.setLineWidth(2f);
        set_radar_long.setDrawHighlightCircleEnabled(true);
        set_radar_long.setDrawHighlightIndicators(false);

        set_radar_long.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Integer.toString((int) value);
            }
        });

        ArrayList<IRadarDataSet> sets_long = new ArrayList<>();
        sets_long.add(set_radar_long);

        RadarChart radarChartLong = (RadarChart) rootView.findViewById(R.id.radarChartLong);
        RadarData data_radar_long = new RadarData(sets_long);

        radarChartLong.setData(data_radar_long);
        radarChartLong.getYAxis().setDrawLabels(false);
        radarChartLong.getXAxis().setDrawLabels(true);

        radarChartLong.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mCategories[(int) value % mCategories.length];
            }
        });

        radarChartLong.invalidate(); // refresh
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
                //TODO remove startActivityWithResult
                //Log.i(TAG, "returned from loading");
                //use data.getExtra(...) to retrieve the returned data
                break;
        }
    }

}
