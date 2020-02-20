package com.example.stepcounter.ui.history;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rv_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        btnAddHistory = root.findViewById(R.id.btn_add_history);

        final HistoryAdapter historyAdapter = new HistoryAdapter();
        historyAdapter.setOnItemClickListener(history -> {
            Bundle bundle = new Bundle();
            bundle.putInt(HISTORY_ID, history.getId());
            navController.navigate(R.id.action_navigation_history_to_navigation_home, bundle);
        });
        recyclerView.setAdapter(historyAdapter);

        historyViewModel.getHistory().observe(getViewLifecycleOwner(), historyAdapter::setHistory);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                HistoryEntity historyToRemove = historyAdapter.getHistoryAtIndex(viewHolder.getAdapterPosition());
                historyViewModel.deleteHistory(historyToRemove);
                showUndoSnackbar(historyToRemove);
            }


        }).attachToRecyclerView(recyclerView);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        btnAddHistory.setOnClickListener(v -> addHistory());
    }

    private void showUndoSnackbar(final HistoryEntity historyEntity) {
        Snackbar snackbar = Snackbar.make(getView(), "Entry removed",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> historyViewModel.addHistory(historyEntity));
        snackbar.show();
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

}