package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Atividade implements Parcelable {

    private String Nome;
    private String Descricao;
    private int Id;
    private int IdUsuarioCriador;
    private int IdUsuarioExecutor;
    private ArrayList<Tarefa> listTarefas;

    public Atividade() {
        listTarefas = new ArrayList<Tarefa>();
    }


    protected Atividade(Parcel in) {
        Nome = in.readString();
        Descricao = in.readString();
        Id = in.readInt();
        IdUsuarioCriador = in.readInt();
        IdUsuarioExecutor = in.readInt();
        listTarefas = in.createTypedArrayList(Tarefa.CREATOR);
    }

    public static final Creator<Atividade> CREATOR = new Creator<Atividade>() {
        @Override
        public Atividade createFromParcel(Parcel in) {
            return new Atividade(in);
        }

        @Override
        public Atividade[] newArray(int size) {
            return new Atividade[size];
        }
    };

    /**
     * GETS
     */
    public String getNome() {
        return Nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public int getId() {
        return Id;
    }

    public ArrayList<Tarefa> getListTarefas() {
        return listTarefas;
    }

    public int getIdUsuarioCriador() {
        return IdUsuarioCriador;
    }

    public int getIdUsuarioExecutor() {
        return IdUsuarioExecutor;
    }

    /**
     * SETS
     */
    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setDescricao(String descricao) {
        this.Descricao = descricao;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setListTarefas(ArrayList<Tarefa> listTarefas) {
        this.listTarefas = listTarefas;
    }

    public void setIdUsuarioCriador(int idUsuarioCriador) {
        this.IdUsuarioCriador = idUsuarioCriador;
    }

    public void setIdUsuarioExecutor(int idUsuarioExecutor) {
        this.IdUsuarioExecutor = idUsuarioExecutor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Nome);
        parcel.writeString(Descricao);
        parcel.writeInt(Id);
        parcel.writeInt(IdUsuarioCriador);
        parcel.writeInt(IdUsuarioExecutor);
        parcel.writeTypedList(listTarefas);
    }
}
