package me.dri.services;

import me.dri.entities.Cliente;
import me.dri.entities.Transacao;
import me.dri.entities.dto.CarteiraDTO;
import me.dri.entities.dto.ExtratoDTO;
import me.dri.exceptions.ClienteNaoEncontradoException;
import me.dri.repository.ClientesRepository;
import me.dri.repository.TransacaoRepository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ExtratoServiceImpl implements  ExtratoService {

    private final ClientesRepository clientesRepository;
    private final TransacaoRepository transacaoRepository;

    public ExtratoServiceImpl(ClientesRepository clientesRepository, TransacaoRepository transacaoRepository) {
        this.clientesRepository = clientesRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Override
    public ExtratoDTO getExtratoByClienteId(Integer idCliente) throws SQLException {
        if (idCliente > 5) {
            throw new ClienteNaoEncontradoException("O cliente com o id: " + idCliente + "não foi encontrado!");
        }
        Cliente cliente = this.clientesRepository.findClienteById(idCliente);
        List<Transacao> transacaos = this.transacaoRepository.getTransacoesByClienteId(cliente.getId());
        return new ExtratoDTO(new CarteiraDTO(cliente.getSaldoInicial(), new Date(), cliente.getLimite()), transacaos);
    }
}
