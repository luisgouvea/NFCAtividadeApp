package com.fiquedeolho.nfcatividadeapp.models;

import java.util.Date;

public class FiltroPesquisaHome {

    private int IdStatusAtividade;
    private Date DataCriacao;
    private String DescricaoAtividade;
    private int IdUsuario;

   public int getIdStatusAtividade() {
        return IdStatusAtividade;
    }

    public void setIdStatusAtividade(int idStatusAtividade) {
        IdStatusAtividade = idStatusAtividade;
    }

    public Date getDataCriacao() {
        return DataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        DataCriacao = dataCriacao;
    }

    public String getDescricaoAtividade() {
        return DescricaoAtividade;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        DescricaoAtividade = descricaoAtividade;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }
}
