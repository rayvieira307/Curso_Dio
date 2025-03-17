package board_dio.board_dio.ui;

import static board_dio.board_dio.config.ConnectionConfig.getConnection;

import java.sql.SQLException;

import board_dio.board_dio.migration.MigrationStrategy;


public class MainMenu {
	
	
	
	public static void main (String[] args) throws SQLException {
		
		try (var connection = getConnection()) {
			
			new MigrationStrategy().executeMigration();
			
		}
	}

   
        
    }

   

  