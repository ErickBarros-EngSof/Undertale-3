package com.mycompany.undertale_3.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UpgradeDAO {
    public Map<String, Integer> listar(int jogadorId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("hp", 0); map.put("atk", 0); map.put("def", 0);
        String sql = "SELECT tipo, nivel FROM upgrades_jogador WHERE jogador_id = ?";
        try (Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, jogadorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) map.put(rs.getString("tipo"), rs.getInt("nivel"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public void comprar(int jogadorId, String tipo) {
        String sql = """
            INSERT INTO upgrades_jogador(jogador_id, tipo, nivel) VALUES (?, ?, 1)
            ON CONFLICT (jogador_id, tipo) DO UPDATE SET nivel = upgrades_jogador.nivel + 1
        """;
        try (Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, jogadorId); ps.setString(2, tipo); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
