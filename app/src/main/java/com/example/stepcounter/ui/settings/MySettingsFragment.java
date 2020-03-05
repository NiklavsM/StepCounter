package com.example.stepcounter.ui.settings;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.stepcounter.R;
import com.example.stepcounter.repositories.HistoryRepository;

public class MySettingsFragment extends PreferenceFragmentCompat {
    private Preference deleteHistory;
    private HistoryRepository historyRepository;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyRepository = HistoryRepository.getInstance(getActivity().getApplication());
        deleteHistory = findPreference(getString(R.string.delete_all_history));
        if (deleteHistory != null) {
            deleteHistory.setOnPreferenceClickListener(p -> {
                onDeleteHistory();
                return true;
            });
        }
    }

    private void onDeleteHistory() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.are_you_sure_delete_all_history))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    historyRepository.deleteAllHistory();
                    Toast.makeText(getContext(), getString(R.string.history_deleted), Toast.LENGTH_LONG).show();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }
}
