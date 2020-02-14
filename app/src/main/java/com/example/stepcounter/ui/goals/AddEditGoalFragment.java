package com.example.stepcounter.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;

import static com.example.stepcounter.utils.Utils.hideKeyboard;

public class AddEditGoalFragment extends Fragment {


    static final String GOAL_ID = "GOAL_ID";
    static final String GOAL_NAME = "GOAL_NAME";
    static final String GOAL_ACTIVE = "GOAL_ACTIVE";
    static final String GOAL_STEP_COUNT = "GOAL_STEP_COUNT";

    private AddEditGoalViewModel mViewModel;
    private NavController navController;
    private Button btnSaveGoal;
    private TextView etGoalName;
    private TextView etStepCount;
    private ImageView ivRemoveGoal;
    private Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_goal_fragment, container, false);
        AppCompatActivity acivity = ((AppCompatActivity) getActivity());
        acivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24px);
        bundle = getArguments();

        etGoalName = view.findViewById(R.id.goal_name_et);
        etStepCount = view.findViewById(R.id.goal_steps_et);
        btnSaveGoal = view.findViewById(R.id.save_goal_btn);
        ivRemoveGoal = view.findViewById(R.id.iv_delete_goal);
        btnSaveGoal.setOnClickListener(v -> saveGoal());
        if (bundle != null) {
            acivity.getSupportActionBar().setTitle("Edit goal");
            ivRemoveGoal.setVisibility(View.VISIBLE);
            ivRemoveGoal.setOnClickListener(v -> deleteGoal(bundle.getInt(GOAL_ID)));
        }


        return view;
    }

    private void deleteGoal(int id) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setIcon(R.drawable.ic_delete)
                .setMessage("Do you want to delete this goal?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    mViewModel.deleteGoal(id);
                    navController.navigate(R.id.action_AddEditGoalFragment_to_navigation_goals);
                })
                .setNegativeButton("Cancel", null).show();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        if (bundle != null) {
            etGoalName.setText(bundle.getString(GOAL_NAME));
            etStepCount.setText(bundle.getString(GOAL_STEP_COUNT));
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AddEditGoalViewModel.class);
    }

    private void saveGoal() {

        String goalName = etGoalName.getText().toString();
        int stepCount = Integer.parseInt(etStepCount.getText().toString());
        if (bundle != null) {
            int id = bundle.getInt(GOAL_ID);
            boolean active = bundle.getBoolean(GOAL_ACTIVE);
            mViewModel.updateGoal(new Goal(id, goalName, stepCount, active));
        } else {
            mViewModel.addGoal(new Goal(goalName, stepCount));
        }
        navController.navigate(R.id.action_AddEditGoalFragment_to_navigation_goals);
        hideKeyboard(requireActivity());
    }


}
