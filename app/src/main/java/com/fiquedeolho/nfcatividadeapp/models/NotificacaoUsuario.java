package com.fiquedeolho.nfcatividadeapp.models;

import java.io.Serializable;

public class NotificacaoUsuario implements Serializable {

    private int IdNotificacaoUsuario;
    private int IdUsuario;
    private String DescricaoNotificacao;
    private Boolean Visualizada;

    public int getIdNotificacaoUsuario() {
        return IdNotificacaoUsuario;
    }

    public void setIdNotificacaoUsuario(int idNotificacaoUsuario) {
        IdNotificacaoUsuario = idNotificacaoUsuario;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getDescricaoNotificacao() {
        return DescricaoNotificacao;
    }

    public void setDescricaoNotificacao(String descricaoNotificacao) {
        DescricaoNotificacao = descricaoNotificacao;
    }

    public Boolean getVisualizada() {
        return Visualizada;
    }

    public void setVisualizada(Boolean visualizada) {
        Visualizada = visualizada;
    }

    /*@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdNotificacaoUsuario);
        dest.writeInt(IdUsuario);
    }*/
}
