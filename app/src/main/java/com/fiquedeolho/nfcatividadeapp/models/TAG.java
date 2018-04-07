package com.fiquedeolho.nfcatividadeapp.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TAG  implements Parcelable{

    private int Id;
    private int IdAtividade;
    private String Nome;
    private String PalavraChave;
    private ArrayList<String> listAntecessores;
    private ArrayList<TAG> listaEncadeamento;

    public TAG(Parcel in) {
        Id = in.readInt();
        IdAtividade = in.readInt();
        Nome = in.readString();
        PalavraChave = in.readString();
        listAntecessores = in.createStringArrayList();
        listaEncadeamento = in.createTypedArrayList(CREATOR);
    }

    public static final Creator<TAG> CREATOR = new Creator<TAG>() {
        @Override
        public TAG createFromParcel(Parcel in) {
            return new TAG(in);
        }

        @Override
        public TAG[] newArray(int size) {
            return new TAG[size];
        }
    };

    public TAG() {
        listAntecessores = new ArrayList<>();
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

    public ArrayList<String> getListAntecessores() {
        return this.listAntecessores;
    }

    public ArrayList<TAG> getListEncadeamento() {
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

    public void setListAntecessores(ArrayList<String> listAntecessores) {
        this.listAntecessores = listAntecessores;
    }

    public void setListEncadeamento(ArrayList<TAG> listaEncadeamento) {
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
        parcel.writeStringList(listAntecessores);
        parcel.writeTypedList(listaEncadeamento);
        //parcel.parc(listaEncadeamento);
    }
}
