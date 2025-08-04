import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaFilmes extends JFrame {

    private JTextField txtTitulo, txtCategoria, txtAno, txtFiltro;
    private JButton btnCadastrar, btnBuscar, btnAtualizar, btnExcluir;
    private JTable tabelaFilmes;
    private DefaultTableModel modeloTabela;

    public TelaFilmes() {
        setTitle("Gerenciador de Filmes");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Título:");
        JLabel lblCategoria = new JLabel("Categoria:");
        JLabel lblAno = new JLabel("Ano:");
        JLabel lblFiltro = new JLabel("Filtrar por categoria:");

        txtTitulo = new JTextField();
        txtCategoria = new JTextField();
        txtAno = new JTextField();
        txtFiltro = new JTextField();

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");

        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Título");
        modeloTabela.addColumn("Categoria");
        modeloTabela.addColumn("Ano");

        tabelaFilmes = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaFilmes);

        lblTitulo.setBounds(20, 20, 100, 25);
        txtTitulo.setBounds(120, 20, 200, 25);

        lblCategoria.setBounds(20, 60, 100, 25);
        txtCategoria.setBounds(120, 60, 200, 25);

        lblAno.setBounds(20, 100, 100, 25);
        txtAno.setBounds(120, 100, 200, 25);

        btnCadastrar.setBounds(350, 20, 120, 25);
        btnAtualizar.setBounds(350, 60, 120, 25);
        btnExcluir.setBounds(350, 100, 120, 25);

        lblFiltro.setBounds(20, 150, 150, 25);
        txtFiltro.setBounds(170, 150, 150, 25);
        btnBuscar.setBounds(350, 150, 120, 25);

        scrollTabela.setBounds(20, 200, 650, 230);

        add(lblTitulo); add(txtTitulo);
        add(lblCategoria); add(txtCategoria);
        add(lblAno); add(txtAno);
        add(btnCadastrar); add(btnAtualizar); add(btnExcluir);
        add(lblFiltro); add(txtFiltro); add(btnBuscar);
        add(scrollTabela);

        btnCadastrar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                String categoria = txtCategoria.getText().trim();
                int ano = Integer.parseInt(txtAno.getText().trim());

                Filme f = new Filme();
                f.setTitulo(titulo);
                f.setCategoria(categoria);
                f.setAno(ano);

                FilmeDAO dao = new FilmeDAO();
                dao.inserir(f);

                JOptionPane.showMessageDialog(null, "Filme cadastrado com sucesso!");
                limparCampos();
                listarFilmes();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ano deve ser um número inteiro.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + ex.getMessage());
            }
        });

        btnBuscar.addActionListener(e -> {
            try {
                String filtro = txtFiltro.getText().trim();

                FilmeDAO dao = new FilmeDAO();
                modeloTabela.setRowCount(0);

                for (Filme f : dao.buscarPorCategoria(filtro)) {
                    modeloTabela.addRow(new Object[]{
                        f.getId(), f.getTitulo(), f.getCategoria(), f.getAno()
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao buscar filmes: " + ex.getMessage());
            }
        });

        btnAtualizar.addActionListener(e -> {
            int linha = tabelaFilmes.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(null, "Selecione um filme na tabela para atualizar.");
                return;
            }

            try {
                int id = (int) modeloTabela.getValueAt(linha, 0);
                String titulo = txtTitulo.getText().trim();
                String categoria = txtCategoria.getText().trim();
                int ano = Integer.parseInt(txtAno.getText().trim());

                Filme f = new Filme();
                f.setId(id);
                f.setTitulo(titulo);
                f.setCategoria(categoria);
                f.setAno(ano);

                FilmeDAO dao = new FilmeDAO();
                dao.atualizar(f);

                JOptionPane.showMessageDialog(null, "Filme atualizado com sucesso!");
                limparCampos();
                listarFilmes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabelaFilmes.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(null, "Selecione um filme na tabela para excluir.");
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    int id = (int) modeloTabela.getValueAt(linha, 0);
                    FilmeDAO dao = new FilmeDAO();
                    dao.excluir(id);

                    JOptionPane.showMessageDialog(null, "Filme excluído com sucesso!");
                    limparCampos();
                    listarFilmes();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir: " + ex.getMessage());
                }
            }
        });

        listarFilmes();
    }

    private void limparCampos() {
        txtTitulo.setText("");
        txtCategoria.setText("");
        txtAno.setText("");
    }

    private void listarFilmes() {
        try {
            FilmeDAO dao = new FilmeDAO();
            modeloTabela.setRowCount(0);

            for (Filme f : dao.listar()) {
                modeloTabela.addRow(new Object[]{
                    f.getId(), f.getTitulo(), f.getCategoria(), f.getAno()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao listar filmes: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaFilmes().setVisible(true);
        });
    }
}