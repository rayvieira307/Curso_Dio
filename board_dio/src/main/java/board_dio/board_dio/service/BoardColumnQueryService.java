package board_dio.board_dio.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import board_dio.board_dio.dao.BoardColumnDAO;
import board_dio.board_dio.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection = null;

    public BoardColumnQueryService(Connection connection2) {
		// TODO Auto-generated constructor stub
	}

	public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
        var dao = new BoardColumnDAO(connection);
        return dao.findById(id);
    }

}