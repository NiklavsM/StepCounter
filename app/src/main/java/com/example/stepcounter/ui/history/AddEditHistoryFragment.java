package com.example.stepcounter.ui.history;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.stepcounter.utils.GoalSelector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.stepcounter.utils.Utils.removeTime;

public class AddEditHistoryFragment extends Fragment {

    static final String HISTORY_ID = "HISTORY_ID";

    private AddEditHistoryViewModel viewModel;
    private NavController navController;

    private Button btnSetDate;
    private TextView tvDateSelected;
    private EditText etStepsToAdd;
    private TextView tvActiveGoal;
    private Calendar dateSelected;
    private Button btnSetGoal;
    private Button btnAddHistory;
    private Bundle bundle;
    private ImageView ivRemoveHistory;

    private Goal selectedGoal;
    private HistoryEntity historyToEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_history_fragment, container, false);
        AppCompatActivity acivity = ((AppCompatActivity) getActivity());
        acivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24px);
        btnSetDate = view.findViewById(R.id.btn_select_date);
        tvDateSelected = view.findViewById(R.id.tv_history_date);
        btnAddHistory = view.findViewById(R.id.btn_add_history);
        etStepsToAdd = view.findViewById(R.id.et_history_add_steps);
        tvActiveGoal = view.findViewById(R.id.tv_active_goal_history);
        btnSetGoal = view.findViewById(R.id.btn_set_current_goal_history);
        ivRemoveHistory = view.findViewById(R.id.iv_delete_history);

        dateSelected = Calendar.getInstance();
        setDateField(dateSelected);

        btnSetDate.setOnClickListener(v -> showDatePickerDialog());
        btnAddHistory.setOnClickListener(v -> addHistory());
        btnSetGoal.setOnClickListener(v -> setGoal());

        bundle = getArguments();
        if (bundle != null) {
            acivity.getSupportActionBar().setTitle("Edit history");
            ivRemoveHistory.setVisibility(View.VISIBLE);
            ivRemoveHistory.setOnClickListener(v -> removeHistory(bundle.getInt(HISTORY_ID)));
        }

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
        viewModel = new ViewModelProvider(requireActivity()).get(AddEditHistoryViewModel.class);
        dateSelected = Calendar.getInstance();
        if (bundle != null) {
            historyToEdit = viewModel.getHistory(bundle.getInt(HISTORY_ID));
            dateSelected.setTimeInMillis((long) historyToEdit.getDay());
            tvActiveGoal.setText(historyToEdit.getGoalName());
            selectedGoal = viewModel.getGoalByName(historyToEdit.getGoalName());
        } else {
            selectedGoal = viewModel.getActiveGoal();
        }
        tvActiveGoal.setText(selectedGoal.getName());
        setDateField(dateSelected);
    }

    private void addHistory() {
        String stepsText = etStepsToAdd.getText().toString();
        if (stepsText.trim().equals("")) {
            Toast.makeText(getContext(), "Please specify steps", Toast.LENGTH_LONG).show();
            return;
        }
        int stepsToAdd = Integer.parseInt(stepsText);
        viewModel.addHistory(new HistoryEntity(dateSelected.getTimeInMillis(), stepsToAdd, selectedGoal));
        navController.navigate(R.id.action_addEditHistoryFragment_to_navigation_history);
        Toast.makeText(getContext(), "History added", Toast.LENGTH_LONG).show();
    }

    private void setGoal() {
        List<Goal> goals = viewModel.getGoals();
        GoalSelector.showSelectGoalDialog(goals, getActivity(), (dialog, which) -> {
            ListView lw = ((AlertDialog) dialog).getListView();
            int index = lw.getCheckedItemPosition();
            if (index != -1) {
                selectedGoal = goals.get(index);
                tvActiveGoal.setText(selectedGoal.getName());
            }
        });
    }

    private void removeHistory(int id) {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setIcon(R.drawable.ic_delete)
                .setMessage("Do you want to delete this days entry?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    viewModel.removeHistory(id);
                    navController.navigate(R.id.action_addEditHistoryFragment_to_navigation_history);
                })
                .setNegativeButton("Cancel", null).show();

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            dateSelected = Calendar.getInstance();
            dateSelected.set(Calendar.YEAR, year);
            dateSelected.set(Calendar.MONTH, month);
            dateSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            removeTime(dateSelected);
            setDateField(dateSelected);
        },
                dateSelected.get(Calendar.YEAR),
                dateSelected.get(Calendar.MONTH),
                dateSelected.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setDateField(Calendar calendar) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        tvDateSelected.setText(format1.format(calendar.getTime()));
    }

}
