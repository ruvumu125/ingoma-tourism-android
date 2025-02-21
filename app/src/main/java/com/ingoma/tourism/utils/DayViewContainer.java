package com.ingoma.tourism.utils;

import android.view.View;
import android.widget.TextView;
import com.kizitonwose.calendar.view.ViewContainer;
import com.ingoma.tourism.R;

public class DayViewContainer extends ViewContainer {
    public TextView textView;

    public DayViewContainer(View view) {
        super(view);
        textView = view.findViewById(R.id.tvCalendarDay); // Ensure this ID matches your custom layout
    }
}
