package impacta;
import java.time.LocalDateTime;

public class PlantioMudas extends Acao{
    private int qtdMudas;

    public PlantioMudas(int id,String titulo,String descricao, LocalDateTime data,int capacidade,int qtdMudas){
        super(id, titulo, descricao, data, capacidade);
        this.qtdMudas = qtdMudas;
    }
    @Override
    public int calcularPontuacao(){
        return 5+2 * qtdMudas;
    }
    @Override
    protected String detalhesEspecificos(){
        return "Tipo: Plantio de mudas. \nQuantidade de mudas: "+qtdMudas;
    }
    public int getQtdMudas(){return qtdMudas;}
}