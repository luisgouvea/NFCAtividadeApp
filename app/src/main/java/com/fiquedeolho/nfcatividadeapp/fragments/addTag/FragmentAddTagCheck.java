package com.fiquedeolho.nfcatividadeapp.fragments.addTag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;

public class FragmentAddTagCheck extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_add_tag_check, container, false);
        return view;
    }
}
