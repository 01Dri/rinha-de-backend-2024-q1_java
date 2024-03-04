package me.dri.utils;

import me.dri.entities.dto.TransacaoDTO;

import java.util.ArrayList;
import java.util.List;

public class Strategy {

    private List<StrategyValidation> strategies = new ArrayList<>();


    public Strategy() {

    }
    public void addStrategy(StrategyValidation... strategyValidation) {
        this.strategies.addAll(List.of(strategyValidation));
    }

    public void executeStrategy(Integer id, TransacaoDTO transacaoDTO) {
        this.strategies.forEach(strategyValidation -> strategyValidation.execute(id, transacaoDTO));
    }
}
