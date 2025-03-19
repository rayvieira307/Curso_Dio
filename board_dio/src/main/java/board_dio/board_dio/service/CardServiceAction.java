package board_dio.board_dio.service;

import java.sql.SQLException;

@FunctionalInterface interface CardServiceAction {
	
    void execute(CardService service) throws SQLException;
    
    
}

