package com.fiquedeolho.nfcatividadeapp.models;

import java.io.Serializable;
import java.util.Date;

public class NotificacaoUsuario implements Serializable {

    private int IdNotificacaoUsuario;
    private int IdUsuarioNotificado;
    private String DescricaoNotificacao;
    private Boolean Visualizada;
    private Date DataNotificacao;

    public int getIdNotificacaoUsuario() {
        return IdNotificacaoUsuario;
    }

    public void setIdNotificacaoUsuario(int idNotificacaoUsuario) {
        IdNotificacaoUsuario = idNotificacaoUsuario;
    }

    public int getIdUsuarioNotificado() {
        return IdUsuarioNotificado;
    }

    public void setIdUsuarioNotificado(int idUsuarioNotificado) {
        IdUsuarioNotificado = idUsuarioNotificado;
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

    public Date getDataNotificacao() {
        return DataNotificacao;
    }

    public void setDataNotificacao(Date dataNotificacao) {
        DataNotificacao = dataNotificacao;
    }
}
