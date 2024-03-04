package me.dri.utils;

import me.dri.entities.dto.TransacaoDTO;

public interface StrategyValidation {

    void execute(Integer id, TransacaoDTO obj);
}
