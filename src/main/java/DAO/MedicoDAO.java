package DAO;

import Factory.ConnectionFactory;
import Models.Convenio;
import Models.Medico;
import Models.Paciente;

import java.sql.*;
import java.time.LocalDateTime;

public class MedicoDAO {
    private String tableName = "medico";
    private Connection connection = new ConnectionFactory().getConnection();

    public void createTable(){
        String sql = "CREATE SEQUENCE IF NOT EXISTS medico_seq;";

        sql += "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "medico_id BIGINT PRIMARY KEY DEFAULT nextval('medico_seq'), " +
                "especialidade_id BIGINT NOT NULL, " +
                "cadastrado TIMESTAMP, " +
                "atualizado TIMESTAMP, " +
                "excluido TIMESTAMP, " +
                "crm TEXT, " +
                "porcentagem_participacao NUMERIC (10,2), " +
                "consultorio TEXT, " +
                "nome TEXT, " +
                "telefone TEXT, " +
                "celular TEXT, " +
                "nacionalidade TEXT, " +
                "cpf VARCHAR(15), " +
                "rg VARCHAR(13), " +
                "email TEXT, " +
                "login TEXT, " +
                "senha TEXT, " +
                "sexo TEXT, " +
                "CONSTRAINT fk_medico_especialidade_id " +
                    "FOREIGN KEY (especialidade_id)" +
                    "REFERENCES especialidade(especialidade_id)"+
                    "ON DELETE CASCADE" +
                ");";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
            statement.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Medico createMedico(Medico medico){
        if(medico != null){
            String sql = "INSERT INTO " + tableName +
                    "(cadastro,nome,especialidade_id,crm,consultorio,porcentagem_participacao,telefone,celular,nacionalidade,cpf,rg,email,login,senha,sexo)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try{
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setString(2, medico.getNome());
                statement.setLong(3, medico.getEspecialidade().getId());
                statement.setString(4, medico.getCrm());
                statement.setString(5, medico.getConsultorio());
                statement.setBigDecimal(6, medico.getPorcenParticipacao());
                statement.setString(7, medico.getTelefone());
                statement.setString(8, medico.getCeluar());
                statement.setString(9, medico.getNacionalidade());
                statement.setString(10, medico.getCpf());
                statement.setString(11, medico.getRg());
                statement.setString(12, medico.getEmail());
                statement.setString(13, medico.getLogin());
                statement.setString(14, medico.getSenha());
                statement.setString(15, medico.getSexo().name());


                statement.execute();

                ResultSet resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    medico.setId(resultSet.getLong("paciente_id"));
                }
                return medico;
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void updateMedico(Medico medico){
        String sql = "UPDATE " + tableName + " SET nome = ?, atualizado = ?, especialidade_id = ?, porcentagem_participacao = ?, crm = ?, consultorio = ?, telefone = ?, celular = ?, nacioanlidade = ?," +
                "cpf = ?, rg = ?, email = ?, login = ?, senha = ?, sexo = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(16, medico.getId());
            statement.setString(1, medico.getNome());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(3, medico.getEspecialidade().getId());
            statement.setBigDecimal(4,medico.getPorcenParticipacao());
            statement.setString(5,medico.getCrm());
            statement.setString(6, medico.getConsultorio());
            statement.setString(7, medico.getTelefone());
            statement.setString(8, medico.getCeluar());
            statement.setString(9, medico.getNacionalidade());
            statement.setString(10, medico.getCpf());
            statement.setString(11, medico.getRg());
            statement.setString(12, medico.getEmail());
            statement.setString(13, medico.getLogin());
            statement.setString(14, medico.getSenha());
            statement.setString(15, medico.getSexo().name());


            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteMedico(Medico medico) {
        String sql = "UPDATE " + tableName + " SET excluido = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(2, medico.getId());
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
