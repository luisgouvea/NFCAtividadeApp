package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.addAtividade;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;
import com.fiquedeolho.nfcatividadeapp.util.Mask;

public class AtividadeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Elemento de interface
    private TextView nomeAtividade;
    //private TextView descricaoAtividade;
    private TextView popMenu;
    public ClickListener clickListner;
    private TextView statusAtividade;
    private TextView modoExecucao;

    /**
     * Header filtro
     */
    private static final String[] STATUS_ATIVIDADE = new String[]{"Status da Atividade", "Não iniciada", "Em execução", "Finalizada"};
    private Spinner mViewSpinnerAtivAdd;
    private EditText mViewEditTextDataCriacaoAddAtividade;
    private Button mVieBtnFiltrarAtivAdd;


    /**
     * Construtor
     */
    public AtividadeViewHolder(View itemView) {
        super(itemView);
        this.nomeAtividade = (TextView) itemView.findViewById(R.id.text_title_nome_ativ);
        //this.descricaoAtividade = (TextView) itemView.findViewById(R.id.text_subtitle_desc_ativ);
        this.popMenu = (TextView) itemView.findViewById(R.id.txtOptionAtivAdd);
        this.statusAtividade = itemView.findViewById(R.id.text_status_atividade);
        this.modoExecucao = itemView.findViewById(R.id.text_modo_execucao_atividade);

        /**
         * Header filtro
         */
        /*this.mViewSpinnerAtivAdd = itemView.findViewById(R.id.status_spinner_atividade_add);
        ArrayAdapter adp = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_spinner_item, STATUS_ATIVIDADE);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mViewSpinnerAtivAdd.setAdapter(adp);

        mViewEditTextDataCriacaoAddAtividade = itemView.findViewById(R.id.data_criacao_add_ativ);
        mViewEditTextDataCriacaoAddAtividade.addTextChangedListener(Mask.insert("##/##/####", mViewEditTextDataCriacaoAddAtividade));
        mViewEditTextDataCriacaoAddAtividade.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mViewEditTextDataCriacaoAddAtividade.setFocusableInTouchMode(true);
                return false;
            }
        });

        mVieBtnFiltrarAtivAdd = (Button) itemView.findViewById(R.id.btn_filtrar_atividade_add);
        mVieBtnFiltrarAtivAdd.setOnClickListener(this);*/
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Atividade atividade, final OnListClickInteractionListenerView listenerOptions, ClickListener listnerFiltro, int position) {
        clickListner = listnerFiltro;
        if (position == 0) {
            this.mViewSpinnerAtivAdd = itemView.findViewById(R.id.status_spinner_atividade_add);
            ArrayAdapter adp = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_spinner_item, STATUS_ATIVIDADE);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
            mViewSpinnerAtivAdd.setAdapter(adp);

            mViewEditTextDataCriacaoAddAtividade = itemView.findViewById(R.id.data_criacao_add_ativ);
            mViewEditTextDataCriacaoAddAtividade.addTextChangedListener(Mask.insert("##/##/####", mViewEditTextDataCriacaoAddAtividade));
            mViewEditTextDataCriacaoAddAtividade.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    mViewEditTextDataCriacaoAddAtividade.setFocusableInTouchMode(true);
                    return false;
                }
            });

            mVieBtnFiltrarAtivAdd = (Button) itemView.findViewById(R.id.btn_filtrar_atividade_add);
            mVieBtnFiltrarAtivAdd.setOnClickListener(this);
        } else {
            // Altera valor
            this.nomeAtividade.setText(atividade.getNome());
            //this.descricaoAtividade.setText(atividade.getDescricao());
            int idStatus = atividade.getIdStatus();

            if (idStatus == 1) {
                this.statusAtividade.setText("Não iniciada");
                this.statusAtividade.setTextColor(Color.parseColor("#ffc107"));
            } else if (idStatus == 2) {
                this.statusAtividade.setText("Em execução");
                this.statusAtividade.setTextColor(Color.parseColor("#17a2b8"));
            } else {
                this.statusAtividade.setText("Finalizada");
                this.statusAtividade.setTextColor(Color.parseColor("#28a745"));
            }

            int modoExec = atividade.getIdModoExecucao();
            this.modoExecucao.setTextColor(Color.parseColor("#6c757d"));
            if (modoExec == 1) {
                this.modoExecucao.setText("Por fluxo completo");
            } else {
                this.modoExecucao.setText("Por dia");
            }

            popMenu.setId(atividade.getId());
            this.popMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     * Metodo (onClick) da interface OnListClickInteractionListenerView,  implementada nesse projeto
                     * Nesse caso, o View eh um TextView
                     */
                    listenerOptions.onClick(view);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_filtrar_atividade_add) {
            clickListner.btnFiltrarClicked(this.mViewSpinnerAtivAdd.getSelectedItemPosition(), this.mViewEditTextDataCriacaoAddAtividade);
        }
    }

    public interface ClickListener {

        void btnFiltrarClicked(int idStatusAtividade, EditText DataCriacao);
    }
}
