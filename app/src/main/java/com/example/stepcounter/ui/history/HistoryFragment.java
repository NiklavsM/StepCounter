package com.example.stepcounter.ui.history;

import android.app.DatePickerDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepcounter.R;
import com.example.stepcounter.database.HistoryEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static com.example.stepcounter.ui.home.HomeFragment.HISTORY_ID;
import static com.example.stepcounter.utils.Utils.removeTime;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private FloatingActionButton btnAddHistory;
    private NavController navController;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rv_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        btnAddHistory = root.findViewById(R.id.btn_add_history);
        final HistoryAdapter historyAdapter = new HistoryAdapter();
        historyAdapter.setOnItemClickListener(history -> {
            if (canEditHistory()) {
                openHistory(history.getId());
            } else {
                Toast.makeText(getContext(), getString(R.string.history_edit_mode_disabled), Toast.LENGTH_LONG).show();
            }
        });
        if (canEditHistory()) {
            btnAddHistory.setVisibility(View.VISIBLE);
            historyAdapter.setOnItemClickListener(history -> {
                Bundle bundle = new Bundle();
                bundle.putInt(HISTORY_ID, history.getId());
                navController.navigate(R.id.action_navigation_history_to_navigation_home, bundle);
            });
        }

        recyclerView.setAdapter(historyAdapter);

        historyViewModel.getHistory().observe(getViewLifecycleOwner(), historyAdapter::setHistory);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btnAddHistory.setOnClickListener(v -> addHistory());
    }

    private void addHistory() {
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            Calendar dateSelected = Calendar.getInstance();
            dateSelected.set(Calendar.YEAR, year);
            dateSelected.set(Calendar.MONTH, month);
            dateSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            removeTime(dateSelected);
            long timeSelected = dateSelected.getTimeInMillis();
            HistoryEntity history = historyViewModel.getHistoryByDate(timeSelected);
            Bundle bundle = new Bundle();
            if (history == null) {
                HistoryEntity newHistoryEntry = new HistoryEntity(timeSelected, 0, historyViewModel.getActiveGoal());
                historyViewModel.addHistory(newHistoryEntry);
                bundle.putInt(HISTORY_ID, historyViewModel.getHistoryByDate(timeSelected).getId());
            } else {
                bundle.putInt(HISTORY_ID, history.getId());
            }
            navController.navigate(R.id.action_navigation_history_to_navigation_home, bundle);
        },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private boolean canEditHistory() {
        return sharedPreferences.getBoolean(getString(R.string.history_editing_enabled), true);
    }

    private void openHistory(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(HISTORY_ID, id);
        navController.navigate(R.id.action_navigation_history_to_navigation_home, bundle);
    }

}