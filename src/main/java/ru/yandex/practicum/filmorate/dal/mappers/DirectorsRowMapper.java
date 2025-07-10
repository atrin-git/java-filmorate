package ru.yandex.practicum.filmorate.dal.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

@Component
public class DirectorsRowMapper implements RowMapper<Director> {
    public DirectorsRowMapper() {
    }

    public Director mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder().id(resultSet.getLong("id")).name(resultSet.getString("name")).build();
    }
}

