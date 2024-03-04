package me.dri.services;

import me.dri.entities.dto.TransacaoDTO;
import me.dri.entities.dto.TransacaoResponseDTO;

import java.sql.SQLException;

public interface ClienteService {

    TransacaoResponseDTO transacao(Integer id, TransacaoDTO transacaoDTO) throws SQLException;

}
