package me.dri.repository;

import me.dri.config.DatabaseConfig;
import me.dri.entities.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientesRepositoryImpl implements  ClientesRepository{

    public ClientesRepositoryImpl() {
    }

    @Override
    public Cliente findClienteById(Integer id)  {
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT id, limite, saldo_inicial FROM clientes WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(resultSet.getInt("id"));
                    cliente.setLimite(resultSet.getInt("limite"));
                    cliente.setSaldoInicial(resultSet.getInt("saldo_inicial"));
                    return cliente;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveCliente(Cliente cliente) throws SQLException {
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement("UPDATE clientes SET saldo_inicial = ? WHERE id = ?")) {
            statement.setInt(1, cliente.getSaldoInicial());
            statement.setInt(2, cliente.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
