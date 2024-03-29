package com.example.reservationsystem.database.conditions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleQueryCondition<T> extends AbstractQueryCondition {
    private T value;

    public SimpleQueryCondition(String key, String symbol, T value) {
        super();
        this.key = key;
        this.symbol = symbol;
        this.value = value;
    }

    @Override
    public String mapToSql() {
        if (isEmpty()) {
            return "";
        }
        return key + " " + symbol + " ?";
    }

    @Override
    public boolean isEmpty() {
        return value == null || String.valueOf(value).isBlank();
    }
}
