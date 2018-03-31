package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Atividade implements Parcelable {

    private String Nome;
    private int Id;
    private ArrayList<TAG> listTags;

    public Atividade() {
        listTags = new ArrayList<TAG>();
    }


    protected Atividade(Parcel in) {
        Nome = in.readString();
        Id = in.readInt();
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
        return Nome;
    }

    public int getId() {
        return Id;
    }

    public ArrayList<TAG> getListTags() {
        return listTags;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setId(int id) {
        this.Id = id;
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
        parcel.writeString(Nome);
        parcel.writeInt(Id);
        parcel.writeTypedList(listTags);
    }
}
