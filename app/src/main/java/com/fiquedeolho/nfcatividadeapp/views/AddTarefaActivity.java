package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TarefaRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;

import retrofit2.Call;
import retrofit2.Callback;

public class AddTarefaActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderAddTarefa mViewHolderAddTarefa = new ViewHolderAddTarefa();
    private int IdAtividade;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
        }

        this.mViewHolderAddTarefa.mViewBtnInputNomeTarefa = findViewById(R.id.input_nomeTarefa);
        this.mViewHolderAddTarefa.mViewBtnSalvarTarefa = findViewById(R.id.btn_salvar_tarefa);
        this.mViewHolderAddTarefa.mViewBtnSalvarTarefa.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    /**
     Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToInfTarefas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_salvar_tarefa){
            Tarefa tarefa  = new Tarefa();
            tarefa.setNome(this.mViewHolderAddTarefa.mViewBtnInputNomeTarefa.getText().toString());
            tarefa.setIdAtividade(IdAtividade);
            addTarefa(tarefa);
        }
    }

    private void addTarefa(final Tarefa tarefa) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setTitle(getString(R.string.title_progress_tarefa_add));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TarefaRetrofit tarefaInterface = BaseUrlRetrofit.retrofit.create(TarefaRetrofit.class);
        final Call<Boolean> call = tarefaInterface.addTarefa(tarefa);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                Boolean result = response.body();
                backToInfTarefas();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    private void backToInfTarefas() {
        Intent resultIntent = new Intent(this, InfTarefasActivity.class);
        resultIntent.putExtra("IdAtividade", IdAtividade);
        startActivity(resultIntent);
        finish();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddTarefa {

        private Button mViewBtnSalvarTarefa;
        private EditText mViewBtnInputNomeTarefa;
    }
}
