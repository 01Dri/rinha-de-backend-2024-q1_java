package me.dri.services;

import me.dri.entities.Cliente;
import me.dri.entities.Transacao;
import me.dri.entities.dto.TransacaoDTO;
import me.dri.entities.dto.TransacaoResponseDTO;
import me.dri.exceptions.LimiteExcedidoTransacaoException;
import me.dri.repository.ClientesRepository;
import me.dri.repository.TransacaoRepository;
import me.dri.utils.Strategy;
import me.dri.utils.ValidationInputsStrategyImpl;
import me.dri.utils.ValidationPayloadStrategyImpl;

import java.sql.SQLException;
import java.util.Date;

public class ClienteServiceImpl implements  ClienteService {

    private final ClientesRepository clientesRepository;
    private final TransacaoRepository transacaoRepository;
    private final Strategy strategy = new Strategy();

    public ClienteServiceImpl(ClientesRepository clientesRepository, TransacaoRepository transacaoRepository) {
        this.clientesRepository = clientesRepository;
        this.transacaoRepository = transacaoRepository;
    }


    @Override
    public TransacaoResponseDTO transacao(Integer id, TransacaoDTO transacaoDTO) throws SQLException {
        this.strategy.addStrategy(new ValidationPayloadStrategyImpl(), new ValidationInputsStrategyImpl());
        this.strategy.executeStrategy(id, transacaoDTO);
        Cliente cliente = this.clientesRepository.findClienteById(id);
        if (transacaoDTO.tipo().equalsIgnoreCase("d")) {
            validarDebito(transacaoDTO, cliente);
        } else {
            validarCredito(transacaoDTO, cliente);
        }
        try {
                this.clientesRepository.saveCliente(cliente);
                this.transacaoRepository.saveTransacao(id, new Transacao(transacaoDTO.valor(), transacaoDTO.tipo(), transacaoDTO.descricao(), new Date()));
                return new TransacaoResponseDTO(cliente.getLimite(), cliente.getSaldoInicial());
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validarDebito(TransacaoDTO transacaoDTO, Cliente cliente) {
        if (transacaoDTO.tipo().equalsIgnoreCase("d")) {
            Integer novoLimite = cliente.getLimite() - -cliente.getSaldoInicial();
            if (transacaoDTO.valor() > novoLimite) {
                throw new LimiteExcedidoTransacaoException("O valor da transaçao é maior do que o limite do cliente");
            }
        }
        cliente.setSaldoInicial(cliente.getSaldoInicial() - transacaoDTO.valor());
    }

    private static void validarCredito(TransacaoDTO transacaoDTO, Cliente cliente) {
        Integer novoSaldo = transacaoDTO.valor() + cliente.getSaldoInicial();
        if (novoSaldo > cliente.getLimite()) {
            throw new LimiteExcedidoTransacaoException("O valor da transaçao é maior do que o limite do cliente");
        }
        cliente.setSaldoInicial(novoSaldo);
    }

}
