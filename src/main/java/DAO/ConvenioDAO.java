package DAO;

import Factory.ConnectionFactory;
import Models.Convenio;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConvenioDAO {
    private String tableName = "convenio";
    private Connection connection = new ConnectionFactory().getConnection();

    public void createTable() {
        String sql = "CREATE SEQUENCE IF NOT EXISTS convenio_seq;";

        sql += "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "convenio_id BIGINT PRIMARY KEY DEFAULT nextval('convenio_seq')," +
                "cadastro TIMESTAMP, " +
                "atualizado TIMESTAMP, " +
                "excluido TIMESTAMP, " +
                "nome TEXT, " +
                "valor NUMERIC (10,2) );";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Convenio createConvenio(Convenio convenio) {
        if (convenio != null) {
            String sql = "INSERT INTO " + tableName +
                    "(cadastro,nome,valor)" +
                    "VALUES (?,?,?)";

            try {
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setString(2, convenio.getNome());
                statement.setBigDecimal(3, convenio.getValor());

                statement.execute();

                ResultSet resultSet = statement.getGeneratedKeys();
                while (resultSet.next()) {
                    convenio.setId(resultSet.getLong("convenio_id"));
                }
                return convenio;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void updateConvenio(Convenio convenio) {
        String sql = "UPDATE " + tableName + " SET nome = ?, atualizado = ?, valor = ? WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(4, convenio.getId());
            statement.setString(1, convenio.getNome());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setBigDecimal(3, convenio.getValor());


            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Convenio mostrarConvenio(){
        String sql = "SELECT * FROM " + tableName ;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            Convenio convenio = new Convenio();;

            while (resultSet.next()) {
                convenio.setId(resultSet.getLong("convenio_id"));
                convenio.setNome(resultSet.getString("nome"));
                convenio.setValor(resultSet.getBigDecimal("valor"));
            }

            return convenio;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteConvenio(Convenio convenio) {
        String sql = "UPDATE " + tableName + " SET excluido = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(2, convenio.getId());
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
