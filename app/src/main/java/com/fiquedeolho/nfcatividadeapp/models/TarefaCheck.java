package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TarefaCheck extends Tarefa {

    private int IdTarefaCheck;
    private int IdStatusCheckNFC;
    private Date DataExecucao;
    private int Ciclo;

    public TarefaCheck(Parcel in) {
        super(in);
        IdStatusCheckNFC = in.readInt();
        IdTarefaCheck = in.readInt();
        DataExecucao = (java.util.Date) in.readSerializable();
        Ciclo = in.readInt();
    }

    public TarefaCheck(){
        super();
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

    public int getIdTarefaCheck() {
        return IdTarefaCheck;
    }

    public void setIdTarefaCheck(int idTarefaCheck) {
        IdTarefaCheck = idTarefaCheck;
    }

    public int getIdStatusCheckNFC() {
        return IdStatusCheckNFC;
    }

    public void setIdStatusCheckNFC(int idStatusCheckNFC) {
        IdStatusCheckNFC = idStatusCheckNFC;
    }

    public int getCiclo() {
        return Ciclo;
    }

    public void setCiclo(int ciclo) {
        Ciclo = ciclo;
    }

    public Date getDataExecucao() {
        return DataExecucao;
    }

    public void setDataExecucao(Date dataExecucao) {
        DataExecucao = dataExecucao;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(IdTarefaCheck);
        parcel.writeInt(IdStatusCheckNFC);
        parcel.writeLong(DataExecucao != null ? DataExecucao.getTime() : -1 );
        parcel.writeInt(Ciclo);
    }
}
