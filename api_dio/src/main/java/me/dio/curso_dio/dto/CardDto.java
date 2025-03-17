package me.dio.curso_dio.dto;

import java.math.BigDecimal;

import me.dio.curso_dio.domain.Card;

public record CardDto(Long id, String number, BigDecimal limit) {

    public CardDto(Card model) {
        this(model.getId(), model.getNumber(), model.getLimit());
    }

    public Card toModel() {
        Card model = new Card();
        model.setId(this.id);
        model.setNumber(this.number);
        model.setLimit(this.limit);
        return model;
    }
}