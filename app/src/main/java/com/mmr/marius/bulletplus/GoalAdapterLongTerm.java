package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.nio.channels.AcceptPendingException;
import java.util.Date;

public class GoalAdapterLongTerm extends FirestoreRecyclerAdapter<LongTermGoal, GoalAdapterLongTerm.GoalHolder> {

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
    public GoalAdapterLongTerm(@NonNull FirestoreRecyclerOptions<LongTermGoal> options, Activity activity) {
        super(options);
        this.mActivity = activity;
        this.fbh = new FireBaseHandler();
    }

    @Override
    protected void onBindViewHolder(@NonNull GoalHolder holder, int position, @NonNull final LongTermGoal model) {

        final String doc_id = getSnapshots().getSnapshot(position).getId();

        viewBinderHelper.bind(holder.mSwipeRevealLayout, doc_id);
        viewBinderHelper.setOpenOnlyOne(true);

        holder.mTextViewTitle.setText(model.getTitle());
        holder.mTextViewDescription.setText(model.getDescription());
        holder.mTextViewDetail.setText(String.format(mActivity.getResources().getString(R.string.long_term_goal_detail),Integer.toString(model.getDone_count()),Integer.toString(model.getAll_count())));
        //holder.mTextViewCreated.setText(new SimpleDateFormat("yyyy-MM-dd").format(model.getCreated()));

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

        holder.mImageButtonDone.bringToFront();
        holder.mImageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, doc_id);
                fbh.setDoneLongTermGoal(doc_id);
            }
        });

        holder.mImageButtonRemove.bringToFront();
        holder.mImageButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog
                        .Builder(mActivity)
                        .setMessage(String.format(mActivity.getResources().getString(R.string.delete_prompt), mActivity.getResources().getString(R.string.long_term_goal)))
                        .setTitle(mActivity.getResources().getString(R.string.delete_title))
                        .setPositiveButton(mActivity.getResources().getString(R.string.delete_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fbh.rmLongTermGoal(doc_id);
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
    public GoalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_item_long,
                viewGroup, false);

        return new GoalHolder(v);
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
