package com.ingoma.tourism.ui.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingoma.tourism.R;


public class AccountFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_account, container, false);
        return root;
    }
}