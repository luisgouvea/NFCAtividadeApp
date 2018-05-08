package com.fiquedeolho.nfcatividadeapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuario;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioAddAtividade;
import com.fiquedeolho.nfcatividadeapp.retrofit.implementation.AtividadeImplementation;
import com.fiquedeolho.nfcatividadeapp.retrofit.implementation.NotificacaoUsuarioImplementation;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfNotificacaoAddAtividadeActivity<T> extends AppCompatActivity implements Callback<T> {

    public static NotificacaoUsuarioAddAtividade notificacaoUsuarioAddAtividade;
    private ViewHolderInfNotificacaoAddAtividade mViewHolderInfNotificacaoAddAtividade = new ViewHolderInfNotificacaoAddAtividade();
    private AtividadeImplementation atividadeImplementation = new AtividadeImplementation();
    private NotificacaoUsuarioImplementation notificacaoUsuarioImplementation = new NotificacaoUsuarioImplementation();

    private Callback<T> requestRetrofit = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_notificacao_add_atividade);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // notificacao add atividade
        if (notificacaoUsuarioAddAtividade.getVisualizada() == false) {
            InitialNavigationActivity.countNotificacoesUsu -= 1;
        }
        this.mViewHolderInfNotificacaoAddAtividade.mViewTextNomeAtividade = findViewById(R.id.nome_atividade);
        this.mViewHolderInfNotificacaoAddAtividade.mViewTextDataCriacao = findViewById(R.id.data_criacao);
        this.mViewHolderInfNotificacaoAddAtividade.mViewTextStatusAtividade = findViewById(R.id.status_atividade);
        this.mViewHolderInfNotificacaoAddAtividade.mViewTextDescricaoAtividade = findViewById(R.id.descrica_atividade);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getNotificacaoAddAtividade();
        if(notificacaoUsuarioAddAtividade.getVisualizada() == false) {
            updateNotificacaoParaVisualizada();
        }
    }

    private void getNotificacaoAddAtividade() {
        atividadeImplementation.requestSelectObjectById(requestRetrofit, notificacaoUsuarioAddAtividade.getIdAtividade());
    }

    private void updateNotificacaoParaVisualizada() {
        NotificacaoUsuario notiUsu = new NotificacaoUsuario();
        notiUsu.setIdUsuarioNotificado(notificacaoUsuarioAddAtividade.getIdUsuarioNotificado());
        notiUsu.setVisualizada(true);
        notiUsu.setDescricaoNotificacao(notificacaoUsuarioAddAtividade.getDescricaoNotificacao());
        notiUsu.setIdNotificacaoUsuario(notificacaoUsuarioAddAtividade.getIdNotificacaoUsuario());
        notificacaoUsuarioImplementation.requestUpdateObject(requestRetrofit, notiUsu);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        requestAtividade(call, response);
        requestNotificacaoUsuario(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Toast.makeText(getApplicationContext(), "Ocorreu um erro genérico" + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void requestAtividade(Call<T> call, Response<T> response) {
        APIError error = null;
        Atividade atividade = null;
        String typeResponse = atividadeImplementation.findResponse(call, response);
        if (typeResponse != "") {
            switch (typeResponse) {
                case "erro":
                    error = atividadeImplementation.resultError();
                    if (error.message() != null) {
                        Toast.makeText(getApplicationContext(), error.message(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro genérico", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "getAtividadeById":
                    atividade = atividadeImplementation.resultSelectObject();
                    setElementsActivity(atividade);
                    break;
            }
        }
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

    private void setElementsActivity(Atividade atividade) {
        this.mViewHolderInfNotificacaoAddAtividade.mViewTextNomeAtividade.setText(atividade.getNome());
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateString = sdfr.format(atividade.getDataCriacao());
        } catch (Exception ex) {
            System.out.println(ex);
        }

        this.mViewHolderInfNotificacaoAddAtividade.mViewTextDataCriacao.setText(dateString);
        if (atividade.getIdStatus() == 1) {
            this.mViewHolderInfNotificacaoAddAtividade.mViewTextStatusAtividade.setText("Não Executada");
        } else {
            this.mViewHolderInfNotificacaoAddAtividade.mViewTextStatusAtividade.setText("Executada");
        }

        this.mViewHolderInfNotificacaoAddAtividade.mViewTextDescricaoAtividade.setText(atividade.getDescricao());

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
    private class ViewHolderInfNotificacaoAddAtividade {

        private TextView mViewTextNomeAtividade;
        private TextView mViewTextDataCriacao;
        private TextView mViewTextStatusAtividade;
        private TextView mViewTextDescricaoAtividade;
    }
}
