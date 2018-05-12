package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogDefaultErro;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioProblemaTarefa;
import com.fiquedeolho.nfcatividadeapp.retrofit.implementation.NotificacaoUsuarioProblemaTarefaImplementation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinalizarProblemaTarefaActivity<T> extends AppCompatActivity implements Callback<T>, View.OnClickListener {

    private ViewHolderSinalizarProblemaTarefa mViewHolderSinalizarProblemaTarefa = new ViewHolderSinalizarProblemaTarefa();
    private NotificacaoUsuarioProblemaTarefaImplementation notificacaoUsuarioProblemaTarefaImplementation = new NotificacaoUsuarioProblemaTarefaImplementation();
    private DialogDefaultErro dialogDefaultErro;
    private Callback<T> requestRetrofit = this;
    public static int idTarefa;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinalizar_problema_tarefa);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogDefaultErro = DialogDefaultErro.newInstance();

        this.mViewHolderSinalizarProblemaTarefa.mViewCheckBoxSim = findViewById(R.id.check_realizado_sim);
        this.mViewHolderSinalizarProblemaTarefa.mViewCheckBoxNao = findViewById(R.id.check_realizado_nao);
        this.mViewHolderSinalizarProblemaTarefa.mViewTextDescricaoProblema = findViewById(R.id.descricao_problema);
        this.mViewHolderSinalizarProblemaTarefa.mViewButtonSinalizarProblema = findViewById(R.id.btn_sinalizar_problema);
        this.mViewHolderSinalizarProblemaTarefa.mViewButtonSinalizarProblema.setOnClickListener(this);
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
        Intent resultIntent = new Intent(this, InitialNavigationActivity.class);
        startActivity(resultIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_sinalizar_problema) {
            NotificacaoUsuarioProblemaTarefa notificacaoUsuarioProblemaTarefa = new NotificacaoUsuarioProblemaTarefa();
            notificacaoUsuarioProblemaTarefa.DescricaoProblema = this.mViewHolderSinalizarProblemaTarefa.mViewTextDescricaoProblema.getText().toString();
            notificacaoUsuarioProblemaTarefa.CheckRealizado = false;
            notificacaoUsuarioProblemaTarefa.IdTarefa = idTarefa;
            if (this.mViewHolderSinalizarProblemaTarefa.mViewCheckBoxSim.isChecked()) {
                notificacaoUsuarioProblemaTarefa.CheckRealizado = true;
            }
            criarNotificacaoProblema(notificacaoUsuarioProblemaTarefa);
        }
    }

    private void criarNotificacaoProblema(NotificacaoUsuarioProblemaTarefa notificacao) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        notificacaoUsuarioProblemaTarefaImplementation.requestInsertObject(requestRetrofit, notificacao);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (dialogDefaultErro != null && dialogDefaultErro.isVisible()) {
            dialogDefaultErro.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (dialogDefaultErro != null && dialogDefaultErro.isVisible()) {
            dialogDefaultErro.dismiss();
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        requestSinalizarProblema(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        dialogDefaultErro.setTextErro(t.getMessage());
        dialogDefaultErro.show(getSupportFragmentManager(), "dialog");
    }

    private void requestSinalizarProblema(Call<T> call, Response<T> response) {
        APIError error = null;
        String typeResponse = notificacaoUsuarioProblemaTarefaImplementation.findResponse(call, response);
        if (typeResponse != "") {
            switch (typeResponse) {
                case "erro":
                    error = notificacaoUsuarioProblemaTarefaImplementation.resultError();
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    if (error.message() != null) {
                        dialogDefaultErro.setTextErro(error.message());
                        dialogDefaultErro.show(getSupportFragmentManager(), "dialog");
                    } else {
                        dialogDefaultErro.setTextErro("Ocorreu um erro genérico");
                        dialogDefaultErro.show(getSupportFragmentManager(), "dialog");
                    }
                    break;
                case "addNotificacaoProblemaTarefa":
                    boolean adicionou = notificacaoUsuarioProblemaTarefaImplementation.resultInsertObject();
                    if (adicionou) {
                        Toast.makeText(getApplicationContext(), "Notificação enviada para o criador da Atividade", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        dialogDefaultErro.setTextErro("Notificação não enviada, tente novamente após alguns minutos!");
                        dialogDefaultErro.show(getSupportFragmentManager(), "dialog");
                    }

                    break;
            }
        }
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderSinalizarProblemaTarefa {

        private CheckBox mViewCheckBoxSim;
        private CheckBox mViewCheckBoxNao;
        private TextView mViewTextDescricaoProblema;
        private Button mViewButtonSinalizarProblema;
    }
}
