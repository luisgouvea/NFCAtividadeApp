package com.fiquedeolho.nfcatividadeapp.models;


import android.os.Parcel;
import android.os.Parcelable;

public class TAG implements Parcelable {

    private int Id;
    private String Nome;

    public TAG(Parcel in) {
        Id = in.readInt();
        Nome = in.readString();
    }

    public TAG(){

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

    public String getNome() {
        return Nome;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Nome);
    }
}
