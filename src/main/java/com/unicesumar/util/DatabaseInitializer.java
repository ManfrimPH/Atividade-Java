package com.unicesumar.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:database.sqlite";

    public static void initializeDatabase() {
        try {
            // Ler e executar o script SQL
            executeScript("scripts/recreate_database.sql");
            System.out.println("Banco de dados inicializado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeScript(String scriptPath) throws SQLException, IOException {
        String script = readFile(scriptPath);
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Dividir o script em comandos individuais
            for (String command : script.split(";")) {
                if (!command.trim().isEmpty()) {
                    stmt.executeUpdate(command);
                }
            }
        }
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignorar comentários SQL
                if (!line.trim().startsWith("--")) {
                    content.append(line).append("\n");
                }
            }
        }
        return content.toString();
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
} 