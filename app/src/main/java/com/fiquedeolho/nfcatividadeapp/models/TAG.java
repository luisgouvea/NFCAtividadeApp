package com.fiquedeolho.nfcatividadeapp.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TAG  implements Parcelable{

    private int id;
    private String nome;
    private ArrayList<String> listAntecessores;

    public TAG(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        listAntecessores = in.createStringArrayList();
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
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getListAntecessores() {
        return this.listAntecessores;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setListAntecessores(ArrayList<String> listAntecessores) {
        this.listAntecessores = listAntecessores;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nome);
        parcel.writeStringList(listAntecessores);
    }
}
