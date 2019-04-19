package com.example.prince.multiscreen.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prince.multiscreen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class familyFragment extends Fragment {

    public familyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_family, container, false);
        return rootView;
    }

}
