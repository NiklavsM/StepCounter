package com.example.stepcounter.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static long getTodayNoTime() {
        Calendar calendar = Calendar.getInstance();
        removeTime(calendar);
        return calendar.getTimeInMillis();
    }

    public static String fromLongToDateString(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(calendar.getTimeInMillis());
    }

    public static void removeTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
