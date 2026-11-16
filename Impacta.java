package impacta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Impacta {

    private Map<String, Voluntario> voluntarios;
    private Map<Integer, Acao> acoes;
    private int proximoId;

    public Impacta() {
        this.voluntarios = new HashMap<>();
        this.acoes = new HashMap<>();
        this.proximoId = 1;
    }



    public boolean cadastrarVoluntario(String nome, String email, String matricula) {
        exigirTexto(nome, "Nome inválido");
        exigirTexto(email, "Email inválido");
        exigirTexto(matricula, "Matrícula inválida");

        String chave = normalizar(email);
        if (voluntarios.containsKey(chave)) {
            throw new EmailDuplicadoException(email);
        }

        voluntarios.put(chave, new Voluntario(nome.trim(), email.trim(), matricula.trim()));
        return true;
    }

    public String exibirVoluntario(String email) {
        return buscarVoluntario(email).toString();
    }

    public String[] listarVoluntarios() {
        List<Voluntario> lista = new ArrayList<>(voluntarios.values());

        Collections.sort(lista, new Comparator<Voluntario>() {
            @Override
            public int compare(Voluntario v1, Voluntario v2) {
                int cmpPontos = Integer.compare(v2.getPontuacaoImpacto(), v1.getPontuacaoImpacto());
                if (cmpPontos != 0) {
                    return cmpPontos;
                }
                return v1.getNome().compareToIgnoreCase(v2.getNome());
            }
        });

        String[] resultado = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            resultado[i] = lista.get(i).toString();
        }
        return resultado;
    }



    public int cadastrarPlantio(String titulo, String descricao, String data,
                                int maxParticipantes, int qtdMudas) {
        validarAcao(titulo, descricao);
        int id = proximoId;
        registrar(new PlantioMudas(id, titulo, descricao, converterData(data), maxParticipantes, qtdMudas));
        return id;
    }

    public int cadastrarMutirao(String titulo, String descricao, String data,
                                int maxParticipantes, int duracaoHoras) {
        validarAcao(titulo, descricao);
        int id = proximoId;
        registrar(new MutiraoReciclagem(id, titulo, descricao, converterData(data), maxParticipantes, duracaoHoras));
        return id;
    }

    public int cadastrarOficina(String titulo, String descricao, String data,
                                int maxParticipantes, int duracaoHoras, boolean kitMaterial) {
        validarAcao(titulo, descricao);
        int id = proximoId;
        registrar(new OficinaEcologica(id, titulo, descricao, converterData(data), maxParticipantes,
                duracaoHoras, kitMaterial));
        return id;
    }

    public boolean inscreverVoluntario(String emailVoluntario, int idAcao) {
        Voluntario v = buscarVoluntario(emailVoluntario);
        Acao a = buscarAcao(idAcao);
        a.inscrever(v); // lança InscricaoDuplicadaException ou AcaoLotadaException
        return true;
    }

    public String exibirDetalhesAcao(int idAcao) {
        return buscarAcao(idAcao).exibirDetalhes();
    }



    private void registrar(Acao acao) {
        acoes.put(acao.getId(), acao);
        proximoId++;
    }

    private void validarAcao(String titulo, String descricao) {
        exigirTexto(titulo, "Título inválido");
        exigirTexto(descricao, "Descrição inválida");
    }

    private Voluntario buscarVoluntario(String email) {
        exigirTexto(email, "Email não pode ser vazio");
        Voluntario v = voluntarios.get(normalizar(email));
        if (v == null) {
            throw new IllegalArgumentException("Voluntário não encontrado");
        }
        return v;
    }

    private Acao buscarAcao(int id) {
        Acao a = acoes.get(id);
        if (a == null) {
            throw new IllegalArgumentException("Ação não encontrada");
        }
        return a;
    }

    private LocalDateTime converterData(String data) {
        exigirTexto(data, "Data não pode ser vazia");
        String texto = data.trim();
        try {
            return LocalDateTime.parse(texto);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDateTime.parse(texto, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } catch (DateTimeParseException e2) {
                try {
                    return LocalDate.parse(texto, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                } catch (DateTimeParseException e3) {
                    throw new IllegalArgumentException("Data em formato inválido");
                }
            }
        }
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().toLowerCase();
    }

    private void exigirTexto(String texto, String msg) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }
}