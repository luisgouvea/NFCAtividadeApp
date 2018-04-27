package com.fiquedeolho.nfcatividadeapp.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Tarefa  implements Parcelable{

    private int Id;
    private int IdAtividade;
    private int IdTag;
    private int IdStatusExecucao;
    private String Nome;
    private String Comentario;
    private Boolean IniciaFluxo;
    private Boolean FinalizaFluxo;
    private ArrayList<TarefaPrecedente> listaAntecessoras;
    private ArrayList<TarefaSucessora> listaSucessoras;

    public Tarefa(Parcel in) {
        Id = in.readInt();
        IdAtividade = in.readInt();
        IdTag = in.readInt();
        IdStatusExecucao = in.readInt();
        Nome = in.readString();
        Comentario = in.readString();
        IniciaFluxo = in.readByte() != 0;
        FinalizaFluxo = in.readByte() != 0;
        listaAntecessoras = in.createTypedArrayList(CREATORLISTANTECESSORAS);
        listaSucessoras = in.createTypedArrayList(CREATORLISTSUCESSORAS);
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

    public static final Creator<TarefaPrecedente> CREATORLISTANTECESSORAS = new Creator<TarefaPrecedente>() {
        @Override
        public TarefaPrecedente createFromParcel(Parcel in) {
            return new TarefaPrecedente(in);
        }

        @Override
        public TarefaPrecedente[] newArray(int size) {
            return new TarefaPrecedente[size];
        }
    };

    public static final Creator<TarefaSucessora> CREATORLISTSUCESSORAS = new Creator<TarefaSucessora>() {
        @Override
        public TarefaSucessora createFromParcel(Parcel in) {
            return new TarefaSucessora(in);
        }

        @Override
        public TarefaSucessora[] newArray(int size) {
            return new TarefaSucessora[size];
        }
    };

    public Tarefa() {
        listaAntecessoras = new ArrayList<>(); listaSucessoras = new ArrayList<>();
    }

    public String getNome() {
        return Nome;
    }

    public String getComentario() {
        return Comentario;
    }

    public int getId() {
        return Id;
    }

    public int getIdTag() {
        return IdTag;
    }

    public int getIdAtividade() {
        return IdAtividade;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setIdTag(int idTag) {
        this.IdTag = idTag;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setComentario(String comentario) {
        this.Comentario = comentario;
    }

    public void setIdAtividade(int idAtividade) {
        this.IdAtividade = idAtividade;
    }

    public Boolean getIniciaFluxo() {
        return IniciaFluxo;
    }

    public void setIniciaFluxo(Boolean iniciaFluxo) {
        IniciaFluxo = iniciaFluxo;
    }

    public Boolean getFinalizaFluxo() {
        return FinalizaFluxo;
    }

    public void setFinalizaFluxo(Boolean finalizaFluxo) {
        FinalizaFluxo = finalizaFluxo;
    }

    public int getIdStatusExecucao() {
        return IdStatusExecucao;
    }

    public void setIdStatusExecucao(int idStatusExecucao) {
        IdStatusExecucao = idStatusExecucao;
    }

    public ArrayList<TarefaPrecedente> getListAntecessoras() {
        return this.listaAntecessoras;
    }

    public void setListAntecessoras(ArrayList<TarefaPrecedente> listaAntecessoras) {
        this.listaAntecessoras = listaAntecessoras;
    }

    public ArrayList<TarefaSucessora> getListSucessoras() {
        return this.listaSucessoras;
    }

    public void setListSucessoras(ArrayList<TarefaSucessora> listaSucessoras) {
        this.listaSucessoras = listaSucessoras;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeInt(IdTag);
        parcel.writeInt(IdAtividade);
        parcel.writeInt(IdStatusExecucao);
        parcel.writeString(Nome);
        parcel.writeString(Comentario);
        parcel.writeByte((byte) (IniciaFluxo ? 1 : 0));
        parcel.writeByte((byte) (FinalizaFluxo ? 1 : 0));
        parcel.writeTypedList(listaAntecessoras);
        parcel.writeTypedList(listaSucessoras);
    }
}
