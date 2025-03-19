package board_dio.board_dio.dto;

import board_dio.board_dio.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
	
}