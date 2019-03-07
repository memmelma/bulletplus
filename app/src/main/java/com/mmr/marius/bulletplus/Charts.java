package com.mmr.marius.bulletplus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Charts {

    private static final String TAG = "com.marius.charts";

    private static int LongTermGoalsUndoneSize = 0;
    private static int LongTermGoalsDoneSize = 0;
    private static int ShortTermGoalsUndoneSize = 0;
    private static int ShortTermGoalsDoneSize = 0;

    private static int[] LongTermGoalsCategories = new int[] {0, 0, 0, 0};
    private static int[] ShortTermGoalsCategories = new int[] {0, 0, 0, 0};

    final static String[] mCategories = new String[] {"Personal", "Social", "Health", "Work"};

    public static void updateChartViewed(View rootView){
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

    public static void fetchAndUpdate(final View rootView, String uid){

        /*
        Log.i(TAG, "fetching...");

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

                    updateChartText(rootView);
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

                    updateChartText(rootView);
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

                    updateChartText(rootView);
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

                    updateChartText(rootView);
                    updateChartsShort(rootView);
                }else{

                }
            }
        });
        */
    }

    private static void updateChartsShort(View rootView){
        Context context = rootView.getContext();

        List<PieEntry> entries_pie_short = new ArrayList<>();
        entries_pie_short.add(new PieEntry(ShortTermGoalsDoneSize, "Done"));
        entries_pie_short.add(new PieEntry(ShortTermGoalsUndoneSize, "Undone"));


        PieDataSet set_pie_short = new PieDataSet(entries_pie_short, "");
        set_pie_short.setColors(new int[] { R.color.colorPrimary, R.color.colorAccent}, context);

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
        set_radar_short.setColors(new int[] {R.color.colorPrimary}, context);
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

    }

    private static void updateChartsLong(View rootView){

        Context context = rootView.getContext();

        List<PieEntry> entries_pie_long = new ArrayList<>();
        entries_pie_long.add(new PieEntry(LongTermGoalsDoneSize, "Done"));
        entries_pie_long.add(new PieEntry(LongTermGoalsUndoneSize, "Undone"));

        PieDataSet set_pie_long = new PieDataSet(entries_pie_long, "");

        set_pie_long.setColors(new int[] { R.color.colorPrimary, R.color.colorAccent}, context);
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
        set_radar_long.setColors(new int[] {R.color.colorPrimary}, context);
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

    private static void updateChartText(View rootView){
        TextView textViewDoneTotal = (TextView) rootView.findViewById(R.id.textViewDoneTotal);
        TextView textViewUndoneTotal = (TextView) rootView.findViewById(R.id.textViewUndoneTotal);
        int doneTotal = ShortTermGoalsDoneSize + LongTermGoalsDoneSize;
        int undoneTotal = ShortTermGoalsUndoneSize + LongTermGoalsUndoneSize;

        textViewDoneTotal.setText(rootView.getResources().getString(R.string.done_total) + Integer.toString(doneTotal));
        textViewUndoneTotal.setText(rootView.getResources().getString(R.string.undone_total) + Integer.toString(undoneTotal));
    }
}
