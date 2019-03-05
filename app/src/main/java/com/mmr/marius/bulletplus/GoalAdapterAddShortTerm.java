package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
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

    @Override
    protected void onBindViewHolder(@NonNull GoalHolder holder, int position, @NonNull final LongTermGoal model) {

        final String doc_id = getSnapshots().getSnapshot(position).getId();

        holder.mTextViewTitle.setText(model.getTitle());
        //holder.mTextViewCreated.setText(new SimpleDateFormat("yyyy-MM-dd").format(model.getCreated()));

        final String modelId = model.getTitle();//model.getId();
        holder.mId = modelId;

        if(mSelectionId == null)
            mSelectionId = modelId;

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionId = modelId;
                notifyDataSetChanged();
            }
        });
        if(model.getTitle() == mSelectionId)
            holder.mCheckBox.setChecked(true);
        else{
            holder.mCheckBox.setChecked(false);
        }

        switch ((int) model.getCategory()){
            case 0:
                holder.mImageViewCategory.setImageResource(R.drawable.ic_personal);
                break;
            case 1:
                holder.mImageViewCategory.setImageResource(R.drawable.ic_social);
                break;
            case 2:
                holder.mImageViewCategory.setImageResource(R.drawable.ic_health);
                break;
            case 3:
                holder.mImageViewCategory.setImageResource(R.drawable.ic_professional);
                break;
        }

    }

    //TODO category icon bug
    @NonNull
    @Override
    public GoalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_item_add_short,
                viewGroup, false);

        //CardView mCardView = v.findViewById(R.id.card_view);
        //mCardView.setBackgroundColor(v.findViewById(R.id.card_view).getResources().getColor(R.color.colorUpperGoal));

        return new GoalHolder(v);
    }

    class GoalHolder extends RecyclerView.ViewHolder {

        TextView mTextViewTitle;
        ImageView mImageViewCategory;
        CheckBox mCheckBox;
        String mId;
        CardView mCardView;

        public GoalHolder(View v) {
            super(v);
            mTextViewTitle = (TextView) v.findViewById(R.id.text_view_title);
            mImageViewCategory = (ImageView) v.findViewById(R.id.image_view_category);
            mCheckBox = (CheckBox) v.findViewById(R.id.check_box);
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }
}
