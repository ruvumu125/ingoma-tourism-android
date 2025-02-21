package com.ingoma.tourism;

import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class HotelDatePickerActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private MaterialButton btnShowDates;
    private AppCompatTextView tvSelectedDates;
    private LocalDate startDate = null;
    private LocalDate endDate = null;

    // Declare variables to store check-in and check-out dates
    private CalendarDay checkInDate = null;
    private CalendarDay checkOutDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_date_picker);

        calendarView = findViewById(R.id.calendarView);
        tvSelectedDates = findViewById(R.id.tvDayCount);
        btnShowDates= findViewById(R.id.btnDone);
        // Set click listener for the button
        btnShowDates.setOnClickListener(v -> showSelectedDates());

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

                DayViewContainer container = new DayViewContainer(view);

                // Add click listener to the day view
                container.textView.setOnClickListener(v -> {
                    CalendarDay selectedDay = container.getData(); // Get the selected day
                    Log.d("DateSelection", "Selected date: " + selectedDay.getDate().toString()); // Log the selected date
                    handleDateSelection(selectedDay); // Handle date selection
                });

                return container;
            }

            @Override
            public void bind(DayViewContainer container, CalendarDay data) {

                // Set the CalendarDay data to the container
                container.setData(data);

                // Set the day of the month
                container.textView.setText(String.valueOf(data.getDate().getDayOfMonth()));

                // Reset all dates to default state
                container.textView.setBackgroundColor(Color.TRANSPARENT); // Reset background

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

                // Highlight today's date with a circular background (only for month dates)
                if (data.getDate().isEqual(LocalDate.now()) && data.getPosition() == DayPosition.MonthDate) {
                    container.textView.setBackgroundResource(R.drawable.circle_background_grey);
                    container.textView.setTextColor(Color.WHITE); // Set text color for better visibility
                }

                // Highlight selected dates (check-in and check-out range) - only for month dates
                if (checkInDate != null && checkOutDate != null && data.getPosition() == DayPosition.MonthDate) {
                    if (data.getDate().isAfter(checkInDate.getDate()) && data.getDate().isBefore(checkOutDate.getDate())) {
                        Log.d("BindMethod", "Highlighting range for date: " + data.getDate().toString()); // Log range highlight
                        container.textView.setBackgroundColor(Color.parseColor("#E3F2FD")); // Light blue background for range
                        container.textView.setTextColor(Color.BLACK);
                    }
                }

                // Highlight check-in date - only for month dates
                if (checkInDate != null && data.getDate().isEqual(checkInDate.getDate()) && data.getPosition() == DayPosition.MonthDate) {
                    Log.d("BindMethod", "Highlighting check-in date: " + data.getDate().toString()); // Log check-in highlight
                    container.textView.setBackgroundColor(Color.BLUE);
                    container.textView.setTextColor(Color.WHITE);
                }

                // Highlight check-out date - only for month dates
                if (checkOutDate != null && data.getDate().isEqual(checkOutDate.getDate()) && data.getPosition() == DayPosition.MonthDate) {
                    Log.d("BindMethod", "Highlighting check-out date: " + data.getDate().toString()); // Log check-out highlight
                    container.textView.setBackgroundColor(Color.BLUE);
                    container.textView.setTextColor(Color.WHITE);
                }
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

    // Method to handle date selection
    private void handleDateSelection(CalendarDay selectedDay) {
        if (checkInDate == null) {
            // If check-in date is not set, set it
            checkInDate = selectedDay;
        } else if (checkOutDate == null) {
            // If check-out date is not set
            if (selectedDay.getDate().isAfter(checkInDate.getDate())) {
                // If the new date is after check-in date, set it as check-out date
                checkOutDate = selectedDay;
            } else if (selectedDay.getDate().isBefore(checkInDate.getDate())) {
                // If the new date is before check-in date, reset check-in date and clear check-out date
                checkInDate = selectedDay;
                checkOutDate = null;
            }
        } else {
            // If both check-in and check-out dates are set
            if (selectedDay.getDate().isAfter(checkOutDate.getDate())) {
                // If the new date is after the current check-out date, extend the range
                checkOutDate = selectedDay;
            } else if (selectedDay.getDate().isBefore(checkInDate.getDate())) {
                // If the new date is before the current check-in date, reset check-in date and clear check-out date
                checkInDate = selectedDay;
                checkOutDate = null;
            } else if (selectedDay.getDate().isAfter(checkInDate.getDate()) && selectedDay.getDate().isBefore(checkOutDate.getDate())) {
                // If the new date is between check-in and check-out, decrease the range
                checkOutDate = selectedDay;
            }
        }

        // Calculate the number of days between check-in and check-out
        long daysBetween = calculateDaysBetween(checkInDate, checkOutDate);
        Log.d("DaysBetween", "Days between check-in and check-out: " + daysBetween);

        // Update the TextView with the number of days
        tvSelectedDates.setText("Days between: " + daysBetween);

        // Refresh the calendar to update the UI
        calendarView.notifyCalendarChanged();
        calendarView.invalidate();
        calendarView.requestLayout();
    }

    private long calculateDaysBetween(CalendarDay startDate, CalendarDay endDate) {
        if (startDate == null || endDate == null) {
            return 0; // Return 0 if either date is null
        }

        // Convert CalendarDay to milliseconds
        long startMillis = startDate.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMillis = endDate.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Calculate the difference in milliseconds
        long diffMillis = endMillis - startMillis;

        // Convert milliseconds to days
        return TimeUnit.MILLISECONDS.toDays(diffMillis);
    }

    // Method to display selected dates
    private void showSelectedDates() {
        if (checkInDate == null && checkOutDate == null) {
            //datesTextView.setText("Selected Dates: None");
            Toast.makeText(this, "Selected Dates: None", Toast.LENGTH_SHORT).show();
        } else {
            String checkIn = checkInDate != null ? checkInDate.getDate().toString() : "Not set";
            String checkOut = checkOutDate != null ? checkOutDate.getDate().toString() : "Not set";

            Toast.makeText(this, checkIn, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, checkOut, Toast.LENGTH_SHORT).show();
            //datesTextView.setText("Check-in: " + checkIn + "\nCheck-out: " + checkOut);
        }
    }


}