package Models;

public class Especialidade extends AbstractEntity{
    private String nome;

    public Especialidade() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
