package DAO;

import Factory.ConnectionFactory;
import Models.Convenio;
import Models.Especialidade;

import java.sql.*;
import java.time.LocalDateTime;

public class EspecialidadeDAO {
    private String tableName = "especialidade";
    private Connection connection = new ConnectionFactory().getConnection();

    public void createTable(){
        String sql = "CREATE SEQUENCE IF NOT EXISTS especialidade_seq ; ";

        sql += "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "especialidade_id BIGINT PRIMARY KEY DEFAULT nextval('especialidade_seq'), " +
                "nome TEXT, " +
                "cadastrado TIMESTAMP, " +
                "excluido TIMESTAMP, " +
                "atualizado TIMESTAMP ); ";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
            statement.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Especialidade createEspecialidade(Especialidade especialidade){
        if(especialidade != null){
            String sql = "INSERT INTO " + tableName +
                    "(cadastro,nome)" +
                    "VALUES (?,?)";

            try{
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setString(2,especialidade.getNome());


                statement.execute();

                ResultSet resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    especialidade.setId(resultSet.getLong("especialidade_id"));
                }
                return especialidade;
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void updateEspecialidade(Especialidade especialidade){
        String sql = "UPDATE " + tableName + " SET nome = ?, atualizado = ? WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(3, especialidade.getId());
            statement.setString(1, especialidade.getNome());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteEspecialidade(Especialidade especialidade) {
        String sql = "UPDATE " + tableName + " SET excluido = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(2, especialidade.getId());
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
