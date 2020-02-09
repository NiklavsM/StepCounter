package com.example.stepcounter.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        holder.tvDate.setText(fromLongToDateString((long) currentHistory.getDay()));
        int percentage = (int) 100.0 * currentHistory.getStepsTaken() / currentHistory.getGoalSteps();
        holder.tvProgress.setText(percentage + "%");// TODO fix warning
        holder.tvGoal.setText(currentHistory.getGoalName());
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


        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            tvProgress = itemView.findViewById(R.id.tv_history_progress);
            tvGoal = itemView.findViewById(R.id.tv_history_goal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(history.get(position));
                    }
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
