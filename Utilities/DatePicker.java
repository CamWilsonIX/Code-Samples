package com.irrelevxnce.jblgroundscare.Utilities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePicker {
    public static DatePickerDialog createDialog(Context context, EditText datePicker, Calendar calendar, int year, int month, int dayOfMonth) {
        return new DatePickerDialog(
                context,
                (calendarView, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    datePicker.setText(dateFormat.format(calendar.getTime()));
                },
                year, month, dayOfMonth
        );
    }
}
