package com.fiquedeolho.nfcatividadeapp.models;

public class NotificacaoUsuarioProblemaTarefa extends NotificacaoUsuario {

    public int IdNotificacaoUsuarioProblemaTarefa;
    public int IdTarefa;
    public String DescricaoProblema;
    public Boolean CheckRealizado;

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

    public String getDescricaoProblema() {
        return DescricaoProblema;
    }

    public void setDescricaoProblema(String descricaoProblema) {
        DescricaoProblema = descricaoProblema;
    }

    public Boolean getCheckRealizado() {
        return CheckRealizado;
    }

    public void setCheckRealizado(Boolean checkRealizado) {
        CheckRealizado = checkRealizado;
    }
}
