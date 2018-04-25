package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TarefaSucessora extends Tarefa {

    private int IdTarefaProxima;
    private int IdTarefaTarget;

    public TarefaSucessora(Parcel in) {
        super();
        IdTarefaProxima = in.readInt();
        IdTarefaTarget = in.readInt();
    }

    public TarefaSucessora(){
        super();
    }

    public static final Parcelable.Creator<TarefaSucessora> CREATOR = new Parcelable.Creator<TarefaSucessora>() {
        @Override
        public TarefaSucessora createFromParcel(Parcel in) {
            return new TarefaSucessora(in);
        }

        @Override
        public TarefaSucessora[] newArray(int size) {
            return new TarefaSucessora[size];
        }
    };

    public int getIdTarefaProxima() {
        return IdTarefaProxima;
    }

    public void setIdTarefaProxima(int idTarefaProxima) {
        IdTarefaProxima = idTarefaProxima;
    }

    public int getIdTarefaTarget() {
        return IdTarefaTarget;
    }

    public void setIdTarefaTarget(int idTarefaTarget) {
        IdTarefaTarget = idTarefaTarget;
    }
}
