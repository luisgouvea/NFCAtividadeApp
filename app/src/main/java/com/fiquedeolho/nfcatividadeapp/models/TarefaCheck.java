package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TarefaCheck {

    private int Id;
    private int IdTarefa;
    private String NomeTarefa;

    public TarefaCheck(Parcel in) {
        Id = in.readInt();
        IdTarefa = in.readInt();
        NomeTarefa = in.readString();
    }

    public TarefaCheck(){

    }

    public static final Parcelable.Creator<TarefaCheck> CREATOR = new Parcelable.Creator<TarefaCheck>() {
        @Override
        public TarefaCheck createFromParcel(Parcel in) {
            return new TarefaCheck(in);
        }

        @Override
        public TarefaCheck[] newArray(int size) {
            return new TarefaCheck[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdTarefa() {
        return IdTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        IdTarefa = idTarefa;
    }

    public String getNomeTarefa() {
        return NomeTarefa;
    }

    public void setNomeTarefa(String nomeTarefa) {
        this.NomeTarefa = nomeTarefa;
    }
}
