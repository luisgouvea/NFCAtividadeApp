package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioProblemaTarefa;

public class InfNotificacaoProblemaTarefaActivity extends AppCompatActivity {

    private ViewHolderInfNotificacaoProblemaTarefa mViewHolderInfNotificacaoProblemaTarefa = new ViewHolderInfNotificacaoProblemaTarefa();
    public static NotificacaoUsuarioProblemaTarefa notificacaoUsuarioProblemaTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_notificacao_problema_tarefa);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // notificacao add atividade
        if (notificacaoUsuarioProblemaTarefa.getVisualizada() == false) {
            InitialNavigationActivity.countNotificacoesUsu -= 1;
        }

        this.mViewHolderInfNotificacaoProblemaTarefa.mViewTextDescricaoProblema = findViewById(R.id.descricao_problema_inf_problema);
        this.mViewHolderInfNotificacaoProblemaTarefa.mViewCheckBoxCheckNaoRealizado = findViewById(R.id.check_realizado_nao_inf_problema);
        this.mViewHolderInfNotificacaoProblemaTarefa.mViewCheckBoxCheckRealizado = findViewById(R.id.check_realizado_sim_inf_problema);


        if(notificacaoUsuarioProblemaTarefa.getCheckRealizado() == true){
            this.mViewHolderInfNotificacaoProblemaTarefa.mViewCheckBoxCheckRealizado.setChecked(true);
        }else{
            this.mViewHolderInfNotificacaoProblemaTarefa.mViewCheckBoxCheckNaoRealizado.setChecked(true);
        }

        this.mViewHolderInfNotificacaoProblemaTarefa.mViewTextDescricaoProblema.setText(notificacaoUsuarioProblemaTarefa.getDescricaoProblema());

        this.mViewHolderInfNotificacaoProblemaTarefa.mViewTextDescricaoProblema.setEnabled(false);
        this.mViewHolderInfNotificacaoProblemaTarefa.mViewCheckBoxCheckNaoRealizado.setEnabled(false);
        this.mViewHolderInfNotificacaoProblemaTarefa.mViewCheckBoxCheckRealizado.setEnabled(false);
    }



    /**
     * Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToInitialNavigation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void backToInitialNavigation() {
        Intent resultIntent = new Intent(this, InfNotificacoesActivity.class);
        startActivity(resultIntent);
        finish();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfNotificacaoProblemaTarefa {

        private TextView mViewTextDescricaoProblema;
        private CheckBox mViewCheckBoxCheckRealizado;
        private CheckBox mViewCheckBoxCheckNaoRealizado;
    }
}
