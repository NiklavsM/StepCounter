package com.example.stepcounter.ui.home;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView tvStepsTaken;
    private FloatingActionButton btnAddSteps;
    private EditText etAddSteps;
    private TextView tvActiveGoal;
    private TextView tvActiveGoalSteps;
    private ImageView ivChangeActiveGoal;
    private TextView tvProgress;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvStepsTaken = root.findViewById(R.id.tv_home_steps_taken);
        btnAddSteps = root.findViewById(R.id.btn_add_steps);
        etAddSteps = root.findViewById(R.id.et_add_steps);
        tvActiveGoal = root.findViewById(R.id.tv_active_goal);
        tvActiveGoalSteps = root.findViewById(R.id.tv_active_goal_steps);
        ivChangeActiveGoal = root.findViewById(R.id.iv_change_active_goal);
        tvProgress = root.findViewById(R.id.tv_goal_percentage);
        progressBar = root.findViewById(R.id.progressBar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!sharedPreferences.getBoolean("goal_switching", true)) {
            ivChangeActiveGoal.setVisibility(View.INVISIBLE);
        }


        btnAddSteps.setOnClickListener(v -> {
            String stepsToAdd = etAddSteps.getText().toString();
            if (stepsToAdd.trim().equals("")) {
                Toast.makeText(getContext(), "Please add steps", Toast.LENGTH_SHORT).show();
            } else {
                etAddSteps.onEditorAction(EditorInfo.IME_ACTION_DONE);
                homeViewModel.addToHistory(Integer.parseInt(etAddSteps.getText().toString()));
                Toast.makeText(getContext(), stepsToAdd + " steps added", Toast.LENGTH_SHORT).show();
            }
        });

        ivChangeActiveGoal.setOnClickListener(v -> homeViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            private int selectedItem = -1;

            @Override
            public void onChanged(final List<Goal> goals) {

                String[] goalNames = goals.stream().map(Goal::getName).toArray(String[]::new);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select goal");
                builder.setIcon(R.drawable.ic_star_24px);
                builder.setSingleChoiceItems(goalNames, -1, (dialog, which) -> selectedItem = which);
                builder.setPositiveButton("Set", (dialog, which) -> {
                    if (selectedItem != -1) {
                        Toast.makeText(getActivity(), "Goal set: " + goals.get(selectedItem).getName(), Toast.LENGTH_SHORT).show();
                        homeViewModel.setNewActiveGoal(goals.get(selectedItem));
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }));

        homeViewModel.getToday().observe(getViewLifecycleOwner(), today -> {
            if (today != null) {
                tvStepsTaken.setText(String.valueOf(today.getStepsTaken()));
                tvActiveGoal.setText(today.getGoalName());
                tvActiveGoalSteps.setText(String.valueOf(today.getGoalSteps()));
                int progressPercentage = (int) 100.0 * today.getStepsTaken() / today.getGoalSteps();
                tvProgress.setText(String.valueOf(progressPercentage));
                progressBar.setProgress(progressPercentage, true);
            }
        });

        return root;
    }

}