package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuario;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioProblemaTarefa;
import com.fiquedeolho.nfcatividadeapp.retrofit.implementation.NotificacaoUsuarioImplementation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfNotificacaoProblemaTarefaActivity<T> extends AppCompatActivity implements Callback<T> {

    private ViewHolderInfNotificacaoProblemaTarefa mViewHolderInfNotificacaoProblemaTarefa = new ViewHolderInfNotificacaoProblemaTarefa();
    public static NotificacaoUsuarioProblemaTarefa notificacaoUsuarioProblemaTarefa;
    private Callback<T> requestRetrofit = this;
    private NotificacaoUsuarioImplementation notificacaoUsuarioImplementation = new NotificacaoUsuarioImplementation();

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

    @Override
    protected void onStart() {
        super.onStart();
        if(notificacaoUsuarioProblemaTarefa.getVisualizada() == false) {
            updateNotificacaoParaVisualizada();
        }
    }

    private void updateNotificacaoParaVisualizada() {
        NotificacaoUsuario notiUsu = new NotificacaoUsuario();
        notiUsu.setIdUsuarioNotificado(notificacaoUsuarioProblemaTarefa.getIdUsuarioNotificado());
        notiUsu.setVisualizada(true);
        notiUsu.setDescricaoNotificacao(notificacaoUsuarioProblemaTarefa.getDescricaoNotificacao());
        notiUsu.setIdNotificacaoUsuario(notificacaoUsuarioProblemaTarefa.getIdNotificacaoUsuario());
        notiUsu.setDataNotificacao(notificacaoUsuarioProblemaTarefa.getDataNotificacao());
        notificacaoUsuarioImplementation.requestUpdateObject(requestRetrofit, notiUsu);
    }



    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        requestNotificacaoUsuario(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Toast.makeText(getApplicationContext(), "Ocorreu um erro genérico" + t.getMessage(), Toast.LENGTH_LONG).show();
    }



    private void requestNotificacaoUsuario(Call<T> call, Response<T> response) {
        APIError error = null;
        Boolean realizado = false;
        String typeResponse = notificacaoUsuarioImplementation.findResponse(call, response);
        if (typeResponse != "") {
            switch (typeResponse) {
                case "erro":
                    error = notificacaoUsuarioImplementation.resultError();
                    if (error.message() != null) {
                        Toast.makeText(getApplicationContext(), error.message(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro genérico", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "updateNotificacao":
                    realizado = notificacaoUsuarioImplementation.resultUpdateObject();
                    break;
            }
        }
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
