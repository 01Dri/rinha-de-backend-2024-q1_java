package me.dri.services;

import me.dri.entities.dto.ExtratoDTO;

import java.sql.SQLException;

public interface ExtratoService {
    ExtratoDTO getExtratoByClienteId(Integer idCliente) throws SQLException;
}
