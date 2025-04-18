package com.ingoma.tourism;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.ingoma.tourism.adapter.PhotoPagerAdapter;
import java.util.ArrayList;

public class PagerGalleryActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private PhotoPagerAdapter adapter;
    private TextView gallery_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_gallery);

        // Initialize views
        viewPager = findViewById(R.id.viewPager);
        gallery_toolbar_title = findViewById(R.id.gallery_toolbar_title);

        // This is crucial for proper insets handling
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        View RootLayout = findViewById(R.id.rootLyt);
        View toolbar = findViewById(R.id.toolbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        String property_name= getIntent().getStringExtra("property_or_room_name");
        String picture_type=getIntent().getStringExtra("picture_type");

        if (imageUrls != null) {

            gallery_toolbar_title.setText(property_name);
            adapter = new PhotoPagerAdapter(imageUrls,picture_type);
            viewPager.setAdapter(adapter);

            // Set up page change callback (for indicators)
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                }
            });
        }

    }

    private void paddingStatusBar(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            v.setPadding(v.getPaddingLeft(),
                    statusBarHeight,
                    v.getPaddingRight(),
                    v.getPaddingBottom());
            return insets;
        });
        ViewCompat.requestApplyInsets(view);
    }

    private void paddingBottomNavigationBar(View layout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setOnApplyWindowInsetsListener(layout, (v, insets) -> {
                int bottomInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
                v.setPadding(v.getPaddingLeft(),
                        v.getPaddingTop(),
                        v.getPaddingRight(),
                        bottomInset);
                return insets;
            });
            ViewCompat.requestApplyInsets(layout);
        } else {
            // Fallback for very old devices
            int navBarHeight = getNavigationBarHeightLegacy(layout.getContext());
            layout.setPadding(layout.getPaddingLeft(),
                    layout.getPaddingTop(),
                    layout.getPaddingRight(),
                    navBarHeight);
        }
    }

    @SuppressLint("InternalInsetResource")
    private int getNavigationBarHeightLegacy(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0;
    }
}