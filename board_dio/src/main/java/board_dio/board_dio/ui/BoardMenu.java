package board_dio.board_dio.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import board_dio.board_dio.dto.BoardColumnInfoDTO;
import board_dio.board_dio.entity.BoardColumnEntity;
import board_dio.board_dio.entity.BoardEntity;
import board_dio.board_dio.entity.CardEntity;
import board_dio.board_dio.service.BoardColumnQueryService;
import board_dio.board_dio.service.BoardQueryService;
import board_dio.board_dio.service.CardQueryService;
import board_dio.board_dio.service.CardService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardEntity entity = new BoardEntity();

    public BoardMenu(BoardEntity b) {
		// TODO Auto-generated constructor stub
	}
    
    private Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/board";  
        String username = "postgres";  
        String password = "123456";  
        
        Connection connection = DriverManager.getConnection(url, username, password);
        
        if (connection != null) {
            System.out.println("Conexão estabelecida com sucesso!");
        } else {
            System.out.println("Falha ao estabelecer conexão.");
        }
        
        return connection;
    }


	public void execute() {
        try {
            displayBoardWelcomeMessage();
            int option = -1;
            while (option != 10) {
                showMenu();
                option = scanner.nextInt();
                processMenuOption(option);
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
        }
    }

    private void displayBoardWelcomeMessage() {
        System.out.printf("Bem-vindo ao board %s, selecione a operação desejada\n", entity.getId());
    }

    private void showMenu() {
        System.out.println("1 - Criar um card");
        System.out.println("2 - Mover um card");
        System.out.println("3 - Bloquear um card");
        System.out.println("4 - Desbloquear um card");
        System.out.println("5 - Cancelar um card");
        System.out.println("6 - Ver board");
        System.out.println("7 - Ver coluna com cards");
        System.out.println("8 - Ver card");
        System.out.println("9 - Voltar para o menu anterior");
        System.out.println("10 - Sair");
    }

    private void processMenuOption(int option) throws SQLException {
        switch (option) {
            case 1 -> createCard();
            case 2 -> moveCardToNextColumn();
            case 3 -> blockCard();
            case 4 -> unblockCard();
            case 5 -> cancelCard();
            case 6 -> showBoard();
            case 7 -> showColumn();
            case 8 -> showCard();
            case 9 -> System.out.println("Voltando para o menu anterior");
            case 10 -> System.exit(0);
            default -> System.out.println("Opção inválida, informe uma opção do menu");
        }
    }

    private void createCard() throws SQLException {
        var card = new CardEntity();
        System.out.println("Informe o título do card");
        card.setTitle(scanner.next());
        System.out.println("Informe a descrição do card");
        card.setDescription(scanner.next());
        card.setBoardColumn(entity.getInitialColumn());

        executeCardServiceAction(card, service -> service.create(card));  
    }




    private void moveCardToNextColumn() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a próxima coluna");
        long cardId = scanner.nextLong();
        var boardColumnsInfo = getBoardColumnInfo();

        executeCardServiceAction(cardId, cardId, boardColumnsInfo, service -> service.moveToNextColumn(cardId, boardColumnsInfo));  // Passando a ação de mover
    }
    

    private void blockCard() throws SQLException {
        System.out.println("Informe o id do card que será bloqueado");
        long cardId = scanner.nextLong();
        System.out.println("Informe o motivo do bloqueio do card");
        String reason = scanner.next();
        var boardColumnsInfo = getBoardColumnInfo();

        executeCardServiceAction(cardId, reason, boardColumnsInfo, service -> service.block(cardId, reason, boardColumnsInfo));  // Passando a ação de bloquear
    }
    
    

    private void unblockCard() throws SQLException {
        System.out.println("Informe o id do card que será desbloqueado");
        long cardId = scanner.nextLong();
        System.out.println("Informe o motivo do desbloqueio do card");
        String reason = scanner.next();

        executeCardServiceAction(cardId, reason, service -> service.unblock(cardId, reason));  // Passando a ação de desbloquear
    }
    
    private void cancelCard() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
        long cardId = scanner.nextLong();
        var cancelColumn = entity.getCancelColumn(); 
        var boardColumnsInfo = getBoardColumnInfo(); 

        executeCardServiceAction(cardId, cancelColumn.getId(), boardColumnsInfo, service -> service.cancel(cardId, cancelColumn.getId(), boardColumnsInfo));
    }




    private void showBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach(c -> 
                    System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    private void showColumn() throws SQLException {
        long selectedColumnId = selectColumn();
        try (var connection = getConnection()) {
            var column = new BoardColumnQueryService(connection).findById(selectedColumnId);
            column.ifPresent(co -> {
                System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
                co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s", 
                        ca.getId(), ca.getTitle(), ca.getDescription()));
            });
        }
    }

    private void showCard() throws SQLException {
        System.out.println("Informe o id do card que deseja visualizar");
        long selectedCardId = scanner.nextLong();
        try (var connection = getConnection()) {
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(
                            c -> {
                                System.out.printf("Card %s - %s.\n", c.id(), c.title());
                                System.out.printf("Descrição: %s\n", c.description());
                                System.out.println(c.blocked() ? 
                                        "Está bloqueado. Motivo: " + c.blockReason() : 
                                        "Não está bloqueado");
                                System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
                                System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
                            },
                            () -> System.out.printf("Não existe um card com o id %s\n", selectedCardId));
        }
    }

    private void handleSQLException(SQLException ex) {
        ex.printStackTrace();
        System.exit(0);
    }

    private List<BoardColumnInfoDTO> getBoardColumnInfo() {
        return entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
    }

    private long selectColumn() {
        var columnsIds = entity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        long selectedColumnId = -1L;
        while (!columnsIds.contains(selectedColumnId)) {
            System.out.printf("Escolha uma coluna do board %s pelo id\n", entity.getName());
            entity.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumnId = scanner.nextLong();
        }
        return selectedColumnId;
    }

    private void executeCardServiceAction(CardEntity card, CardServiceAction action) throws SQLException {
        try (var connection = getConnection()) {
            new CardService(connection).create(card);
        }
    }

    private void executeCardServiceAction(long cardId, Long long1, List<BoardColumnInfoDTO> boardColumnsInfo, CardServiceAction action) throws SQLException {
        try (var connection = getConnection()) {
            new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
        }
    }

  

	private void executeCardServiceAction(long cardId, String reason, List<BoardColumnInfoDTO> boardColumnsInfo, CardServiceAction action) throws SQLException {
        try (var connection = getConnection()) {
            new CardService(connection).block(cardId, reason, boardColumnsInfo);
        }
    }

    private void executeCardServiceAction(long cardId, String reason, CardServiceAction action) throws SQLException {
        try (var connection = getConnection()) {
            new CardService(connection).unblock(cardId, reason);
        }
    }

    @FunctionalInterface
    private interface CardServiceAction {
        void execute(CardService service) throws SQLException;
    }
}