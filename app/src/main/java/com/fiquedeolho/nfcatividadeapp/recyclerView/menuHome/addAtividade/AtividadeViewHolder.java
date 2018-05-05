package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.addAtividade;

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
    private TextView descricaoAtividade;
    private TextView popMenu;
    public ClickListener clickListner;

    /**
     * Header filtro
     */
    private static final String[] STATUS_ATIVIDADE = new String[]{"Status da Atividade", "Disponível", "Finalizada"};
    private Spinner mViewSpinnerAtivAdd;
    private EditText mViewEditTextDataCriacaoAddAtividade;
    private Button mVieBtnFiltrarAtivAdd;


    /**
     * Construtor
     */
    public AtividadeViewHolder(View itemView) {
        super(itemView);
        this.nomeAtividade = (TextView) itemView.findViewById(R.id.text_title_nome_ativ);
        this.descricaoAtividade = (TextView) itemView.findViewById(R.id.text_subtitle_desc_ativ);
        this.popMenu = (TextView) itemView.findViewById(R.id.txtOptionAtivAdd);

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
    public void bindData(final Atividade atividade, final OnListClickInteractionListener listener, final OnListClickInteractionListenerView listenerOptions, ClickListener listnerFiltro, int position) {
        clickListner = listnerFiltro;
        if(position == 0){
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
        }
        else {
            // Altera valor
            this.nomeAtividade.setText(atividade.getNome());
            this.descricaoAtividade.setText(atividade.getDescricao());


            // Adciona evento de click
            this.nomeAtividade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     * Metodo (onClick) da interface OnListClickInteractionListener,  implementada nesse projeto
                     */
                    listener.onClick(atividade.getId());
                }
            });

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
            clickListner.btnFiltrarClicked(this.mViewSpinnerAtivAdd.getSelectedItemPosition(), this.mViewEditTextDataCriacaoAddAtividade.getText().toString());
        }
    }

    public interface ClickListener {

        void btnFiltrarClicked(int idStatusAtividade, String DataCriacao);
    }
}
