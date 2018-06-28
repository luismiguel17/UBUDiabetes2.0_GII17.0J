package com.ubu.lmi.gii170j.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubu.lmi.gii170j.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistroListaIngestaFragment extends Fragment {

    public RegistroListaIngestaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registro_lista_ingesta, container, false);
    }
}
