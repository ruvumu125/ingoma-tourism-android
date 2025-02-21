package com.ingoma.tourism.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingoma.tourism.HotelSearchActivity;
import com.ingoma.tourism.PropertiesListActivity;
import com.ingoma.tourism.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private Button btn_try_again;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btn_try_again=(Button) root.findViewById(R.id.btn_try_again);

        btn_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), HotelSearchActivity.class));

            }
        });
        return root;
    }
}