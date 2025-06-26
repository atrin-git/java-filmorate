package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rates;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatesRowMapper implements RowMapper<Rates> {

    @Override
    public Rates mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Rates.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
