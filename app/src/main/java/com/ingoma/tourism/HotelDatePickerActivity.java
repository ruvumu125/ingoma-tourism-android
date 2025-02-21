package com.ingoma.tourism;

import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.ingoma.tourism.utils.DayViewContainer;
import com.ingoma.tourism.utils.MonthViewContainer;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.core.OutDateStyle;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;



public class HotelDatePickerActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private MaterialButton btnShowDates;
    private AppCompatTextView tvSelectedDates;
    private LocalDate startDate = null;
    private LocalDate endDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_date_picker);

        calendarView = findViewById(R.id.calendarView);
        tvSelectedDates = findViewById(R.id.tvDayCount);
        btnShowDates= findViewById(R.id.btnDone);

        // Set up the calendar
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth;
        //YearMonth startMonth = currentMonth.minusMonths(12); // Start from 12 months ago
        YearMonth endMonth = currentMonth.plusMonths(12); // End 12 months later

        // Define the days of the week
        List<DayOfWeek> daysOfWeek = Arrays.asList(DayOfWeek.values());

        // Use the first day of the week (e.g., DayOfWeek.MONDAY)
        calendarView.setup(startMonth, endMonth, daysOfWeek.get(0));

        // Set the MonthDayBinder
        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(DayViewContainer container, CalendarDay data) {

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                if (data.getPosition() != DayPosition.MonthDate || YearMonth.from(data.getDate()).isBefore(currentMonth) ||data.getDate().isBefore(currentDate)) {

                    container.textView.setTextColor(Color.GRAY);
                    container.textView.setAlpha(0.5f);
                    container.textView.setClickable(false);
                } else {

                    container.textView.setTextColor(Color.BLACK);
                    container.textView.setAlpha(1f);
                    container.textView.setClickable(true);

                }


                // Set the day of the month
                container.textView.setText(String.valueOf(data.getDate().getDayOfMonth()));
            }
        });

        // Set the MonthHeaderBinder
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @Override
            public void bind(@NonNull MonthViewContainer container, CalendarMonth data) {

                Log.d("CalendarDebug", "Binding month: " + data.getYearMonth());
                if (container.tvMonthHeader != null && data != null && data.getYearMonth() != null) {
                    // Format the YearMonth as "February 2025"
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
                    String formattedMonth = data.getYearMonth().format(formatter);
                    container.tvMonthHeader.setText(formattedMonth);
                } else {
                    Log.e("CalendarDebug", "Null value detected in month header bind method");
                }
            }

            @Override
            public MonthViewContainer create(View view) {
                return new MonthViewContainer(view);
            }
        });

        // Scroll to the current month
        calendarView.scrollToMonth(currentMonth);

    }

    private void selectDate(LocalDate date) {
        if (startDate == null || (startDate != null && endDate != null)) {
            // Reset selection and set start date
            startDate = date;
            endDate = null;
        } else if (date.isAfter(startDate)) {
            // Set end date if after start date
            endDate = date;
        } else {
            // Reset if selecting a date before startDate
            startDate = date;
            endDate = null;
        }

        // Refresh calendar UI
        calendarView.notifyCalendarChanged();

        // Update TextView immediately
        updateSelectedDatesText();
    }

    private void showSelectedDates() {
        updateSelectedDatesText();
    }

    private void updateSelectedDatesText() {
        if (startDate == null) {
            tvSelectedDates.setText("Selected Dates: None");
        } else if (endDate == null) {
            tvSelectedDates.setText("Selected Date: " + startDate.toString());
        } else {
            tvSelectedDates.setText("Selected Dates: " + startDate.toString() + " - " + endDate.toString());
        }
    }






}