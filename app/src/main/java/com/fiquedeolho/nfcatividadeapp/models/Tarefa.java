package com.fiquedeolho.nfcatividadeapp.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Tarefa  implements Parcelable{

    private int Id;
    private int IdAtividade;
    private String Nome;
    private String PalavraChave;
    private ArrayList<Tarefa> listaEncadeamento;

    public Tarefa(Parcel in) {
        Id = in.readInt();
        IdAtividade = in.readInt();
        Nome = in.readString();
        PalavraChave = in.readString();
        listaEncadeamento = in.createTypedArrayList(CREATOR);
    }

    public static final Creator<Tarefa> CREATOR = new Creator<Tarefa>() {
        @Override
        public Tarefa createFromParcel(Parcel in) {
            return new Tarefa(in);
        }

        @Override
        public Tarefa[] newArray(int size) {
            return new Tarefa[size];
        }
    };

    public Tarefa() {
        listaEncadeamento = new ArrayList<>();
    }

    public String getNome() {
        return Nome;
    }

    public String getPalavraChave() {
        return PalavraChave;
    }

    public int getId() {
        return Id;
    }

    public int getIdAtividade() {
        return IdAtividade;
    }

    public ArrayList<Tarefa> getListEncadeamento() {
        return this.listaEncadeamento;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setPalavraChave(String palavraChave) {
        this.PalavraChave = palavraChave;
    }

    public void setIdAtividade(int idAtividade) {
        this.IdAtividade = idAtividade;
    }

    public void setListEncadeamento(ArrayList<Tarefa> listaEncadeamento) {
        this.listaEncadeamento = listaEncadeamento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeInt(IdAtividade);
        parcel.writeString(Nome);
        parcel.writeString(PalavraChave);
        parcel.writeTypedList(listaEncadeamento);
    }
}
