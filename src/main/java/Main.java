import DAO.*;
import Models.Convenio;

public class Main {
    public static void main(String[] args) {
        ConvenioDAO convenioDAO = new ConvenioDAO();
        EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
        PacienteDAO pacienteDAO = new PacienteDAO();
        SecretariaDAO secretariaDAO = new SecretariaDAO();
        MedicoDAO medicoDAO = new MedicoDAO();
        AgendaDAO agendaDAO = new AgendaDAO();
        HistoricoDAO historicoDAO = new HistoricoDAO();

        convenioDAO.createTable();
        especialidadeDAO.createTable();
        pacienteDAO.createTable();
        secretariaDAO.createTable();
        medicoDAO.createTable();
        agendaDAO.createTable();
        historicoDAO.createTable();

    }
}