package com.fiquedeolho.nfcatividadeapp.models;


import android.os.Parcel;
import android.os.Parcelable;

public class TAG implements Parcelable {

    private int Id;
    private int IdUsuario;
    private String Nome;
    private String PalavraChave;

    public TAG(Parcel in) {
        Id = in.readInt();
        IdUsuario = in.readInt();
        Nome = in.readString();
        PalavraChave = in.readString();
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

    public String getPalavraChave() {
        return PalavraChave;
    }

    public int getIdUsuario(){return IdUsuario;}

    public void setId(int id) {
        this.Id = id;
    }

    public void setIdUsuario(int idUsuario) {
        this.IdUsuario = idUsuario;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setPalavraChave(String palavraChave){this.PalavraChave = palavraChave;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Nome);
        parcel.writeInt(IdUsuario);
        parcel.writeString(PalavraChave);
    }
}
