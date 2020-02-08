package com.example.stepcounter.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AddGoalFragment extends Fragment {

    private AddGoalViewModel mViewModel;
    private NavController navController;
    private Button btnAddGoal;
    private TextView etGoalName;
    private TextView etStepCount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_goal_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_directions_walk_24px); // TODO

        etGoalName = view.findViewById(R.id.goal_name_et);
        etStepCount = view.findViewById(R.id.goal_steps_et);
        btnAddGoal = view.findViewById(R.id.add_goal_btn);
        btnAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoal();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AddGoalViewModel.class);
    }

    private void addGoal() {
        mViewModel.addGoal(new Goal(etGoalName.getText().toString(), Integer.parseInt(etStepCount.getText().toString())));
        navController.navigate(R.id.action_addGoalFragment_to_navigation_goals);
    }

}
