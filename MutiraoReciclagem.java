package impacta;
import java.time.LocalDateTime;

public class MutiraoReciclagem extends Acao{
    private int duracaoHoras;

    public MutiraoReciclagem(int id,String titulo,String descricao, LocalDateTime data,int capacidade,int duracaoHoras){
        super(id, titulo, descricao, data, capacidade);
        this.duracaoHoras = duracaoHoras;
    }
    @Override
    public int calcularPontuacao(){
        return 4 * duracaoHoras;
    }
    @Override
    protected String detalhesEspecificos(){
        return "Tipo: Mutirão de Reciclagem. \nDuração: "+duracaoHoras+"h";
    }

    public int getDuracaoHoras() {
        return duracaoHoras;
    }
}
