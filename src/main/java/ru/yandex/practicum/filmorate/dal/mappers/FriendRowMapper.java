package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendRowMapper implements RowMapper<Friends> {

    @Override
    public Friends mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Friends.builder()
                .userId(rs.getLong("user_id"))
                .friendId(rs.getLong("friend_id"))
                .build();
    }
}
