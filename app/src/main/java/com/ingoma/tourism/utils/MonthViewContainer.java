package com.ingoma.tourism.utils;

import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import com.kizitonwose.calendar.view.ViewContainer;
import com.ingoma.tourism.R;

public class MonthViewContainer extends ViewContainer {
    public AppCompatTextView tvMonthHeader;

    public MonthViewContainer(View view) {
        super(view);
        // Ensure this ID matches the AppCompatTextView in your layout
        tvMonthHeader = view.findViewById(R.id.tvMonthHeader);
        Log.d("CalendarDebug", "tvMonthHeader initialized: " + (tvMonthHeader != null));
    }
}