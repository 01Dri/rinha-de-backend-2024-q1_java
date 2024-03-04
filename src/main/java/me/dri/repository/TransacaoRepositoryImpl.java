package me.dri.repository;

import me.dri.config.DatabaseConfig;
import me.dri.entities.Transacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacaoRepositoryImpl implements TransacaoRepository {

    @Override
    public void saveTransacao(Integer clientId,  Transacao transacao) throws SQLException {
        try (Connection conn = DatabaseConfig.getDataSource().getConnection()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO transacoes (cliente_id, valor, tipo, descricao, realizada_em) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, clientId);
                statement.setInt(2, transacao.getValor());
                statement.setString(3, transacao.getTipo());
                statement.setString(4, transacao.getDescricao());
                statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated != 1) {
                    throw new SQLException("Falha ao atualizar o cliente. Nenhuma linha afetada.");
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }


    @Override
    public List<Transacao> getTransacoesByClienteId(Integer id) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getDataSource().getConnection()) {
            String sql = "SELECT limite, saldo_inicial, valor, descricao, tipo, realizada_em FROM clientes c LEFT JOIN transacoes t ON t.cliente_id = c.id WHERE c.id = ? ORDER BY t.id DESC LIMIT 10";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        transacoes.add(new Transacao(resultSet.getInt("valor"),
                                resultSet.getString("tipo"), resultSet.getString("descricao"), resultSet.getDate("realizada_em")));
                    }
                }
            }
        }
        return transacoes;
    }
}
