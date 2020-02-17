package com.example.stepcounter.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;

import java.util.List;

public class GoalSelector {

    public static void showSelectGoalDialog(List<Goal> goals, Activity activity, DialogInterface.OnClickListener onPositiveButtonClicked) {

        String[] goalNames = goals.stream().map(Goal::getName).toArray(String[]::new);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select goal");
        builder.setIcon(R.drawable.ic_star_24px);
        builder.setSingleChoiceItems(goalNames, -1, (dialog, which) -> {
        });
        builder.setPositiveButton("Set", onPositiveButtonClicked);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
