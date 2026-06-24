package com.mycompany.undertale_3.dao;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {
    public static void criarTabelasSeNaoExistirem() {
        String jogadores = """
            CREATE TABLE IF NOT EXISTS jogadores (
                id SERIAL PRIMARY KEY,
                nome VARCHAR(100) UNIQUE NOT NULL,
                pontos_totais INT DEFAULT 0,
                moedas INT DEFAULT 0,
                runs INT DEFAULT 0
            )
        """;
        String runs = """
            CREATE TABLE IF NOT EXISTS runs (
                id SERIAL PRIMARY KEY,
                jogador_id INT REFERENCES jogadores(id),
                pontuacao INT NOT NULL,
                moedas_ganhas INT NOT NULL,
                venceu_boss BOOLEAN DEFAULT FALSE,
                data_run TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        String upgrades = """
            CREATE TABLE IF NOT EXISTS upgrades_jogador (
                id SERIAL PRIMARY KEY,
                jogador_id INT REFERENCES jogadores(id),
                tipo VARCHAR(50) NOT NULL,
                nivel INT DEFAULT 0,
                UNIQUE(jogador_id, tipo)
            )
        """;
        try (Connection conn = ConnectionFactory.getConnection(); Statement st = conn.createStatement()) {
            st.execute(jogadores); st.execute(runs); st.execute(upgrades);
        } catch (Exception e) {
            System.err.println("Banco não conectado. Confira URL, usuário e senha em ConnectionFactory.java");
            e.printStackTrace();
        }
    }
}
