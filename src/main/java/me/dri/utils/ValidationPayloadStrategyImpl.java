package me.dri.utils;

import me.dri.entities.dto.TransacaoDTO;
import me.dri.exceptions.ClienteNaoEncontradoException;
import me.dri.exceptions.LimiteTamanhoDescricaoExcedidoException;
import me.dri.exceptions.TipoDeOperacaoInvalidoTransacaoException;

public class ValidationPayloadStrategyImpl implements StrategyValidation {


    @Override
    public void execute(Integer id, TransacaoDTO obj) {
        if (id > 5 ) {
            throw new ClienteNaoEncontradoException("O cliente com o id: " + id + "não foi encontrado!");
        }
        if (obj.descricao().length() > 10) {
            throw new LimiteTamanhoDescricaoExcedidoException("O tamanho da descrição deve ser de apenas 10 caracteres");
        }
        if (!obj.tipo().equalsIgnoreCase("c") && (!obj.tipo().equalsIgnoreCase("d"))) {
            throw new TipoDeOperacaoInvalidoTransacaoException("O tipo de operação deve ser apenas 'C' ou 'D'");
        }
    }
}
