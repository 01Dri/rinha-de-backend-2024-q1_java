package me.dri.entities.dto;

import me.dri.entities.Transacao;

import java.util.List;

public record ExtratoDTO(CarteiraDTO saldo, List<Transacao> ultimas_transacoes) {
}

