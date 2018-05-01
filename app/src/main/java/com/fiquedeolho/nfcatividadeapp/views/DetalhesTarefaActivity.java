package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogDefaultErro;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TAG;

import retrofit2.Call;
import retrofit2.Callback;

public class DetalhesTarefaActivity extends AppCompatActivity {

    private ViewHolderDetalhesTarefa mViewHolderDetalhesTarefa = new ViewHolderDetalhesTarefa();
    private ProgressDialog pDialog;
    public  static int idTarefa;
    private DialogDefaultErro dialogDefaultErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_tarefa);
        this.mViewHolderDetalhesTarefa.mViewHolderInputNomeTag = findViewById(R.id.input_nome_tag_da_tarefa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dialogDefaultErro = DialogDefaultErro.newInstance();
        getDetalhesTAG();
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

    private void getDetalhesTAG() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //pDialog.setTitle(getString(R.string.title_progress_tag_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TagRetrofit tagRetrofit = BaseUrlRetrofit.retrofit.create(TagRetrofit.class);
        final Call<TAG> call = tagRetrofit.getDetalhesTag(idTarefa);
        call.enqueue(new Callback<TAG>() {
            @Override
            public void onResponse(Call<TAG> call, retrofit2.Response<TAG> response) {
                if(response.code() == 200) {
                    TAG tag = response.body();
                    mViewHolderDetalhesTarefa.mViewHolderInputNomeTag.setText(tag.getNome());
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
            public void onFailure(Call<TAG> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                dialogDefaultErro.setTextErro(t.getMessage());
                dialogDefaultErro.show(getSupportFragmentManager(),"dialog");
            }
        });
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

    public class ViewHolderDetalhesTarefa{
        private TextInputEditText mViewHolderInputNomeTag;
    }
}
