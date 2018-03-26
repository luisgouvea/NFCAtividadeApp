package com.fiquedeolho.nfcatividadeapp.fragments.menuHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.views.AddAtividadeActivity;


/**
 * Conteudo abaixo do menu.
 * Relacionado a todas as atividades que o usuario logado criou.
 */

public class FragmentHomeAddAtividade extends Fragment implements View.OnClickListener{

    private ViewHolderAddAtivHome mViewHolderAddAtivHome = new ViewHolderAddAtivHome();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_add_ativ, container, false);

        this.mViewHolderAddAtivHome.mViewFloatingActionButton = rootView.findViewById(R.id.btn_addFloatingAction);
        this.mViewHolderAddAtivHome.mViewFloatingActionButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_addFloatingAction) {
            Intent intent = new Intent(view.getContext(), AddAtividadeActivity.class);
            startActivity(intent);
        }
    }


    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddAtivHome {

        private Button mVieBtnFiltrarAtivAdd;
        private RecyclerView mViewRecyclerViewAtividadeAdd;
        private FloatingActionButton mViewFloatingActionButton;
    }
}
