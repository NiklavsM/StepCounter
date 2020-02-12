package com.example.stepcounter.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepcounter.R;
import com.example.stepcounter.database.HistoryEntity;

import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rv_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final HistoryAdapter historyAdapter = new HistoryAdapter();
        recyclerView.setAdapter(historyAdapter);

        historyViewModel.getHistory().observe(getViewLifecycleOwner(), historyEntities -> historyAdapter.setHistory(historyEntities));

        return root;
    }
}