package board_dio.board_dio.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import board_dio.board_dio.dao.BoardColumnDAO;
import board_dio.board_dio.dao.BoardDAO;
import board_dio.board_dio.entity.BoardColumnEntity;
import board_dio.board_dio.entity.BoardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;
    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO;

    // Construtor para usar injeção de dependência
    public BoardService(Connection connection) {
        this.connection = connection;
        this.boardDAO = new BoardDAO(connection);  // Criação do DAO com a conexão
        this.boardColumnDAO = new BoardColumnDAO(connection);  // Criação do BoardColumnDAO com a conexão
    }

    // Inserir Board e suas colunas
    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        try {
            connection.setAutoCommit(false);  // Inicia transação

            // Inserir Board
            boardDAO.insert(entity);

            // Inserir as colunas do Board
            List<BoardColumnEntity> columns = entity.getBoardColumns();
            for (var column : columns) {
                column.setBoard(entity);
                boardColumnDAO.insert(column);
            }

            connection.commit();  // Commit da transação
        } catch (SQLException e) {
            connection.rollback();  // Rollback em caso de erro
            throw e;  // Propaga o erro
        }
        return entity;
    }

    // Deletar Board por ID
    public boolean delete(final Long id) throws SQLException {
        try {
            if (!boardDAO.exists(id)) {
                return false;  // Retorna falso se não encontrar o board
            }

            boardDAO.delete(id);  // Deleta o board
            connection.commit();  // Commit da transação
            return true;  // Retorna verdadeiro
        } catch (SQLException e) {
            connection.rollback();  // Rollback em caso de erro
            throw e;  // Propaga o erro
        }
    }
}
