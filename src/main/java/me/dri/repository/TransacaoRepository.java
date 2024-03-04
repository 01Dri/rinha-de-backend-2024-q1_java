package me.dri.repository;

import me.dri.entities.Transacao;

import java.sql.SQLException;
import java.util.List;

public interface TransacaoRepository {


    void saveTransacao(Integer clientId, Transacao transacao) throws SQLException;
    List<Transacao> getTransacoesByClienteId(Integer id) throws SQLException;
}
