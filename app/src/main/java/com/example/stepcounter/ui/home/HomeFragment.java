package com.example.stepcounter.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.stepcounter.utils.GoalSelector.showSelectGoalDialog;

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
    private Button btnRemoveHistory;
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
        btnRemoveHistory = root.findViewById(R.id.btn_delete_history);
        tvDateSelected = root.findViewById(R.id.tv_date_selected);

        btnAddSteps.setOnClickListener(v -> addStepsButtonClicked());
        ivChangeActiveGoal.setOnClickListener(v -> setActiveGoal());

        setUpViewModel();
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
                .setTitle(getString(R.string.confirmation))
                .setIcon(R.drawable.ic_delete)
                .setMessage(getString(R.string.do_you_want_to_delete_this_days_entry))
                .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                    homeViewModel.removeHistory(id);
                    navController.navigate(R.id.action_navigation_home_to_navigation_history);
                    Toast.makeText(getContext(),getString(R.string.history_entry_removed), Toast.LENGTH_LONG).show();
                })
                .setNegativeButton(getString(R.string.cancel), null).show();

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
                Toast.makeText(getActivity(), getString(R.string.goal_set) + goals.get(index).getName(), Toast.LENGTH_SHORT).show();
                homeViewModel.setNewActiveGoal(goals.get(index));
            }
        });
    }

    private void addStepsButtonClicked() {
        String stepsToAdd = etAddSteps.getText().toString();
        if (stepsToAdd.trim().equals("")) {
            Toast.makeText(getContext(), getString(R.string.toast_please_add_steps), Toast.LENGTH_SHORT).show();
        } else {
            etAddSteps.onEditorAction(EditorInfo.IME_ACTION_DONE);
            homeViewModel.addToHistory(Integer.parseInt(etAddSteps.getText().toString()));
            Toast.makeText(getContext(), stepsToAdd + getString(R.string.steps_added), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupEditToolbar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_24px);
        actionBar.setTitle(getString(R.string.edit_history));
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    private void setUpViewModel() {
        bundle = getArguments();
        int historyId = -1;
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        if (bundle != null) {
            BottomNavigationView navBar = getActivity().findViewById(R.id.nav_view);
            navBar.setVisibility(View.GONE);
            historyId = bundle.getInt(HISTORY_ID);
            btnRemoveHistory.setVisibility(View.VISIBLE);
            int finalHistoryId = historyId;
            btnRemoveHistory.setOnClickListener(v -> removeHistory(finalHistoryId));
            setupEditToolbar();
        }
        homeViewModel.getDay(historyId).observe(getViewLifecycleOwner(), day -> {
            if (day != null) updateFields(day);
        });

    }
}