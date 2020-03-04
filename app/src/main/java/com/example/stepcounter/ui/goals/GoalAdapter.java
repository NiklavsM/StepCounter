package com.example.stepcounter.ui.goals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;

import java.util.ArrayList;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalHolder> {
    private List<Goal> goals = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public GoalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_item, parent, false);
        return new GoalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalHolder holder, int position) {
        Goal currentGoal = goals.get(position);
        holder.tvCount.setText(String.valueOf(currentGoal.getSteps()));
        holder.tvName.setText(currentGoal.getName());
        if (currentGoal.isActive()) {
            holder.tvActiveGoalLabel.setVisibility(View.VISIBLE);
        } else {
            holder.tvActiveGoalLabel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
        notifyDataSetChanged();
    }

    public Goal getGoalAtIndex(int index) {
        return goals.get(index);
    }

    class GoalHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvCount;
        private TextView tvActiveGoalLabel;

        public GoalHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_goal_name);
            tvCount = itemView.findViewById(R.id.tv_goal_step_count);
            tvActiveGoalLabel = itemView.findViewById(R.id.active_goal_active_label);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(goals.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Goal goal);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
