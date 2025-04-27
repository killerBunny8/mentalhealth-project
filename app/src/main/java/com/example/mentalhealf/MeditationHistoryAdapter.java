package com.example.mentalhealf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MeditationHistoryAdapter extends RecyclerView.Adapter<MeditationHistoryAdapter.ViewHolder> {

    private final List<Moodlog> meditationLogs;

    public MeditationHistoryAdapter(List<Moodlog> meditationLogs) {
        this.meditationLogs = meditationLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meditation_historyu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Moodlog log = meditationLogs.get(position);
        holder.bind(log);
    }

    @Override
    public int getItemCount() {
        return meditationLogs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTime, txtMood, txtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //list item layout components
            txtTime = itemView.findViewById(R.id.txtMeditationTime);
            txtMood = itemView.findViewById(R.id.txtMood);
            txtDescription = itemView.findViewById(R.id.txtMeditationDesc);
        }
        //bind moodlog values to the components
        public void bind(Moodlog log) {
            String time = new SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
                    .format(log.getTime().toDate());
            txtTime.setText(time);

            String moodEmoji;
            if (log.getFeeling() == 1) {
                moodEmoji = "😢";
            } else if (log.getFeeling() == 2) {
                moodEmoji = "😕";
            } else if (log.getFeeling() == 3) {
                moodEmoji = "😐";
            } else if (log.getFeeling() == 4) {
                moodEmoji = "🙂";
            } else if (log.getFeeling() == 5) {
                moodEmoji = "😁";
            } else {
                moodEmoji = "🤔";
            }
            txtMood.setText("Mood: " + moodEmoji);

            txtDescription.setText(log.getDescription());
        }
    }

}