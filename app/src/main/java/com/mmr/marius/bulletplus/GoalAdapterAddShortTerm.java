package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Random;

public class GoalAdapterAddShortTerm extends FirestoreRecyclerAdapter<LongTermGoal, GoalAdapterAddShortTerm.GoalHolder> {

    private final static String TAG = "com.marius.adptaddshort";

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    private FireBaseHandler fbh;
    private Activity mActivity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GoalAdapterAddShortTerm(@NonNull FirestoreRecyclerOptions<LongTermGoal> options) {
        super(options);
        this.fbh = new FireBaseHandler();
    }

    String mSelectionId = null;
    String mSelectionTitle = null;

    public String getSelectionId(){
        return mSelectionId;
    }

    public String getSelectionTitle(){
        return mSelectionTitle;
    }

    @Override
    protected void onBindViewHolder(@NonNull GoalHolder holder, int position, @NonNull final LongTermGoal model) {

        final String doc_id = getSnapshots().getSnapshot(position).getId();

        holder.mTextViewTitle.setText(model.getTitle());
        //holder.mTextViewCreated.setText(new SimpleDateFormat("yyyy-MM-dd").format(model.getCreated()));

        final String modelTitle = model.getTitle();


        if(mSelectionId == null){
            mSelectionId = doc_id;
            mSelectionTitle = modelTitle;
        }


        holder.mConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionId = doc_id;
                mSelectionTitle = modelTitle;
                notifyDataSetChanged();
            }
        });

        if(doc_id.equals(mSelectionId))
            holder.mCheckBox.setChecked(true);
        else{
            holder.mCheckBox.setChecked(false);
        }

        int imageResource = 0;
        switch ((int) model.getCategory()){
            case 0:
                imageResource = R.drawable.ic_personal;
                break;
            case 1:
                imageResource = R.drawable.ic_social;
                break;
            case 2:
                imageResource = R.drawable.ic_health;
                break;
            case 3:
                imageResource = R.drawable.ic_professional;
                break;
        }
        holder.mImageViewCategory.setImageResource(imageResource);

    }

    @NonNull
    @Override
    public GoalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_item_add_short,
                viewGroup, false);

        return new GoalHolder(v);
    }

    class GoalHolder extends RecyclerView.ViewHolder {

        TextView mTextViewTitle;
        ImageView mImageViewCategory;
        CheckBox mCheckBox;
        ConstraintLayout mConstraintLayout;
        RecyclerView mRecyclerView;

        public GoalHolder(View v) {
            super(v);
            mTextViewTitle = (TextView) v.findViewById(R.id.text_view_title);
            mImageViewCategory = (ImageView) v.findViewById(R.id.image_view_category);
            mCheckBox = (CheckBox) v.findViewById(R.id.check_box);
            mConstraintLayout = (ConstraintLayout) v.findViewById(R.id.layout_constraint);
            mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        }
    }
}
