package me.dri.utils;

import me.dri.entities.dto.TransacaoDTO;
import me.dri.exceptions.DescricaoInvalidaTransacaoException;
import me.dri.exceptions.TipoInvalidoTransacaoException;
import me.dri.exceptions.ValorInvalidoTransacaoException;

public class ValidationInputsStrategyImpl  implements  StrategyValidation{
    @Override
    public void execute(Integer id, TransacaoDTO obj) {
        if (obj.tipo().isEmpty() || obj.tipo().isBlank()) {
            throw new TipoInvalidoTransacaoException("O tipo da transação é inválido");
        }
        if (obj.descricao().isBlank() || obj.descricao().isEmpty()) {
            throw new DescricaoInvalidaTransacaoException("Descrição está invalida");
        }
        if (obj.valor().toString().isEmpty() || obj.valor().toString().isBlank()) {
            throw new ValorInvalidoTransacaoException("O valor da transação é invalido!");
        }
    }
}
