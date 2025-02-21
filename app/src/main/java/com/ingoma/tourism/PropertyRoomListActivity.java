package com.ingoma.tourism;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ingoma.tourism.adapter.RoomAdapter;
import com.ingoma.tourism.model.Plan;
import com.ingoma.tourism.model.Room;

import java.util.ArrayList;
import java.util.List;

public class PropertyRoomListActivity extends AppCompatActivity {

    private RecyclerView rvRooms;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;

    private ImageView imgShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_room_list);

        //padding status bar and bottom navigation bar
        View RootLayout = findViewById(R.id.cordinatorLyt);
        View toolbar = findViewById(R.id.htab_appbar);
        paddingStatusBar(toolbar);
        paddingBottomNavigationBar(RootLayout);

        // initialisation
        rvRooms = findViewById(R.id.rvSrpList);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));

        // Load rooms data
        roomList = getRoomList();
        roomAdapter = new RoomAdapter(this, roomList);
        rvRooms.setAdapter(roomAdapter);

        //jjjj
        imgShare=(ImageView) findViewById(R.id.imgShare);
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PropertyRoomListActivity.this, ConfirmHotelBookingActivity.class));
            }
        });


    }

    public int getNavigationBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11+
            WindowInsets insets = activity.getWindowManager().getCurrentWindowMetrics().getWindowInsets();
            return insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6+
            View decorView = activity.getWindow().getDecorView();
            return decorView.getRootWindowInsets().getStableInsetBottom();
        } else { // Older versions (pre-Marshmallow)
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            return resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0;
        }
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        // Convert 10dp to pixels
        int extraHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                Resources.getSystem().getDisplayMetrics()
        );

        return statusBarHeight + extraHeight;
    }

    private void paddingStatusBar(View layout){
        int statusBarHeight = getStatusBarHeight();
        layout.setPadding(layout.getPaddingLeft(), statusBarHeight, layout.getPaddingRight(), layout.getPaddingBottom());

    }
    private void paddingBottomNavigationBar(View layout){

        // Get the system bottom inset (dynamic)
        int navBarHeight = getNavigationBarHeight(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            layout.setOnApplyWindowInsetsListener((v, insets) -> {

                v.setPadding(0, 0, 0, navBarHeight);
                return insets;
            });
        } else {

            layout.setPadding(0, 0, 0, navBarHeight);
        }
    }

    private List<Room> getRoomList() {
        List<Room> rooms = new ArrayList<>();

        // Sample plans for each room
        List<Plan> plans1 = new ArrayList<>();
        plans1.add(new Plan("Room Only", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.", 50.0));
        plans1.add(new Plan("Room with Breakfast", "Includes breakfast", 65.0));

        List<Plan> plans2 = new ArrayList<>();
        plans2.add(new Plan("Room Only", "Basic stay with no meals", 80.0));
        plans2.add(new Plan("Room with Breakfast", "Includes continental breakfast", 95.0));

        // Sample rooms
        rooms.add(new Room(
                "Deluxe Room",
                "https://cf.bstatic.com/xdata/images/hotel/max1024x768/228178704.jpg?k=d0efcd7d4bc8db6181e0cf44aa998dca0507bc74150c28e44805a2f655eb258c&o=&hp=1",
                5,
                2,
                "King Bed",
                30.5,
                "WiFi, TV, Air Conditioner",
                plans1
        ));

        rooms.add(new Room(
                "Suite Room",
                "https://cf.bstatic.com/xdata/images/hotel/max1024x768/228175786.jpg?k=cce91514a27959bbe50c8576c6b3046bf1c4ee474f62a6ed07f86bbb479a25e0&o=&hp=1",
                8,
                4,
                "2 Queen Beds",
                50.0,
                "WiFi, TV, Minibar, Jacuzzi",
                plans2
        ));

        return rooms;
    }
}