package com.mmr.marius.bulletplus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class GoalAdapterLongTerm extends FirestoreRecyclerAdapter<LongTermGoal, GoalAdapterLongTerm.GoalHolder> {

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
        holder.mTextViewCreated.setText(new SimpleDateFormat("yyyy-MM-dd").format(model.getCreated()));
        holder.mTextViewCategory1.setText(model.getCategory_1());
        holder.mTextViewCategory2.setText(model.getCategory_2());
    }

    @NonNull
    @Override
    public GoalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_item,
                viewGroup, false);

        return new GoalHolder(v);
    }

    class GoalHolder extends RecyclerView.ViewHolder {

        TextView mTextViewTitle;
        TextView mTextViewCreated;
        TextView mTextViewCategory1;
        TextView mTextViewCategory2;

        public GoalHolder(View v) {
            super(v);
            mTextViewTitle = v.findViewById(R.id.text_view_title);
            mTextViewCreated = v.findViewById(R.id.text_view_created);
            mTextViewCategory1 = v.findViewById(R.id.text_view_category_1);
            mTextViewCategory2 = v.findViewById(R.id.text_view_category_2);

        }
    }
}
