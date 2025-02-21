package com.ingoma.tourism.utils;

import android.view.View;
import android.widget.TextView;

import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.view.ViewContainer;
import com.ingoma.tourism.R;

public class DayViewContainer extends ViewContainer {
    public TextView textView;
    public CalendarDay data;

    public DayViewContainer(View view) {
        super(view);
        textView = view.findViewById(R.id.tvCalendarDay); // Ensure this ID matches your custom layout
    }

    public void setData(CalendarDay data) {
        this.data = data; // Set the CalendarDay data
    }

    public CalendarDay getData() {
        return data; // Get the CalendarDay data
    }
}
