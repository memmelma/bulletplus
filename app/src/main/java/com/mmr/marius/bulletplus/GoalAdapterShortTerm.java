package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class GoalAdapterShortTerm extends FirestoreRecyclerAdapter<ShortTermGoal, GoalAdapterShortTerm.GoalHolder> {

    private final static String TAG = "com.marius.adaptershort";

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    private FireBaseHandler fbh;
    private Activity mActivity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GoalAdapterShortTerm(@NonNull FirestoreRecyclerOptions<ShortTermGoal> options, Activity activity) {
        super(options);
        this.mActivity = activity;
        this.fbh = new FireBaseHandler();
    }

    @Override
    protected void onBindViewHolder(@NonNull GoalAdapterShortTerm.GoalHolder holder, int position, @NonNull final ShortTermGoal model) {

        final String doc_id = getSnapshots().getSnapshot(position).getId();

        viewBinderHelper.bind(holder.mSwipeRevealLayout, doc_id);
        viewBinderHelper.setOpenOnlyOne(true);

        holder.mTextViewTitle.setText(model.getTitle());
        holder.mTextViewDescription.setText(String.format(mActivity.getResources().getString(R.string.short_term_goal_description), mActivity.getResources().getString(R.string.long_term_goal), model.getLong_term_goal_title()));

        //holder.mTextViewCreated.setText(new SimpleDateFormat("yyyy-MM-dd").format(model.getCreated()));

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

        holder.mImageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbh.setDoneShortTermGoal(doc_id, model.getLong_term_goal_id());
            }
        });

        holder.mImageButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog
                        .Builder(mActivity)
                        .setMessage(String.format(mActivity.getResources().getString(R.string.delete_prompt), mActivity.getResources().getString(R.string.short_term_goal)))
                        .setTitle(mActivity.getResources().getString(R.string.delete_title))
                        .setPositiveButton(mActivity.getResources().getString(R.string.delete_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fbh.rmShortTermGoal(doc_id, model.getLong_term_goal_id());
                            }
                        })
                        .setNegativeButton(mActivity.getResources().getString(R.string.delete_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    @NonNull
    @Override
    public GoalAdapterShortTerm.GoalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_item_short,
                viewGroup, false);

        return new GoalAdapterShortTerm.GoalHolder(v);
    }

    class GoalHolder extends RecyclerView.ViewHolder {

        TextView mTextViewTitle;
        TextView mTextViewDescription;
        TextView mTextViewDetail;
        ImageView mImageViewCategory;
        SwipeRevealLayout mSwipeRevealLayout;
        ImageButton mImageButtonRemove;
        ImageButton mImageButtonDone;

        public GoalHolder(View v) {
            super(v);
            mTextViewTitle = (TextView) v.findViewById(R.id.text_view_title);
            mTextViewDescription = (TextView) v.findViewById(R.id.text_view_description);
            mTextViewDetail = v.findViewById(R.id.text_view_detail);
            mImageViewCategory = (ImageView) v.findViewById(R.id.imageViewCategory);
            mSwipeRevealLayout = (SwipeRevealLayout) v.findViewById(R.id.swipeRevealLayout);
            mImageButtonRemove = (ImageButton) v.findViewById(R.id.removeGoal);
            mImageButtonDone = (ImageButton) v.findViewById(R.id.doneGoal);
        }
    }
}
