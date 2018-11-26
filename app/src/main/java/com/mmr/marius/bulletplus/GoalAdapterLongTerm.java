package com.mmr.marius.bulletplus;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;

public class GoalAdapterLongTerm extends FirestoreRecyclerAdapter<LongTermGoal, GoalAdapterLongTerm.GoalHolder> {

    private final static String TAG = "com.marius.adaptershort";

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GoalAdapterLongTerm(@NonNull FirestoreRecyclerOptions<LongTermGoal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GoalHolder holder, int position, @NonNull LongTermGoal model) {
        holder.mTextViewTitle.setText(model.getTitle());
        //holder.mTextViewCreated.setText(new SimpleDateFormat("yyyy-MM-dd").format(model.getCreated()));
        //holder.mTextViewCategory.setText(model.getCategory());
        Log.i(TAG, "short " + model.getCategory());
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

    @NonNull
    @Override
    public GoalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_item,
                viewGroup, false);

        CardView mCardView = v.findViewById(R.id.card_view);
        mCardView.setBackgroundColor(v.findViewById(R.id.card_view).getResources().getColor(R.color.colorUpperGoal));

        return new GoalHolder(v);
    }

    class GoalHolder extends RecyclerView.ViewHolder {

        TextView mTextViewTitle;
        TextView mTextViewCreated;
        ImageView mImageViewCategory;

        public GoalHolder(View v) {
            super(v);
            mTextViewTitle = (TextView) v.findViewById(R.id.text_view_title);
            //mTextViewCreated = v.findViewById(R.id.text_view_created);
            mImageViewCategory = (ImageView) v.findViewById(R.id.imageViewCategory);
        }
    }
}
