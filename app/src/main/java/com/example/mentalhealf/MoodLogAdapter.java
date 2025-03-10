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

import java.text.SimpleDateFormat;
import java.util.List;

public class MoodLogAdapter extends RecyclerView.Adapter<MoodLogAdapter.MoodLogViewHolder> {

    private List<Moodlog> moodLogs;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onMoodUpdate(Moodlog moodlog, String updatedText, int position);
    }

    public MoodLogAdapter(List<Moodlog> moodLogs, onItemClickListener listener) {
        this.moodLogs = moodLogs;
        db = FirebaseFirestore.getInstance();
        this.listener = listener;
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
        holder.btnUpdate.setOnClickListener(v -> {
            if (listener != null) {
                String updatedText = holder.editDescription.getText().toString();
                listener.onMoodUpdate(moodlog, updatedText, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moodLogs.size();
    }

    public static class MoodLogViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeLog, txtMood;
        EditText editDescription;
        Button btnUpdate;


        public MoodLogViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTimeLog = itemView.findViewById(R.id.txtTimeLog);
            txtMood = itemView.findViewById(R.id.txtMoodd);
            editDescription = itemView.findViewById(R.id.textView11);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);

        }

        public void bind(Moodlog moodlog) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

            String formatTime = simpleDateFormat.format(moodlog.getTime().toDate());
            txtTimeLog.setText(formatTime);

            //txtMood.setText("Mood: " + moodlog.getFeeling());

            String moodText;
            if (moodlog.getFeeling() == 1) {
                moodText = "😢";
            } else if (moodlog.getFeeling() == 2) {
                moodText = "😕";
            } else if (moodlog.getFeeling() == 3) {
                moodText = "😐";
            } else if (moodlog.getFeeling() == 4) {
                moodText = "🙂";
            } else if (moodlog.getFeeling() == 5) {
                moodText = "😁";
            } else {
                moodText = "🤔";
            }

            txtMood.setText(moodText);
            editDescription.setText(moodlog.getDescription() + " You were "+ moodlog.getActivity());
        }
    }
}
