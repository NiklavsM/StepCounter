package com.example.stepcounter.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepcounter.R;
import com.example.stepcounter.database.HistoryEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.stepcounter.utils.Utils.fromLongToDateString;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private List<HistoryEntity> history = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        HistoryEntity currentHistory = history.get(position);

        holder.tvDate.setText(fromLongToDateString(currentHistory.getDay()));
        int percentage = (int) 100.0 * currentHistory.getStepsTaken() / currentHistory.getGoalSteps();
        holder.tvProgress.setText(String.valueOf(percentage));
        holder.tvGoal.setText(currentHistory.getGoalName());
        holder.tvStepsTaken.setText(currentHistory.getStepsTaken() + "/" + currentHistory.getGoalSteps());
        if (percentage >= 100) {
            holder.imHistoryIcon.setImageResource(R.drawable.ic_award_star);
        } else {
            holder.imHistoryIcon.setImageResource(R.drawable.ic_award_star_empty);
        }
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public void setHistory(List<HistoryEntity> history) {
        this.history = history;
        notifyDataSetChanged();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        private TextView tvDate;
        private TextView tvProgress;
        private TextView tvGoal;
        private TextView tvStepsTaken;
        private ImageView imHistoryIcon;


        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            tvProgress = itemView.findViewById(R.id.tv_history_progress);
            tvGoal = itemView.findViewById(R.id.tv_history_goal);
            tvStepsTaken = itemView.findViewById(R.id.tv_history_steps_taken);
            imHistoryIcon = itemView.findViewById(R.id.history_icon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(history.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(HistoryEntity history);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
