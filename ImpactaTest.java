package impacta;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImpactaTest {
    private static final String DATA = "2026-12-05T09:00";
    private Impacta sistema;

    @BeforeEach
    void setUp() {
        sistema = new Impacta();
    }



    @Test
    void pontuacaoPolimorficaDasTresAcoes() {
        LocalDateTime d = LocalDateTime.parse(DATA);
        List<Acao> acoes = List.of(
                new PlantioMudas(1, "P", "d", d, 10, 10),            // 5 + 2*10 = 25
                new MutiraoReciclagem(2, "M", "d", d, 10, 3),        // 4*3 = 12
                new OficinaEcologica(3, "O1", "d", d, 10, 2, true),  // 3*2 + 10 = 16
                new OficinaEcologica(4, "O2", "d", d, 10, 2, false)  // 3*2 = 6
        );
        int[] esperado = {25, 12, 16, 6};
        for (int i = 0; i < acoes.size(); i++) {
            assertEquals(esperado[i], acoes.get(i).calcularPontuacao());
        }
    }



    @Test
    void rankingPorPontuacaoDecrescenteComDesempatePorNome() {
        sistema.cadastrarVoluntario("Bruno", "bruno@x.com", "2");
        sistema.cadastrarVoluntario("Ana", "ana@x.com", "1");
        sistema.cadastrarVoluntario("Carla", "carla@x.com", "3");
        sistema.cadastrarVoluntario("Daniel", "daniel@x.com", "4");

        int plantio = sistema.cadastrarPlantio("Plantio", "d", DATA, 5, 10); // 25
        int mutirao = sistema.cadastrarMutirao("Mutirão", "d", DATA, 5, 3);  // 12

        sistema.inscreverVoluntario("carla@x.com", plantio);
        sistema.inscreverVoluntario("bruno@x.com", mutirao);
        sistema.inscreverVoluntario("ana@x.com", mutirao);

        String[] ranking = sistema.listarVoluntarios();
        assertEquals(4, ranking.length);
        assertTrue(ranking[0].contains("Carla"));
        assertTrue(ranking[1].contains("Ana"));
        assertTrue(ranking[2].contains("Bruno"));
        assertTrue(ranking[3].contains("Daniel"));
    }



    @Test
    void emailDuplicadoLancaExcecao() {
        assertTrue(sistema.cadastrarVoluntario("Ana", "ana@x.com", "1"));
        assertThrows(EmailDuplicadoException.class,
                () -> sistema.cadastrarVoluntario("Outra Ana", "ana@x.com", "9"));
    }

    @Test
    void acaoLotadaLancaExcecao() {
        sistema.cadastrarVoluntario("Ana", "ana@x.com", "1");
        sistema.cadastrarVoluntario("Bruno", "bruno@x.com", "2");
        int id = sistema.cadastrarMutirao("Mutirão", "d", DATA, 1, 2);
        sistema.inscreverVoluntario("ana@x.com", id);
        assertThrows(AcaoLotadaException.class, () -> sistema.inscreverVoluntario("bruno@x.com", id));
    }

    @Test
    void duplaInscricaoLancaExcecao() {
        sistema.cadastrarVoluntario("Ana", "ana@x.com", "1");
        int id = sistema.cadastrarOficina("Oficina", "d", DATA, 5, 2, true);
        assertTrue(sistema.inscreverVoluntario("ana@x.com", id));
        assertThrows(InscricaoDuplicadaException.class, () -> sistema.inscreverVoluntario("ana@x.com", id));
    }



    @Test
    void exibirVoluntarioMostraAcoesEPontuacao() {
        sistema.cadastrarVoluntario("Ana", "ana@x.com", "1");
        int id = sistema.cadastrarPlantio("Plantio", "d", DATA, 5, 10);
        sistema.inscreverVoluntario("ana@x.com", id);
        String texto = sistema.exibirVoluntario("ana@x.com");
        assertTrue(texto.contains("Ações: 1"));
        assertTrue(texto.contains("Pontuação: 25"));
    }

    @Test
    void exibirDetalhesAcaoMostraInscritosEPontuacao() {
        sistema.cadastrarVoluntario("Ana", "ana@x.com", "1");
        int id = sistema.cadastrarOficina("Oficina", "Reuso", DATA, 5, 2, true);
        sistema.inscreverVoluntario("ana@x.com", id);
        String texto = sistema.exibirDetalhesAcao(id);
        assertTrue(texto.contains("Pontuação: 16"));
        assertTrue(texto.contains("Ana"));
        assertTrue(texto.contains("Kit de material: sim"));
    }
}
