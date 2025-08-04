import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO {

    public void inserir(Filme f) throws Exception {
        String sql = "INSERT INTO filmes (titulo, categoria, ano) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getTitulo());
            stmt.setString(2, f.getCategoria());
            stmt.setInt(3, f.getAno());
            stmt.executeUpdate();
        }
    }

    public List<Filme> listar() throws Exception {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT * FROM filmes";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Filme f = new Filme();
                f.setId(rs.getInt("id"));
                f.setTitulo(rs.getString("titulo"));
                f.setCategoria(rs.getString("categoria"));
                f.setAno(rs.getInt("ano"));
                lista.add(f);
            }
        }
        return lista;
    }

    public List<Filme> buscarPorCategoria(String categoria) throws Exception {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT * FROM filmes WHERE categoria LIKE ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + categoria + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Filme f = new Filme();
                f.setId(rs.getInt("id"));
                f.setTitulo(rs.getString("titulo"));
                f.setCategoria(rs.getString("categoria"));
                f.setAno(rs.getInt("ano"));
                lista.add(f);
            }
        }
        return lista;
    }

    public void atualizar(Filme f) throws Exception {
        String sql = "UPDATE filmes SET titulo = ?, categoria = ?, ano = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getTitulo());
            stmt.setString(2, f.getCategoria());
            stmt.setInt(3, f.getAno());
            stmt.setInt(4, f.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM filmes WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}