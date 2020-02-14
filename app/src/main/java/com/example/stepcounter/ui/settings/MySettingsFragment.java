package com.example.stepcounter.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.stepcounter.R;
import com.example.stepcounter.repositories.HistoryRepository;

public class MySettingsFragment extends PreferenceFragmentCompat {
    private Preference button;
    private HistoryRepository historyRepository;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyRepository = HistoryRepository.getInstance(getActivity().getApplication());
        button = findPreference(getString(R.string.delete_all_history));
        if (button != null) {
            button.setOnPreferenceClickListener(p -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete all the history?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            historyRepository.deleteAllHistory();
                            Toast.makeText(getContext(), "History deleted", Toast.LENGTH_LONG).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            });
        }

    }
}
