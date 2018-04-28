package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TarefaSucessora extends Tarefa {

    private int IdTarefaProxima;
    private int IdTarefaSucedente; // PK

    public TarefaSucessora(Parcel in) {
        super(in);
        IdTarefaProxima = in.readInt();
        IdTarefaSucedente = in.readInt();
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

    /**
     * PK da tabela TarefaSucessora
     * @return ID da tarefa sucessora
     */
    public int getIdTarefaSucedente() {
        return IdTarefaSucedente;
    }

    public void setIdTarefaSucedente(int idTarefaSucedente) {
        IdTarefaSucedente = idTarefaSucedente;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IdTarefaSucedente);
        parcel.writeInt(IdTarefaProxima);
    }
}
