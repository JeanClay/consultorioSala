package DAO;

import Factory.ConnectionFactory;
import Models.Convenio;
import Models.Medico;
import Models.Paciente;
import Models.Secretaria;

import java.sql.*;
import java.time.LocalDateTime;

public class SecretariaDAO {
    private String tableName = "secretaria";
    private Connection connection = new ConnectionFactory().getConnection();

    public void createTable(){
        String sql = "CREATE SEQUENCE IF NOT EXISTS secretaria_seq;";

        sql += "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                "secretaria_id BIGINT PRIMARY KEY DEFAULT nextval('secretaria_seq'), " +
                "cadastrado TIMESTAMP, " +
                "atualizado TIMESTAMP, " +
                "excluido TIMESTAMP, " +
                "salario NUMERIC (10,2), " +
                "pis TEXT, " +
                "data_contratacao TIMESTAMP, " +
                "nome TEXT, " +
                "telefone VARCHAR(12), " +
                "celular VARCHAR (12), "+
                "nacionalidade TEXT, " +
                "cpf VARCHAR(15), " +
                "rg VARCHAR(13), " +
                "email TEXT, " +
                "login TEXT, " +
                "senha TEXT, " +
                "sexo TEXT );";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
            statement.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public Secretaria createSecretaria(Secretaria secretaria){
        if(secretaria != null){
            String sql = "INSERT INTO " + tableName +
                    "(cadastro,nome,,data_contratacao,pis,salario,telefone,celular,nacionalidade,cpf,rg,email,login,senha,sexo)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try{
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setString(2, secretaria.getNome());
                statement.setTimestamp(3, Timestamp.valueOf(secretaria.getDataContratacao()));
                statement.setString(4, secretaria.getPis());
                statement.setBigDecimal(5, secretaria.getSalario());
                statement.setString(6, secretaria.getTelefone());
                statement.setString(7, secretaria.getCeluar());
                statement.setString(8, secretaria.getNacionalidade());
                statement.setString(9, secretaria.getCpf());
                statement.setString(10, secretaria.getRg());
                statement.setString(11, secretaria.getEmail());
                statement.setString(12, secretaria.getLogin());
                statement.setString(13, secretaria.getSenha());
                statement.setString(14, secretaria.getSexo().name());


                statement.execute();

                ResultSet resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    secretaria.setId(resultSet.getLong("paciente_id"));
                }
                return secretaria;
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void updateSecretaria(Secretaria secretaria){
        String sql = "UPDATE " + tableName + " SET nome = ?, atualizado = ?, salario = ?, pis = ?, data_contratacao = ?, telefone = ?, celular = ?, nacioanlidade = ?," +
                "cpf = ?, rg = ?, email = ?, login = ?, senha = ?, sexo = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(15, secretaria.getId());
            statement.setString(1, secretaria.getNome());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setBigDecimal(3, secretaria.getSalario());
            statement.setString(4,secretaria.getPis());
            statement.setTimestamp(5,Timestamp.valueOf(secretaria.getDataContratacao()));
            statement.setString(6, secretaria.getTelefone());
            statement.setString(7, secretaria.getCeluar());
            statement.setString(8, secretaria.getNacionalidade());
            statement.setString(9, secretaria.getCpf());
            statement.setString(10, secretaria.getRg());
            statement.setString(11, secretaria.getEmail());
            statement.setString(12, secretaria.getLogin());
            statement.setString(13, secretaria.getSenha());
            statement.setString(14, secretaria.getSexo().name());


            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteSecretaria(Secretaria secretaria) {
        String sql = "UPDATE " + tableName + " SET excluido = ?, WHERE convenio_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(2, secretaria.getId());
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
