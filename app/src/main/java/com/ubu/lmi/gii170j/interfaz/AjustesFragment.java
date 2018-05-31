package com.ubu.lmi.gii170j.interfaz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubu.lmi.gii170j.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AjustesFragment extends Fragment {


    public AjustesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ajustes, container, false);
    }

}
