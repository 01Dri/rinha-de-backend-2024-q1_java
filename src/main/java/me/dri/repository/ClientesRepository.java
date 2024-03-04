package me.dri.repository;

import me.dri.entities.Cliente;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ClientesRepository {

    Cliente findClienteById(Integer id) throws SQLException;

    void saveCliente(Cliente cliente) throws SQLException;
}
