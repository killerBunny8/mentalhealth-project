package com.example.mentalhealf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MoodLogAdapter extends RecyclerView.Adapter<MoodLogAdapter.MoodLogViewHolder> {

    private List<Moodlog> moodLogs;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public MoodLogAdapter(List<Moodlog> moodLogs) {
        this.moodLogs = moodLogs;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MoodLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moodlogitem, parent, false);
        return new MoodLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodLogViewHolder holder, int position) {
        Moodlog moodlog = moodLogs.get(position);
        holder.bind(moodlog);
    }

    @Override
    public int getItemCount() {
        return moodLogs.size();
    }

    public static class MoodLogViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeLog, txtMood, editDescription;
        //EditText editDescription;


        public MoodLogViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTimeLog = itemView.findViewById(R.id.txtTimeLog);
            txtMood = itemView.findViewById(R.id.txtMoodd);
            editDescription = itemView.findViewById(R.id.textView11);

        }

        public void bind(Moodlog moodlog) {
            txtTimeLog.setText(moodlog.getTime().toDate().toString());
            txtMood.setText("Mood: " + moodlog.getFeeling());
            editDescription.setText(moodlog.getDescription());

        }
    }
}
