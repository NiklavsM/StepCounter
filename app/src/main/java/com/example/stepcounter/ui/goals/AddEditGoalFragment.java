package com.example.stepcounter.ui.goals;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;

import static com.example.stepcounter.utils.Utils.hideKeyboard;

public class AddEditGoalFragment extends Fragment {


    public static final String GOAL_ID = "GOAL_ID";
    public static final String GOAL_NAME = "GOAL_NAME";
    public static final String GOAL_STEP_COUNT = "GOAL_STEP_COUNT";

    private AddEditGoalViewModel mViewModel;
    private NavController navController;
    private Button btnSaveGoal;
    private TextView etGoalName;
    private TextView etStepCount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_goal_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_directions_walk_24px); // TODO

        etGoalName = view.findViewById(R.id.goal_name_et);
        etStepCount = view.findViewById(R.id.goal_steps_et);
        btnSaveGoal = view.findViewById(R.id.save_goal_btn);
        btnSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoal();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        System.out.println(savedInstanceState);
        Bundle bundle = getArguments();
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

        Bundle bundle = getArguments();
        String goalName = etGoalName.getText().toString();
        int stepCount = Integer.parseInt(etStepCount.getText().toString());
        if (bundle != null) {
            int id = bundle.getInt(GOAL_ID);
            mViewModel.updateGoal(new Goal(id, goalName, stepCount));
        } else {
            mViewModel.addGoal(new Goal(goalName, stepCount));
        }
        navController.navigate(R.id.action_AddEditGoalFragment_to_navigation_goals);
        hideKeyboard(requireActivity());
    }


}
