package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TarefaPrecedente extends Tarefa {

    private int IdTarefaAntecessora;

    public TarefaPrecedente(Parcel in) {
        super();
    }

    public TarefaPrecedente(){
        super();
    }

    public static final Parcelable.Creator<TarefaPrecedente> CREATOR = new Parcelable.Creator<TarefaPrecedente>() {
        @Override
        public TarefaPrecedente createFromParcel(Parcel in) {
            return new TarefaPrecedente(in);
        }

        @Override
        public TarefaPrecedente[] newArray(int size) {
            return new TarefaPrecedente[size];
        }
    };

    public int getIdTarefaAntecessora() {
        return IdTarefaAntecessora;
    }

    public void setIdTarefaAntecessora(int idTarefaAntecessora) {
        IdTarefaAntecessora = idTarefaAntecessora;
    }
}
