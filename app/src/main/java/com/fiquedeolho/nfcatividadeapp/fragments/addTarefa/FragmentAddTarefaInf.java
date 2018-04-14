package com.fiquedeolho.nfcatividadeapp.fragments.addTarefa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.util.Mask;

public class FragmentAddTarefaInf extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_add_tarefa_inf, container, false);
        EditText dataFinalizaElement = view.findViewById(R.id.input_data_finalizacao_tarefa);
        dataFinalizaElement.addTextChangedListener(Mask.insert("##/##/####", dataFinalizaElement));
        return view;
    }
}
