package DAO;

import Factory.ConnectionFactory;
import Models.Convenio;
import Models.Paciente;

import java.sql.*;
import java.time.LocalDateTime;

public class PacienteDAO {
    private String tableName = "paciente";
    private Connection connection = new ConnectionFactory().getConnection();

    public void createTable () {
        String sql = "CREATE SEQUENCE IF NOT EXISTS paciente_seq; ";

        sql += "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "paciente_id BIGINT PRIMARY KEY DEFAULT nextval('paciente_seq'), " +
                "convenio_id BIGINT NOT NULL, " +
                "cadastrado TIMESTAMP, " +
                "atualizado TIMESTAMP, " +
                "excluido TIMESTAMP, " +
                "tipo_atendimento TEXT, " +
                "numero_cartao_convenio TEXT, " +
                "data_vencimento TIMESTAMP, " +
                "nome TEXT, " +
                "telefone VARCHAR(12), " +
                "celular VARCHAR(12), " +
                "nacionalidade TEXT, " +
                "cpf VARCHAR(14), " +
                "rg VARCHAR(12), " +
                "email TEXT, " +
                "login TEXT, " +
                "senha TEXT, " +
                "sexo TEXT," +
                " CONSTRAINT fk_paciente_convenio_id " +
                        "FOREIGN KEY (convenio_id) " +
                        "REFERENCES convenio(convenio_id) " +
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

    public Paciente createPaciente(Paciente paciente){
        if(paciente != null){
            String sql = "INSERT INTO " + tableName +
                    "(cadastro,nome,convenio_id,tipo_atendimento,numero_cartao_convenio,data_vencimento,telefone,celular,nacionalidade,cpf,rg,email,login,senha,sexo)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try{
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setString(2, paciente.getNome());
                statement.setLong(3, paciente.getConvenio().getId());
                statement.setString(4, paciente.getTipoAtendimento().name());
                statement.setString(5, paciente.getNumeroCartaoConvenio());
                statement.setTimestamp(6, Timestamp.valueOf(paciente.getDataVencimento()));
                statement.setString(7, paciente.getTelefone());
                statement.setString(8, paciente.getCeluar());
                statement.setString(9, paciente.getNacionalidade());
                statement.setString(10, paciente.getCpf());
                statement.setString(11, paciente.getRg());
                statement.setString(12, paciente.getEmail());
                statement.setString(13, paciente.getLogin());
                statement.setString(14, paciente.getSenha());
                statement.setString(15, paciente.getSexo().name());


                statement.execute();

                ResultSet resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    paciente.setId(resultSet.getLong("paciente_id"));
                }
                return paciente;
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void updatePaciente(Paciente paciente){
        String sql = "UPDATE " + tableName + " SET nome = ?, atualizado = ?, convenio_id = ?, tipo_atendimento = ?, numero_cartao_convenio = ?, data_vencimento = ?, telefone = ?, celular = ?, nacioanlidade = ?," +
                "cpf = ?, rg = ?, email = ?, login = ?, senha = ?, sexo = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(16, paciente.getId());
            statement.setString(1, paciente.getNome());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(3, paciente.getConvenio().getId());
            statement.setString(4,paciente.getTipoAtendimento().name());
            statement.setString(5,paciente.getNumeroCartaoConvenio());
            statement.setTimestamp(6,Timestamp.valueOf(paciente.getDataVencimento()));
            statement.setString(7, paciente.getTelefone());
            statement.setString(8, paciente.getCeluar());
            statement.setString(9, paciente.getNacionalidade());
            statement.setString(10, paciente.getCpf());
            statement.setString(11, paciente.getRg());
            statement.setString(12, paciente.getEmail());
            statement.setString(13, paciente.getLogin());
            statement.setString(14, paciente.getSenha());
            statement.setString(15, paciente.getSexo().name());


            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Paciente mostrarPaciente(){
        String sql = "SELECT * FROM " + tableName ;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            Paciente paciente = new Paciente();;

            while (resultSet.next()) {
                paciente.setId(resultSet.getLong("paciente_id"));
                paciente.setNome(resultSet.getString("nome"));
                paciente.setValor(resultSet.getBigDecimal("valor"));
            }

            return paciente;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePaciente(Paciente paciente) {
        String sql = "UPDATE " + tableName + " SET excluido = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(2, paciente.getId());
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
