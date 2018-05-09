package com.fiquedeolho.nfcatividadeapp.models;


import android.os.Parcel;
import android.os.Parcelable;

public class TAG implements Parcelable {

    private int IdentificadorTag;
    private int IdUsuario;
    private String Nome;
    private String PalavraChave;

    public TAG(Parcel in) {
        IdentificadorTag = in.readInt();
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

    public int getIdentificadorTag() {
        return IdentificadorTag;
    }

    public String getPalavraChave() {
        return PalavraChave;
    }

    public int getIdUsuario(){return IdUsuario;}

    public void setIdentificadorTag(int identificadorTag) {
        this.IdentificadorTag = identificadorTag;
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
        parcel.writeInt(IdentificadorTag);
        parcel.writeString(Nome);
        parcel.writeInt(IdUsuario);
        parcel.writeString(PalavraChave);
    }
}
