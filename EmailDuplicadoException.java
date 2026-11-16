package impacta;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String email) {
        super("Já existe um voluntário cadastrado com esse email: "+email);
    }
}
