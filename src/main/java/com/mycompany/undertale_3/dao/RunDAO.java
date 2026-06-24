package com.mycompany.undertale_3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RunDAO {
    public void salvarRun(int jogadorId, int pontuacao, int moedas, boolean venceuBoss) {
        String sql = "INSERT INTO runs(jogador_id, pontuacao, moedas_ganhas, venceu_boss) VALUES (?, ?, ?, ?)";
        try (Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, jogadorId); ps.setInt(2, pontuacao); ps.setInt(3, moedas); ps.setBoolean(4, venceuBoss); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
