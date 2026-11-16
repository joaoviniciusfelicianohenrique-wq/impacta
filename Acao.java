package impacta;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public abstract class Acao {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDateTime data;
    private int capacidade;
    private List<Voluntario> inscritos = new ArrayList<>();

    public Acao(int id, String titulo,String descricao,LocalDateTime data,int capacidade){
        this.capacidade = capacidade;
        this.data = data;
        this.descricao = descricao;
        this.id = id;
        this.titulo = titulo;
    }
    public abstract int calcularPontuacao();
    protected abstract String detalhesEspecificos();

    public void inscrever(Voluntario v){
        if(inscritos.contains(v)){
            throw new InscricaoDuplicadaException("Voluntário já inscrito nesta ação!");
        }
        if(inscritos.size()>= capacidade){
            throw new AcaoLotadaException("Ação lotada!");
        }
        inscritos.add(v);
        v.registrarParticipacao(calcularPontuacao());
    }
    public String exibirDetalhes(){
        String lista = "";
        for(int i = 0; i < inscritos.size(); i++){
            if(i > 0){
                lista += ", ";
            }
            lista += inscritos.get(i).getNome();
        }
        if(inscritos.isEmpty()){
            lista = "nenhum";
        }
        return "Ação #"+id+":"+titulo+
                "\nDescrição: "+ descricao+
                "\nData: "+data+
                "\nInscritos: "+ inscritos.size()+"/"+capacidade+
                "\nVoluntários inscritos: "+ lista+
                "\n"+detalhesEspecificos()+
                "\nPontuação: "+calcularPontuacao();
    }

    public int getId() {return id;}
    public String getTitulo() {return titulo;}
    public String getDescricao() {return descricao;}
    public LocalDateTime getData() {return data;}
    public int getCapacidade() {return capacidade;}
    public List<Voluntario> getInscritos() {return inscritos;}
}
