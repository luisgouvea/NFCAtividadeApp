package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogDefaultErro;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.TarefaCheckRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TarefaCheck;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infCheckNFC.RegistroCheckListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InfCheckNFCActivity extends AppCompatActivity {

    private ViewHolderInfCheck mViewHolderInfCheck = new ViewHolderInfCheck();
    private ArrayList<TarefaCheck> listaCheck;
    private RegistroCheckListAdapter registroCheckListAdapter;
    private ProgressDialog pDialog;
    private int IdAtividade;
    private DialogDefaultErro dialogDefaultErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_check_nfc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
        }

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        dialogDefaultErro = DialogDefaultErro.newInstance();
        this.mViewHolderInfCheck.mViewTextListCheckVaziaInfCheck = findViewById(R.id.textListAtividadeTarefaCheckRegistroCheck);
        getListChecks();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if(dialogDefaultErro != null && dialogDefaultErro.isVisible()){
            dialogDefaultErro.dismiss();
        }
    }

    private void getListChecks() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //pDialog.setTitle(getString(R.string.title_progress_tag_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TarefaCheckRetrofit tarefaCheckInterface = BaseUrlRetrofit.retrofit.create(TarefaCheckRetrofit.class);
        final Call<ArrayList<TarefaCheck>> call = tarefaCheckInterface.getAllRegistroCheckNFC(this.IdAtividade);
        call.enqueue(new Callback<ArrayList<TarefaCheck>>() {
            @Override
            public void onResponse(Call<ArrayList<TarefaCheck>> call, retrofit2.Response<ArrayList<TarefaCheck>> response) {
                if(response.code() == 200) {
                    listaCheck = response.body();
                    if (listaCheck == null || listaCheck.size() == 0) {
                        mViewHolderInfCheck.mViewTextListCheckVaziaInfCheck.setVisibility(View.VISIBLE);
                    } else {
                        mViewHolderInfCheck.mViewTextListCheckVaziaInfCheck.setVisibility(View.GONE);
                        SetarRecyclerView();
                    }
                }
                else{
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    APIError error = ErrorUtils.parseError(response);
                    dialogDefaultErro.setTextErro(error.message());
                    dialogDefaultErro.show(getSupportFragmentManager(),"dialog");
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TarefaCheck>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                dialogDefaultErro.setTextErro(t.getMessage());
                dialogDefaultErro.show(getSupportFragmentManager(),"dialog");
            }
        });
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderInfCheck.mViewRecyclerViewInfCheck = findViewById(R.id.recyclerViewRegistroCheck);

        // 2 - Definir adapter passando listagem de tarefas e listener
        registroCheckListAdapter = new RegistroCheckListAdapter(listaCheck);
        this.mViewHolderInfCheck.mViewRecyclerViewInfCheck.setAdapter(registroCheckListAdapter);

        this.mViewHolderInfCheck.mViewRecyclerViewInfCheck.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderInfCheck.mViewRecyclerViewInfCheck.setLayoutManager(linearLayoutManager);
    }

    /**
     Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfCheck {

        private RecyclerView mViewRecyclerViewInfCheck;
        private TextView mViewTextListCheckVaziaInfCheck;
    }
}
