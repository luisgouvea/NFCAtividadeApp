package com.fiquedeolho.nfcatividadeapp.models;

public class DetalhesAtividade {

    private Atividade atividade;
    private String nomeExecutor;

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String getNomeExecutor() {
        return nomeExecutor;
    }

    public void setNomeExecutor(String nomeExecutor) {
        this.nomeExecutor = nomeExecutor;
    }
}
