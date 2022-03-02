package DAO;

import Factory.ConnectionFactory;
import Models.Convenio;
import Models.Historico;

import java.sql.*;
import java.time.LocalDateTime;

public class HistoricoDAO {
    private String tableName = "historico";
    private Connection connection = new ConnectionFactory().getConnection();

    public void createTable(){
        String sql = "CREATE SEQUENCE IF NOT EXISTS historico_seq;";

        sql += "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "historico_id BIGINT PRIMARY KEY DEFAULT nextval('historico_seq'), " +
                "agenda_id BIGINT NOT NULL, " +
                "secretaria_id BIGINT NOT NULL, " +
                "paciente_id BIGINT NOT NULL, " +
                "observacao TEXT, " +
                "data TIMESTAMP, " +
                "status_agenda TEXT, " +
                "cadastrado TIMESTAMP, " +
                "atualizado TIMESTAMP, " +
                "excluido TIMESTAMP, " +
                "CONSTRAINT fk_historico_agenda_id " +
                    "FOREIGN KEY (agenda_id) " +
                    "REFERENCES agenda(agenda_id) " +
                    "ON DELETE CASCADE," +
                "CONSTRAINT fk_historico_secretaria_id " +
                    "FOREIGN KEY (secretaria_id)" +
                    "REFERENCES secretaria(secretaria_id) " +
                    "ON DELETE CASCADE," +
                "CONSTRAINT fk_historico_paciente_id " +
                    "FOREIGN KEY (paciente_id) " +
                    "REFERENCES paciente(paciente_id) " +
                    "ON DELETE CASCADE" +
                ");";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
            statement.close();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Historico createHistorico(Historico historico){
        if(historico != null){
            String sql = "INSERT INTO " + tableName +
                    "(cadastro,agenda_id,secretaria_id,paciente_id,observacao,data,status_agenda)" +
                    "VALUES (?,?,?,?,?,?,?)";

            try{
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setLong(2,historico.getAgenda().getId());
                statement.setLong(3, historico.getSecretaria().getId());
                statement.setLong(4, historico.getPaciente().getId());
                statement.setString(5, historico.getObservacao());
                statement.setTimestamp(6,Timestamp.valueOf(historico.getData()));
                statement.setString(7, historico.getStatusAgenda().name());

                statement.execute();

                ResultSet resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    historico.setId(resultSet.getLong("historico_id"));
                }
                return historico;
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void updateHistorico(Historico historico){
        String sql = "UPDATE " + tableName + " SET agenda_id = ?, atualizado = ?, secretaria_id = ?, agenda_id = ?,  observacao = ?, data = ?, status_agenda = ? WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(8, historico.getId());
            statement.setLong(1, historico.getAgenda().getId());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(3, historico.getSecretaria().getId());
            statement.setLong(4, historico.getAgenda().getId());
            statement.setString(5, historico.getObservacao());
            statement.setTimestamp(6, Timestamp.valueOf(historico.getData()));
            statement.setString(7,historico.getStatusAgenda().name());


            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteHistorico(Historico historico) {
        String sql = "UPDATE " + tableName + " SET excluido = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(2, historico.getId());
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
