package impacta;
import java.time.LocalDateTime;

public class OficinaEcologica extends Acao{
    private int duracaoHoras;
    private boolean kitMaterial;

    public OficinaEcologica(int id,String titulo,String descricao, LocalDateTime data,int capacidade,int duracaoHoras, boolean kitMaterial){
        super(id, titulo, descricao, data, capacidade);
        this.duracaoHoras=duracaoHoras;
        this.kitMaterial=kitMaterial;
    }
    @Override
    public int calcularPontuacao(){
        int pontos = 3 * duracaoHoras;
        if (kitMaterial){
            pontos+=10;
        }
        return pontos;
    }
    @Override
    protected String detalhesEspecificos(){
        return "Tipo: Oficina Ecológica. \nDuração: "+duracaoHoras+"h"
                +"\nKit de material: "+(kitMaterial ? "sim" : "não");
    }
    public int getDuracaoHoras() {return duracaoHoras;}
    public boolean isKitMaterial() {return kitMaterial;}
}
