package board_dio.board_dio.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import board_dio.board_dio.dao.CardDAO;

import board_dio.board_dio.dto.CardDetailsDTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardQueryService {

    private final Connection connection = null;

    public CardQueryService(Connection connection2) {
		// TODO Auto-generated constructor stub
	}

	public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var dao = new CardDAO(connection);
        return dao.findById(id);
    }

}