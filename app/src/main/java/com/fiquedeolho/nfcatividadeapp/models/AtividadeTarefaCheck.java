package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AtividadeTarefaCheck {

    private int Id;
    private int IdAtividade;
    private int IdTarefa;
    private String nomeTarefa;

    public AtividadeTarefaCheck(Parcel in) {
        Id = in.readInt();
        IdAtividade = in.readInt();
        IdTarefa = in.readInt();
    }

    public AtividadeTarefaCheck(){

    }

    public static final Parcelable.Creator<AtividadeTarefaCheck> CREATOR = new Parcelable.Creator<AtividadeTarefaCheck>() {
        @Override
        public AtividadeTarefaCheck createFromParcel(Parcel in) {
            return new AtividadeTarefaCheck(in);
        }

        @Override
        public AtividadeTarefaCheck[] newArray(int size) {
            return new AtividadeTarefaCheck[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdAtividade() {
        return IdAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        IdAtividade = idAtividade;
    }

    public int getIdTarefa() {
        return IdTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        IdTarefa = idTarefa;
    }

    public String getNomeTarefa() {
        return nomeTarefa;
    }

    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }
}
