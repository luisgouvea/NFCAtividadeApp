package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TarefaPrecedente extends Tarefa {

    private int IdTarefaPrecedente; //PK
    private int IdTarefaAntecessora;

    public TarefaPrecedente(Parcel in) {
        super(in);
        IdTarefaAntecessora = in.readInt();
        IdTarefaPrecedente = in.readInt();
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

    /**
     * PK da tabela TarefaPrecedente
     * @return ID da tarefa precedente
     */
    public int getIdTarefaPrecedente() {
        return IdTarefaPrecedente;
    }

    public void setIdTarefaPrecedente(int idTarefaPrecedente) {
        IdTarefaPrecedente = idTarefaPrecedente;
    }

    public int getIdTarefaAntecessora() {
        return IdTarefaAntecessora;
    }

    public void setIdTarefaAntecessora(int idTarefaAntecessora) {
        IdTarefaAntecessora = idTarefaAntecessora;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IdTarefaAntecessora);
        parcel.writeInt(IdTarefaPrecedente);
    }
}
