package com.example.stepcounter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.stepcounter.R;
import com.example.stepcounter.database.HistoryEntity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView tvStepsTaken;
    private Button btnAddSteps;
    private EditText etAddSteps;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvStepsTaken = root.findViewById(R.id.tv_home_steps_taken);
        btnAddSteps = root.findViewById(R.id.btn_add_steps);
        etAddSteps = root.findViewById(R.id.et_add_steps);


        btnAddSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stepsToAdd = etAddSteps.getText().toString();
                if (stepsToAdd.trim().equals("")) {
                    Toast.makeText(getContext(), "Please add steps", Toast.LENGTH_SHORT).show();
                } else {
                    etAddSteps.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    homeViewModel.addToHistory(Integer.parseInt(etAddSteps.getText().toString()));
                    Toast.makeText(getContext(), stepsToAdd + " steps added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        homeViewModel.getToday().observe(getViewLifecycleOwner(), new Observer<HistoryEntity>() {
            @Override
            public void onChanged(HistoryEntity today) {
                if (today != null) {
                    tvStepsTaken.setText("Steps taken: " + String.valueOf(today.getStepsTaken())); // TODO fix warning
                } else {
                    homeViewModel.addNewDay();
                }
            }
        });

        return root;
    }
}