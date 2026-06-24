package com.mycompany.undertale_3.dao;

import com.mycompany.undertale_3.model.Jogador;
import com.mycompany.undertale_3.model.RankingLinha;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {
    public Jogador buscarOuCriar(String nome) {
        Jogador existente = buscarPorNome(nome);
        if (existente != null) return existente;
        String sql = "INSERT INTO jogadores(nome) VALUES (?) RETURNING id, nome, pontos_totais, moedas, runs";
        try (Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Jogador buscarPorNome(String nome) {
        String sql = "SELECT * FROM jogadores WHERE LOWER(nome) = LOWER(?)";
        try (Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void somarResultado(int jogadorId, int pontos, int moedas) {
        String sql = "UPDATE jogadores SET pontos_totais = pontos_totais + ?, moedas = moedas + ?, runs = runs + 1 WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, pontos); ps.setInt(2, moedas); ps.setInt(3, jogadorId); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<RankingLinha> rankingTop10() {
        List<RankingLinha> lista = new ArrayList<>();
        String sql = "SELECT nome, pontos_totais, runs FROM jogadores ORDER BY pontos_totais DESC LIMIT 10";
        try (Connection c = ConnectionFactory.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(new RankingLinha(rs.getString("nome"), rs.getInt("pontos_totais"), rs.getInt("runs")));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private Jogador map(ResultSet rs) throws SQLException {
        return new Jogador(rs.getInt("id"), rs.getString("nome"), rs.getInt("pontos_totais"), rs.getInt("moedas"), rs.getInt("runs"));
    }
}
