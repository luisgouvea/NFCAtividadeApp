package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Atividade implements Parcelable {

    private String nome;
    private int id;
    private ArrayList<TAG> listTags;

    public Atividade() {
        listTags = new ArrayList<TAG>();
    }


    protected Atividade(Parcel in) {
        nome = in.readString();
        id = in.readInt();
        listTags = in.createTypedArrayList(TAG.CREATOR);
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

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public ArrayList<TAG> getListTags() {
        return listTags;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListTags(ArrayList<TAG> listTags) {
        this.listTags = listTags;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeInt(id);
        parcel.writeTypedList(listTags);
    }
}
