package impacta;

import java.util.Objects;

public class Voluntario {
    private String email;
    private String nome;
    private String matricula;
    private int qtdAcoes;
    private int pontuacaoImpacto;

    public Voluntario(String nome, String email, String matricula) {
        Objects.requireNonNull(nome, "O nome não pode ser nulo.");
        Objects.requireNonNull(email, "O e-mail não pode ser nulo.");
        Objects.requireNonNull(matricula, "A matrícula não pode ser nula.");
        if (nome.isBlank() || email.isBlank() || matricula.isBlank()) {
            throw new IllegalArgumentException("Nenhum dos campos (nome, e-mail, matrícula) pode estar em branco.");
        }
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.qtdAcoes = 0;
        this.pontuacaoImpacto = 0;
    }

    public String getEmail() { return email; }
    public String getNome() { return nome; }
    public String getMatricula() { return matricula; }
    public int getQtdAcoes() { return qtdAcoes; }
    public int getPontuacaoImpacto() { return pontuacaoImpacto; }

    public void registrarParticipacao(int pontos) {
        this.qtdAcoes++;
        this.pontuacaoImpacto += pontos;
    }

    @Override
    public String toString() {
        return String.format("Nome: %s | Ações: %d | Pontuação: %d", nome, qtdAcoes, pontuacaoImpacto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voluntario that = (Voluntario) o;
        return Objects.equals(email.toLowerCase(), that.email.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email.toLowerCase());
    }
}
