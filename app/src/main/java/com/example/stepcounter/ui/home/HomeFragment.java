package com.example.stepcounter.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.stepcounter.utils.GoalSelector.showSelectGoalDialog;
import static com.example.stepcounter.utils.Utils.removeTime;

public class HomeFragment extends Fragment {

    public static final String HISTORY_ID = "HISTORY_ID";

    private HomeViewModel homeViewModel;
    private TextView tvStepsTaken;
    private FloatingActionButton btnAddSteps;
    private EditText etAddSteps;
    private TextView tvActiveGoal;
    private TextView tvActiveGoalSteps;
    private ImageView ivChangeActiveGoal;
    private TextView tvProgress;
    private ProgressBar progressBar;
    private ImageView ivRemoveHistory;
    private TextView tvDateSelected;

    private NavController navController;

    private Calendar dateSelected;

    private Bundle bundle;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvStepsTaken = root.findViewById(R.id.tv_home_steps_taken);
        btnAddSteps = root.findViewById(R.id.btn_add_steps);
        etAddSteps = root.findViewById(R.id.et_add_steps);
        tvActiveGoal = root.findViewById(R.id.tv_active_goal);
        tvActiveGoalSteps = root.findViewById(R.id.tv_active_goal_steps);
        ivChangeActiveGoal = root.findViewById(R.id.iv_change_active_goal);
        tvProgress = root.findViewById(R.id.tv_goal_percentage);
        progressBar = root.findViewById(R.id.progressBar);
        ivRemoveHistory = root.findViewById(R.id.iv_delete_history);
        tvDateSelected = root.findViewById(R.id.tv_date_selected);

        btnAddSteps.setOnClickListener(v -> addStepsButtonClicked());
        ivChangeActiveGoal.setOnClickListener(v -> setActiveGoal());

        bundle = getArguments();
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        if (bundle == null) {
            homeViewModel.setHistory(-1);
        } else {
            int historyId = bundle.getInt(HISTORY_ID);
            homeViewModel.setHistory(historyId);
            AppCompatActivity acivity = ((AppCompatActivity) getActivity());
            acivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24px); // TODO not working
            acivity.getSupportActionBar().setTitle("Edit history");
            ivRemoveHistory.setVisibility(View.VISIBLE);
            ivRemoveHistory.setOnClickListener(v -> removeHistory(historyId));
        }
        homeViewModel.getDayToEdit().observe(getViewLifecycleOwner(), day -> {
            if (day != null) updateFields(day);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onResume() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onResume();
    }

    private void removeHistory(int id) {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setIcon(R.drawable.ic_delete)
                .setMessage("Do you want to delete this days entry?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    homeViewModel.removeHistory(id);
                    navController.navigate(R.id.action_navigation_home_to_navigation_history);
                })
                .setNegativeButton("Cancel", null).show();

    }

    private void setDateField(Calendar calendar) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        tvDateSelected.setText(format1.format(calendar.getTime()));
    }

    private void setActiveGoal() {
        List<Goal> goals = homeViewModel.getGoals();
        showSelectGoalDialog(goals, getActivity(), (dialog, which) -> {
            ListView lw = ((AlertDialog) dialog).getListView();
            int index = lw.getCheckedItemPosition();
            if (index != -1) {
                Toast.makeText(getActivity(), "Goal set: " + goals.get(index).getName(), Toast.LENGTH_SHORT).show();
                homeViewModel.setNewActiveGoal(goals.get(index));
            }
        });
    }

    private void addStepsButtonClicked() {
        String stepsToAdd = etAddSteps.getText().toString();
        if (stepsToAdd.trim().equals("")) {
            Toast.makeText(getContext(), "Please add steps", Toast.LENGTH_SHORT).show();
        } else {
            etAddSteps.onEditorAction(EditorInfo.IME_ACTION_DONE);
            homeViewModel.addToHistory(Integer.parseInt(etAddSteps.getText().toString()));
            Toast.makeText(getContext(), stepsToAdd + " steps added", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFields(HistoryEntity history) {
        dateSelected = Calendar.getInstance();
        dateSelected.setTimeInMillis(history.getDay());
        setDateField(dateSelected);
        tvStepsTaken.setText(String.valueOf(history.getStepsTaken()));
        tvActiveGoal.setText(history.getGoalName());
        tvActiveGoalSteps.setText(String.valueOf(history.getGoalSteps()));
        int progressPercentage = (int) 100.0 * history.getStepsTaken() / history.getGoalSteps();
        tvProgress.setText(String.valueOf(progressPercentage));
        progressBar.setProgress(progressPercentage, true);
    }
}