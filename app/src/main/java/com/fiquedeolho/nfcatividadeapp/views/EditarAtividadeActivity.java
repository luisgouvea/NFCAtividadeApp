package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.AtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.util.Convert;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class EditarAtividadeActivity extends AppCompatActivity {

    private ViewHolderEditarAtividade mViewHolderEditarAtividade = new ViewHolderEditarAtividade();
    public static int idAtividade;
    private ProgressDialog pDialogLoadingPage;
    private ProgressDialog pDialogLoadingSave;
    private static Atividade atividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_ativ_inf);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        capturaElementos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAtividade();
    }


    public void HabilitaInfFormaExecDia(View v) {
        mViewHolderEditarAtividade.mViewContentExec.setVisibility(View.VISIBLE);
        mViewHolderEditarAtividade.mViewContentRepFluxo.setVisibility(View.VISIBLE);
    }


    public void DesabilitaInfFormaExecDia(View v) {
        mViewHolderEditarAtividade.mViewContentExec.setVisibility(View.GONE);
        mViewHolderEditarAtividade.mViewContentRepFluxo.setVisibility(View.GONE);
    }

    public void HabilitarDiaEspecifico(View v) {
        mViewHolderEditarAtividade.mViewContentDiaEspecifico = findViewById(R.id.content_dia_especifico);
        mViewHolderEditarAtividade.mViewContentDiaEspecifico.setVisibility(View.VISIBLE);
    }

    public void DesabilitarDiaEspecifico(View v) {
        mViewHolderEditarAtividade.mViewContentDiaEspecifico = findViewById(R.id.content_dia_especifico);
        mViewHolderEditarAtividade.mViewContentDiaEspecifico.setVisibility(View.GONE);
    }


    public void HabilitarQtdRepeticoes(View v) {
        LinearLayout linear = findViewById(R.id.qtd_numero_repeticoes_fluxo_completo);
        linear.setVisibility(View.VISIBLE);
        final ScrollView scrollView = findViewById(R.id.scrollAddAtiv);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void DesabilitarQtdRepeticoes(View v) {
        LinearLayout linear = findViewById(R.id.qtd_numero_repeticoes_fluxo_completo);
        linear.setVisibility(View.GONE);

        final ScrollView scrollView = findViewById(R.id.scrollAddAtiv);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void capturaElementos() {
        mViewHolderEditarAtividade.mViewContentFirstLinear = findViewById(R.id.first_content_linear_layout_add_ativ);

        mViewHolderEditarAtividade.mViewDescricaoAtividade = findViewById(R.id.desc_atividade);

        mViewHolderEditarAtividade.mViewNomeAtividade = findViewById(R.id.input_nomeAtividade);
        mViewHolderEditarAtividade.mViewDataFinalizacao = findViewById(R.id.input_data_finalizacao_ativ);

        mViewHolderEditarAtividade.mViewFormExecAtivFinaliza = findViewById(R.id.forma_execucao_tarefa_finaliza);
        mViewHolderEditarAtividade.mViewFormExecCiclica = findViewById(R.id.forma_execucao_por_dia);


        mViewHolderEditarAtividade.mViewContentExec = findViewById(R.id.content_opcoes_forma_execucao_dia);
        mViewHolderEditarAtividade.mViewExecTodoDiaSim = findViewById(R.id.execucao_todo_dia_sim);
        mViewHolderEditarAtividade.mViewExecTodoDiaNao = findViewById(R.id.execucao_todo_dia_nao);

        mViewHolderEditarAtividade.mViewContentDiaEspecifico = findViewById(R.id.content_dia_especifico);
        mViewHolderEditarAtividade.mViewDiaEspecifico = findViewById(R.id.editText_dia_especifico);

        mViewHolderEditarAtividade.mViewContentRepFluxo = findViewById(R.id.content_repeticao_fluxo_completo);
        mViewHolderEditarAtividade.mViewFluxoRepSemLimite = findViewById(R.id.repeti_fluxo_compl_semLimite);
        mViewHolderEditarAtividade.mViewFluxoRepQtdEsp = findViewById(R.id.repeti_fluxo_compl_qtdEsp);

        mViewHolderEditarAtividade.mViewQtdNumRepeticoes = findViewById(R.id.qtd_numero_repeticoes_fluxo_completo);
        mViewHolderEditarAtividade.mViewNumRepeticoes = findViewById(R.id.editText_repeticao_fluxo_rept_fluxo);
    }

    private void getAtividade() {
        pDialogLoadingPage = new ProgressDialog(this);
        pDialogLoadingPage.setCancelable(false);
        pDialogLoadingPage.setTitle("Buscando Atividade");
        pDialogLoadingPage.setMessage(getString(R.string.message_progress_dialog));
        pDialogLoadingPage.show();
        AtividadeRetrofit atividadeInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<Atividade> call = atividadeInterface.getAtividadeById(idAtividade);
        call.enqueue(new Callback<Atividade>() {
            @Override
            public void onResponse(Call<Atividade> call, retrofit2.Response<Atividade> response) {
                if (response.code() == 200) {
                    atividade = response.body();
                    setarInformacoes();
                } else {
                    if (pDialogLoadingPage != null && pDialogLoadingPage.isShowing()) {
                        pDialogLoadingPage.dismiss();
                    }
                    /*APIError error = ErrorUtils.parseError(response);
                    dialogDefaultErro.setTextErro(error.message());
                    dialogDefaultErro.show(getSupportFragmentManager(),"dialog");*/
                }
                if (pDialogLoadingPage != null && pDialogLoadingPage.isShowing()) {
                    pDialogLoadingPage.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Atividade> call, Throwable t) {
                if (pDialogLoadingPage != null && pDialogLoadingPage.isShowing()) {
                    pDialogLoadingPage.dismiss();
                }
                /*dialogDefaultErro.setTextErro(t.getMessage());
                dialogDefaultErro.show(getSupportFragmentManager(),"dialog");*/
            }
        });
    }

    public void setarInformacoes() {
        String nomeAtiv = atividade.getNome();
        Date dataFinalizacao = atividade.getDataFinalizacao();
        String descAtividade = atividade.getDescricao();
        int modoExec = atividade.getIdModoExecucao();
        String diaExec = atividade.getDiaExecucao();
        int qtdLimiteExecFluxo = atividade.getNumMaximoCiclo();


        mViewHolderEditarAtividade.mViewContentFirstLinear.setEnabled(false);
        mViewHolderEditarAtividade.mViewContentFirstLinear.setFocusable(false);

        mViewHolderEditarAtividade.mViewNomeAtividade.setText(nomeAtiv);
        mViewHolderEditarAtividade.mViewDescricaoAtividade.setText(descAtividade);

        if(dataFinalizacao.getYear() > 0) {
            mViewHolderEditarAtividade.mViewDataFinalizacao.setText(Convert.formatDate(dataFinalizacao));
        } else{
            mViewHolderEditarAtividade.mViewDataFinalizacao.setText("Não preenchido");
           // mViewHolderEditarAtividade.mViewDataFinalizacao.setVisibility(View.GONE);
        }

        /**
         * Mode de execucao
         */
        if (modoExec == 1) {
            mViewHolderEditarAtividade.mViewFormExecAtivFinaliza.setChecked(true);
        } else {
            mViewHolderEditarAtividade.mViewFormExecCiclica.setChecked(true);

            mViewHolderEditarAtividade.mViewContentExec.setVisibility(View.VISIBLE);

            /**
             * Qual dia?
             */
            if (diaExec != null && !diaExec.equals("")) {
                mViewHolderEditarAtividade.mViewExecTodoDiaNao.setChecked(true);
                mViewHolderEditarAtividade.mViewContentDiaEspecifico.setVisibility(View.VISIBLE);
                mViewHolderEditarAtividade.mViewDiaEspecifico.setText(diaExec);
            } else {
                mViewHolderEditarAtividade.mViewExecTodoDiaSim.setChecked(true);
            }


            /**
             * Repeticao Fluxo
             */

            mViewHolderEditarAtividade.mViewContentRepFluxo.setVisibility(View.VISIBLE);


            if (qtdLimiteExecFluxo == 0) {
                mViewHolderEditarAtividade.mViewFluxoRepSemLimite.setChecked(true);
            } else {
                mViewHolderEditarAtividade.mViewFluxoRepQtdEsp.setChecked(true);
                mViewHolderEditarAtividade.mViewQtdNumRepeticoes.setVisibility(View.VISIBLE);
                mViewHolderEditarAtividade.mViewNumRepeticoes.setText(String.valueOf(qtdLimiteExecFluxo));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu_editar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.salvar_edicoes) {
            alterarAtividade();
        }
        return super.onOptionsItemSelected(item);
    }

    private void alterarAtividade() {
        pDialogLoadingSave = new ProgressDialog(this);
        pDialogLoadingSave.setCancelable(false);
        pDialogLoadingSave.setTitle("Alterando Atividade");
        pDialogLoadingSave.setMessage(getString(R.string.message_progress_dialog));
        pDialogLoadingSave.show();
        getChangeAtividade();
        AtividadeRetrofit atividadeInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<Boolean> call = atividadeInterface.alterarAtividade(atividade);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if (pDialogLoadingSave != null && pDialogLoadingSave.isShowing()) {
                    pDialogLoadingSave.dismiss();
                }
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro, tente novamente, por favor", Toast.LENGTH_LONG).show();
                } else{
                    finish();
                    Toast.makeText(getApplicationContext(), "Alteração realizada", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (pDialogLoadingSave != null && pDialogLoadingSave.isShowing()) {
                    pDialogLoadingSave.dismiss();
                }
                /*dialogDefaultErro.setTextErro(t.getMessage());
                dialogDefaultErro.show(getSupportFragmentManager(),"dialog");*/
            }
        });
    }


    public void getChangeAtividade() {
        EditText nome = findViewById(R.id.input_nomeAtividade);
        if(nome != null && !nome.getText().toString().equals("")){
            atividade.setNome(nome.getText().toString());
        }
        EditText descAtiv = mViewHolderEditarAtividade.mViewDescricaoAtividade;
        if(descAtiv != null && !descAtiv.getText().toString().equals("")){
            atividade.setDescricao(descAtiv.getText().toString());
        }

        /*EditText dataFinalizacao = findViewById(R.id.input_data_finalizacao_ativ);
        if(dataFinalizacao != null && !dataFinalizacao.getText().toString().equals("") && !dataFinalizacao.getText().toString().equals("Não preenchido"))
        {
            atividade.setDataFinalizacao(Convert.formatDate(dataFinalizacao.getText().toString()));
        }*/

        RadioButton ativSequencial = findViewById(R.id.forma_execucao_tarefa_finaliza);
        RadioButton ativCiclica = findViewById(R.id.forma_execucao_por_dia);
        if(ativSequencial.isChecked()){
            atividade.setIdModoExecucao(1);
        } else{
            atividade.setIdModoExecucao(2);
        }

        EditText diaEsp = findViewById(R.id.editText_dia_especifico);
        if(diaEsp != null && !diaEsp.getText().toString().equals("")){
            atividade.setDiaExecucao(diaEsp.getText().toString());
        }

        EditText ed = findViewById(R.id.editText_repeticao_fluxo_rept_fluxo);
        if(ed != null && !ed.getText().toString().equals("")){
            atividade.setNumMaximoCiclo(Integer.parseInt(ed.getText().toString()));
        }
    }

    /**
     * ViewHolder dos elementos
     */
    public static class ViewHolderEditarAtividade {
        private LinearLayout mViewContentFirstLinear;

        private EditText mViewDescricaoAtividade;
        private EditText mViewNomeAtividade;
        private EditText mViewDataFinalizacao;

        private RadioButton mViewFormExecAtivFinaliza;  //forma_execucao_tarefa_finaliza
        private RadioButton mViewFormExecCiclica;  //forma_execucao_por_dia


        private LinearLayout mViewContentExec; //content_opcoes_forma_execucao_dia
        private RadioButton mViewExecTodoDiaSim;  //execucao_todo_dia_sim
        private RadioButton mViewExecTodoDiaNao;  //execucao_todo_dia_nao

        private LinearLayout mViewContentDiaEspecifico; //content_dia_especifico
        private EditText mViewDiaEspecifico;  //editText_dia_especifico


        private LinearLayout mViewContentRepFluxo; //content_repeticao_fluxo_completo
        private RadioButton mViewFluxoRepSemLimite;  //repeti_fluxo_compl_semLimite
        private RadioButton mViewFluxoRepQtdEsp;  //repeti_fluxo_compl_qtdEsp


        private LinearLayout mViewQtdNumRepeticoes; //qtd_numero_repeticoes_fluxo_completo
        private EditText mViewNumRepeticoes; //editText_repeticao_fluxo_rept_fluxo
    }
}
