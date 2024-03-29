package com.example.stepcounter.ui.goals;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static com.example.stepcounter.ui.goals.AddEditGoalFragment.GOAL_ID;

public class GoalsFragment extends Fragment {


    private GoalsViewModel goalsViewModel;
    private NavController navController;
    private FloatingActionButton btnAddGoal;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        goalsViewModel = new ViewModelProvider(requireActivity()).get(GoalsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_goals, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rv_goals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final GoalAdapter goalAdapter = new GoalAdapter();
        recyclerView.setAdapter(goalAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        goalAdapter.setOnItemClickListener(goal -> {
            if (!canEditGoals()) {
                Toast.makeText(getContext(), getString(R.string.goal_editing_disabled), Toast.LENGTH_LONG).show();
                return;
            }
            if (goal.isActive()) {
                Toast.makeText(getContext(), getString(R.string.cannot_edit_active_goal), Toast.LENGTH_LONG).show();
                return;
            }
            goToEditGoal(goal.getId());
        });

        goalsViewModel.getGoals().observe(getActivity(), goalAdapter::setGoals);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                Goal goalToRemove = goalAdapter.getGoalAtIndex(viewHolder.getAdapterPosition());
                if (!goalToRemove.isActive()) {
                    goalsViewModel.removeGoal(goalToRemove);
                    showUndoSnackbar(goalToRemove);
                } else {
                    goalAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), getString(R.string.cannot_delete_active_goal), Toast.LENGTH_LONG).show();
                }

            }


        }).attachToRecyclerView(recyclerView);
        return root;
    }

    private void showUndoSnackbar(final Goal goal) {
        Snackbar snackbar = Snackbar.make(getView(), goal.getName() + getString(R.string.goal_removed),
                Snackbar.LENGTH_LONG);
        snackbar.setAction(getString(R.string.undo), v -> goalsViewModel.addGoal(goal));
        snackbar.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btnAddGoal = view.findViewById(R.id.add_goal_btn);
        btnAddGoal.setOnClickListener(v -> navController.navigate(R.id.action_navigation_goals_to_AddEditGoalFragment));
    }

    private boolean canEditGoals() {
        return sharedPreferences.getBoolean(getString(R.string.goal_editing_enabled), true);
    }

    private void goToEditGoal(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(GOAL_ID, id);
        navController.navigate(R.id.action_navigation_goals_to_AddEditGoalFragment, bundle);
    }

}