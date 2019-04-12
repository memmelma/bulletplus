package com.mmr.marius.bulletplus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class FinalGoal {

    private static ArrayList<TimelineRow> timelineRowsList;
    private static View root;
    private static ArrayAdapter<TimelineRow> myAdapter;
    private static int idCount;

    private final static String TAG = "com.marius.finalgoal";

    private final static ArrayList<Integer> iconList = Lists.newArrayList(R.drawable.ic_planet_earth, R.drawable.ic_planet_moon, R.drawable.ic_planet_mars, R.drawable.ic_planet_saturn, R.drawable.ic_planet_sun);

    public static void init(){

        new FireBaseHandler().getLongTermGoals()
                .whereEqualTo("user_id", new FireBaseHandler().getUserID())
                .whereEqualTo("done", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<DocumentSnapshot> docList = task.getResult().getDocuments();

                    for(ListIterator<DocumentSnapshot> iter = docList.listIterator(); iter.hasNext();){
                        DocumentSnapshot ds = iter.next();
                        addRow(ds);
                    }
                }
        });

    }

    public static void setup(View rootView){
       timelineRowsList = new ArrayList<>();
       root = rootView;
       idCount = 0;

       addRoot();

       myAdapter = new TimelineViewAdapter(rootView.getContext(), 0, timelineRowsList,
                    //if true, list will be sorted by date
                    false);

        ListView myListView = (ListView) rootView.findViewById(R.id.timeline_listView);
        myListView.setAdapter(myAdapter);

        init();
    }

    private static void addRoot(){
        // Create new timeline row (Row Id)
        TimelineRow myRow = new TimelineRow(idCount);
        idCount++;

        // To set the row Date (optional)
        //myRow.setDate(new Date());
        // To set the row Title (optional)
        myRow.setTitle("Falcon Heavy");
        // To set the row Description (optional)
        myRow.setDescription("Space X");
        // To set the row bitmap image (optional)
        myRow.setImage(drawableToBitmap(root.getResources().getDrawable(R.drawable.ic_space_shuttle)));
        // To set row Below Line Color (optional)
        myRow.setBellowLineColor(root.getResources().getColor(R.color.colorAccent));
        // To set row Below Line Size in dp (optional)
        myRow.setBellowLineSize(6);
        // To set row Image Size in dp (optional)
        myRow.setImageSize(60);
        // To set background color of the row image (optional)
        myRow.setBackgroundColor(root.getResources().getColor(R.color.colorAccent));
        // To set the Background Size of the row image in dp (optional)
        myRow.setBackgroundSize(60);
        // To set row Date text color (optional)
        //myRow.setDateColor(Color.argb(255, 0, 0, 0));
        // To set row Title text color (optional)
        myRow.setTitleColor(Color.argb(255, 0, 0, 0));
        // To set row Description text color (optional)
        myRow.setDescriptionColor(Color.argb(255, 0, 0, 0));

        // Add the new row to the list
        timelineRowsList.add(myRow);
    }

    private static void addRow(DocumentSnapshot ds){

        // Create new timeline row (Row Id)
        TimelineRow myRow = new TimelineRow(idCount);
        idCount++;

        // To set the row Date (optional)
        //myRow.setDate(new Date());
        // To set the row Title (optional)
        myRow.setTitle(ds.getString("title"));
        // To set the row Description (optional)
        myRow.setDescription(ds.getString("description"));
        // To set the row bitmap image (optional)
        myRow.setImage(drawableToBitmap(root.getResources().getDrawable(iconList.get(idCount%iconList.size()))));
        // To set row Below Line Color (optional)
        myRow.setBellowLineColor(root.getResources().getColor(R.color.colorPrimaryLight));
        // To set row Below Line Size in dp (optional)
        myRow.setBellowLineSize(6);
        // To set row Image Size in dp (optional)
        myRow.setImageSize(60);
        // To set background color of the row image (optional)
        myRow.setBackgroundColor(root.getResources().getColor(R.color.colorPrimaryLight));
        // To set the Background Size of the row image in dp (optional)
        myRow.setBackgroundSize(60);
        // To set row Date text color (optional)
        //myRow.setDateColor(Color.argb(255, 0, 0, 0));
        // To set row Title text color (optional)
        myRow.setTitleColor(Color.argb(255, 0, 0, 0));
        // To set row Description text color (optional)
        myRow.setDescriptionColor(Color.argb(255, 0, 0, 0));

        // Add the new row to the list
        timelineRowsList.add(0, myRow);

        myAdapter.notifyDataSetChanged();
    }

    private static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}

