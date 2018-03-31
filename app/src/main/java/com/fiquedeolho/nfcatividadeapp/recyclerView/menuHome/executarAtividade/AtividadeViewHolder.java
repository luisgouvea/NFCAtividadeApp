package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.views.DetailsAtividadeActivity;


public class AtividadeViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView descricaoAtividade;
    private TextView statusAtividade;
    private TextView popMenu;


    /**
     * Construtor
     */
    public AtividadeViewHolder(View itemView) {
        super(itemView);
        this.descricaoAtividade = (TextView) itemView.findViewById(R.id.text_car_model);
        this.statusAtividade = (TextView) itemView.findViewById(R.id.text_view_details);
        this.popMenu = (TextView) itemView.findViewById(R.id.txtOptionDigit);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Atividade atividade, final OnListClickInteractionListener listener, final OnListClickInteractionListener listenerOptions ) {

        // Altera valor
        this.descricaoAtividade.setText(atividade.getNome());
        this.statusAtividade.setText(atividade.getNome());


        // Adciona evento de click
        this.descricaoAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(atividade.getId());
            }
        });

        /*PopupMenu popupMenu = new PopupMenu(mContext, popMenu);
        popupMenu.inflate(R.menu.options_list_ativ_executar);
        popupMenu.show();*/
        popMenu.setId(atividade.getId());
        this.popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerOptions.onClick(view.getId());
            }
        });

        /*this.popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display option menu
                PopupMenu popupMenu = new PopupMenu(mContext, popMenu);
                popupMenu.inflate(R.menu.options_list_ativ_executar);
                popupMenu.setOnMenuItemClickListener(listenerOptions);
                *//*popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.mnu_item_save:
                                Toast.makeText(mContext, "Saved", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_item_delete:
                                //Delete item
                                *//**//*listItems.remove(position);
                                notifyDataSetChanged();*//**//*
                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });*//*
                popupMenu.show();
            }
        });*/
    }
}
