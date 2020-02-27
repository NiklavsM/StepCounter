package com.example.stepcounter.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private AddEditGoalViewModel mViewModel;
    private NavController navController;
    private Button btnSaveGoal;
    private TextView etGoalName;
    private TextView etStepCount;
    private Button btnRemoveGoal;
    private Bundle bundle;
    private Goal goalToEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_goal_fragment, container, false);
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24px);
        bundle = getArguments();

        etGoalName = view.findViewById(R.id.goal_name_et);
        etStepCount = view.findViewById(R.id.goal_steps_et);
        btnSaveGoal = view.findViewById(R.id.save_goal_btn);
        btnRemoveGoal = view.findViewById(R.id.btn_delete_goal);
        btnSaveGoal.setOnClickListener(v -> saveGoal());
        if (bundle != null) {
            activity.getSupportActionBar().setTitle("Edit goal");
            btnRemoveGoal.setVisibility(View.VISIBLE);
            btnRemoveGoal.setOnClickListener(v -> deleteGoal(bundle.getInt(GOAL_ID)));
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
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AddEditGoalViewModel.class);
        if (bundle != null) {
            goalToEdit = mViewModel.getGoalToEdit(bundle.getInt(GOAL_ID));
            etGoalName.setText(goalToEdit.getName());
            etStepCount.setText(String.valueOf(goalToEdit.getSteps()));
        }
    }

    @Override
    public void onPause() {
        hideKeyboard(requireActivity());
        super.onPause();
    }

    private void saveGoal() {

        boolean successful;
        String goalName = etGoalName.getText().toString();
        String steps = etStepCount.getText().toString();
        hideKeyboard(requireActivity());

        if (goalName.trim().equals("")) {
            showToast("Please add name");
            return;
        }
        if (steps.trim().equals("")) {
            showToast("Please add steps");
            return;
        }

        if (goalToEdit != null) {
            goalToEdit.setName(goalName);
            goalToEdit.setSteps(Integer.parseInt(steps));
            successful = mViewModel.updateGoal(goalToEdit);
        } else {
            successful = mViewModel.addGoal(new Goal(goalName, Integer.parseInt(steps)));
        }

        if (successful) {
            showToast("Goal saved");
            navController.navigate(R.id.action_AddEditGoalFragment_to_navigation_goals);
        } else {
            showToast("Goal with this name already exists");
        }
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }


}
