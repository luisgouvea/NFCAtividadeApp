package com.fiquedeolho.nfcatividadeapp.models;


public class NotificacaoUsuarioAddAtividade extends NotificacaoUsuario {

    private int IdNotificacaoUsuarioAddAtividade;  // PK dessa tabela
    private int IdAtividade;

    public int getIdNotificacaoUsuarioAddAtividade() {
        return IdNotificacaoUsuarioAddAtividade;
    }

    public void setIdNotificacaoUsuarioAddAtividade(int idNotificacaoUsuarioAddAtividade) {
        IdNotificacaoUsuarioAddAtividade = idNotificacaoUsuarioAddAtividade;
    }

    public int getIdAtividade() {
        return IdAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        IdAtividade = idAtividade;
    }
}
