package Models;

import java.math.BigDecimal;

public class Convenio extends AbstractEntity{
    private String nome;
    private BigDecimal valor;

    public Convenio() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Convenio{" +
                "id=" + this.getId() + '\'' +
                "nome='" + nome + '\'' +
                ", valor=" + valor +
                '}';
    }
}
