package com.fiquedeolho.nfcatividadeapp.models;

public class NotificacaoUsuarioProblemaTarefa extends NotificacaoUsuario {

    public int IdNotificacaoUsuarioProblemaTarefa;
    public int IdTarefa;

    public int getIdNotificacaoUsuarioProblemaTarefa() {
        return IdNotificacaoUsuarioProblemaTarefa;
    }

    public void setIdNotificacaoUsuarioProblemaTarefa(int idNotificacaoUsuarioProblemaTarefa) {
        IdNotificacaoUsuarioProblemaTarefa = idNotificacaoUsuarioProblemaTarefa;
    }

    public int getIdTarefa() {
        return IdTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        IdTarefa = idTarefa;
    }
}
