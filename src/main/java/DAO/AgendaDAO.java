package DAO;

import Factory.ConnectionFactory;
import Models.Agenda;
import Models.Convenio;

import java.sql.*;
import java.time.LocalDateTime;

public class AgendaDAO {
    private String tableName = "agenda";
    private Connection connection = new ConnectionFactory().getConnection();

    public void createTable(){
        String sql = "CREATE SEQUENCE IF NOT EXISTS agenda_seq;";

        sql += "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "agenda_id BIGINT PRIMARY KEY DEFAULT nextval('agenda_seq'), " +
                "paciente_id BIGINT NOT NULL, " +
                "medico_id BIGINT NOT NULL, " +
                "status_agenda TEXT, " +
                "data_agendamento TIMESTAMP, " +
                "encaixe BOOLEAN DEFAULT false, " +
                "cadastrado TIMESTAMP, " +
                "atualizado TIMESTAMP, " +
                "excluido TIMESTAMP, " +
                "CONSTRAINT fk_agenda_paciente_id " +
                    "FOREIGN KEY (paciente_id)" +
                    "REFERENCES paciente(paciente_id) " +
                    "ON DELETE CASCADE, " +
                "CONSTRAINT fk_agenda_medico_id " +
                    "FOREIGN KEY (medico_id) " +
                    "REFERENCES medico(medico_id)" +
                    "ON DELETE CASCADE " +
                ");";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
            statement.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Agenda createAgenda(Agenda agenda){
        if(agenda != null){
            String sql = "INSERT INTO " + tableName +

                    "(cadastro,paciente_id,medico_id,status_agenda,data_agendamento,encaixe)" +
                    "VALUES (?,?,?,?,?)";

            try{
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setLong(2,agenda.getPaciente().getId());
                statement.setLong(3,agenda.getMedico().getId());
                statement.setString(4,agenda.getStatusAgenda().name());
                statement.setTimestamp(5,Timestamp.valueOf(agenda.getDataAgendamento()));
                statement.setBoolean(6,agenda.getEncaixe());

                statement.execute();

                ResultSet resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    agenda.setId(resultSet.getLong("agenda_id"));
                }
                return agenda;
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void updateAgenda(Agenda agenda){
        String sql = "UPDATE " + tableName + " SET paciente_id = ?, atualizado = ?, medico_id = ?, status_agenda = ?, data_agendamento = ?, encaixe = ? WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(7, agenda.getId());
            statement.setLong(1, agenda.getPaciente().getId());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(3, agenda.getMedico().getId());
            statement.setString(4,agenda.getStatusAgenda().name());
            statement.setTimestamp(5,Timestamp.valueOf(agenda.getDataAgendamento()));
            statement.setBoolean(6, agenda.getEncaixe());


            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteAgenda(Agenda agenda) {
        String sql = "UPDATE " + tableName + " SET excluido = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(2, agenda.getId());
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
