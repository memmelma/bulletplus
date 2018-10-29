package com.mmr.marius.bulletplus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
        holder.mTextViewTitle.setText(model.getTitle());
        holder.mTextViewDescription.setText(model.getDescription());
        holder.mTextViewPriority.setText(String.valueOf(model.getPriority()));
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item,
                viewGroup, false);

        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView mTextViewTitle;
        TextView mTextViewDescription;
        TextView mTextViewPriority;

        public NoteHolder(View v) {
            super(v);
            mTextViewTitle = v.findViewById(R.id.text_view_title);
            mTextViewDescription = v.findViewById(R.id.text_view_description);
            mTextViewPriority = v.findViewById(R.id.text_view_priority);

        }
    }
}
