package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.DetalhesAtividade;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.AtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.util.Convert;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class DetalhesAtividadeActivity extends AppCompatActivity {

    private ViewHolderDetalhesAtividade mViewHolderDetalhesAtividade = new ViewHolderDetalhesAtividade();
    private ProgressDialog pDialog;
    public static int idAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_ativ_inf);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        capturaElementos();
        disableActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDetalhesAtividade();
    }

    public void capturaElementos() {
        mViewHolderDetalhesAtividade.mViewContentFirstLinear = findViewById(R.id.first_content_linear_layout_add_ativ);

        mViewHolderDetalhesAtividade.mViewNomeAtividade = findViewById(R.id.input_nomeAtividade);
        mViewHolderDetalhesAtividade.mViewDataFinalizacao = findViewById(R.id.input_data_finalizacao_ativ);

        mViewHolderDetalhesAtividade.mViewFormExecAtivFinaliza = findViewById(R.id.forma_execucao_tarefa_finaliza);
        mViewHolderDetalhesAtividade.mViewFormExecCiclica = findViewById(R.id.forma_execucao_por_dia);


        mViewHolderDetalhesAtividade.mViewContentExec = findViewById(R.id.content_opcoes_forma_execucao_dia);
        mViewHolderDetalhesAtividade.mViewExecTodoDiaSim = findViewById(R.id.execucao_todo_dia_sim);
        mViewHolderDetalhesAtividade.mViewExecTodoDiaNao = findViewById(R.id.execucao_todo_dia_nao);

        mViewHolderDetalhesAtividade.mViewContentDiaEspecifico = findViewById(R.id.content_dia_especifico);
        mViewHolderDetalhesAtividade.mViewDiaEspecifico = findViewById(R.id.editText_dia_especifico);

        mViewHolderDetalhesAtividade.mViewContentRepFluxo = findViewById(R.id.content_repeticao_fluxo_completo);
        mViewHolderDetalhesAtividade.mViewFluxoRepSemLimite = findViewById(R.id.repeti_fluxo_compl_semLimite);
        mViewHolderDetalhesAtividade.mViewFluxoRepQtdEsp = findViewById(R.id.repeti_fluxo_compl_qtdEsp);

        mViewHolderDetalhesAtividade.mViewQtdNumRepeticoes = findViewById(R.id.qtd_numero_repeticoes_fluxo_completo);
        mViewHolderDetalhesAtividade.mViewNumRepeticoes = findViewById(R.id.editText_repeticao_fluxo_rept_fluxo);

        mViewHolderDetalhesAtividade.mViewContentNomeExecutor = findViewById(R.id.content_nome_executor);
        mViewHolderDetalhesAtividade.mViewNomeExecutor = findViewById(R.id.input_nomeExecutor);
    }

    public void disableActivity() {
        mViewHolderDetalhesAtividade.mViewNomeAtividade.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewDataFinalizacao.setEnabled(false);

        mViewHolderDetalhesAtividade.mViewFormExecAtivFinaliza.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewFormExecCiclica.setEnabled(false);
        
        mViewHolderDetalhesAtividade.mViewContentExec.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewExecTodoDiaSim.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewExecTodoDiaNao.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewDiaEspecifico.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewContentDiaEspecifico.setEnabled(false);

        mViewHolderDetalhesAtividade.mViewContentRepFluxo.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewFluxoRepSemLimite.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewFluxoRepQtdEsp.setEnabled(false);

        mViewHolderDetalhesAtividade.mViewQtdNumRepeticoes.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewNumRepeticoes.setEnabled(false);

        mViewHolderDetalhesAtividade.mViewNomeExecutor.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewContentNomeExecutor.setEnabled(false);
    }

    private void getDetalhesAtividade() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setTitle("Buscando Atividade");
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        AtividadeRetrofit atividadeInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<DetalhesAtividade> call = atividadeInterface.getDetalhesAtividade(idAtividade);
        call.enqueue(new Callback<DetalhesAtividade>() {
            @Override
            public void onResponse(Call<DetalhesAtividade> call, retrofit2.Response<DetalhesAtividade> response) {
                if (response.code() == 200) {
                    DetalhesAtividade detalhesAtividade = response.body();
                    setarInformacoes(detalhesAtividade);
                } else {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    /*APIError error = ErrorUtils.parseError(response);
                    dialogDefaultErro.setTextErro(error.message());
                    dialogDefaultErro.show(getSupportFragmentManager(),"dialog");*/
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DetalhesAtividade> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                /*dialogDefaultErro.setTextErro(t.getMessage());
                dialogDefaultErro.show(getSupportFragmentManager(),"dialog");*/
            }
        });
    }

    public void setarInformacoes(DetalhesAtividade detalhesAtividade) {
        Atividade atividade = detalhesAtividade.getAtividade();
        String nomeExecutor = detalhesAtividade.getNomeExecutor();
        String nomeAtiv = atividade.getNome();
        Date dataFinalizacao = atividade.getDataFinalizacao();
        int modoExec = atividade.getIdModoExecucao();
        String diaExec = atividade.getDiaExecucao();
        int qtdLimiteExecFluxo = atividade.getNumMaximoCiclo();


        mViewHolderDetalhesAtividade.mViewContentFirstLinear.setEnabled(false);
        mViewHolderDetalhesAtividade.mViewContentFirstLinear.setFocusable(false);

        mViewHolderDetalhesAtividade.mViewNomeAtividade.setText(nomeAtiv);
        if(dataFinalizacao.getYear() > 0) {
            mViewHolderDetalhesAtividade.mViewDataFinalizacao.setText(Convert.formatDate(dataFinalizacao));
        } else{
            mViewHolderDetalhesAtividade.mViewDataFinalizacao.setText("Não preenchido");
        }

        /**
         * Mode de execucao
         */
        if (modoExec == 1) {
            mViewHolderDetalhesAtividade.mViewFormExecAtivFinaliza.setChecked(true);
        } else {
            mViewHolderDetalhesAtividade.mViewFormExecCiclica.setChecked(true);

            mViewHolderDetalhesAtividade.mViewContentExec.setVisibility(View.VISIBLE);

            /**
             * Qual dia?
             */
            if (diaExec != null && !diaExec.equals("")) {
                mViewHolderDetalhesAtividade.mViewExecTodoDiaNao.setChecked(true);
                mViewHolderDetalhesAtividade.mViewContentDiaEspecifico.setVisibility(View.VISIBLE);
                mViewHolderDetalhesAtividade.mViewDiaEspecifico.setText(diaExec);
            } else {
                mViewHolderDetalhesAtividade.mViewExecTodoDiaSim.setChecked(true);
            }


            /**
             * Repeticao Fluxo
             */

            mViewHolderDetalhesAtividade.mViewContentRepFluxo.setVisibility(View.VISIBLE);


            if (qtdLimiteExecFluxo == 0) {
                mViewHolderDetalhesAtividade.mViewFluxoRepSemLimite.setChecked(true);
            } else {
                mViewHolderDetalhesAtividade.mViewFluxoRepQtdEsp.setChecked(true);
                mViewHolderDetalhesAtividade.mViewQtdNumRepeticoes.setVisibility(View.VISIBLE);
                mViewHolderDetalhesAtividade.mViewNumRepeticoes.setText(String.valueOf(qtdLimiteExecFluxo));
            }
        }
        mViewHolderDetalhesAtividade.mViewNomeExecutor.setText(nomeExecutor);
        mViewHolderDetalhesAtividade.mViewContentNomeExecutor.setVisibility(View.VISIBLE);
        mViewHolderDetalhesAtividade.mViewNomeExecutor.setVisibility(View.VISIBLE);
    }

    /**
     * ViewHolder dos elementos
     */
    public static class ViewHolderDetalhesAtividade {
        private LinearLayout mViewContentFirstLinear;
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

        private LinearLayout mViewContentNomeExecutor; //content_nome_executor
        private EditText mViewNomeExecutor; //input_nomeExecutor
    }
}
