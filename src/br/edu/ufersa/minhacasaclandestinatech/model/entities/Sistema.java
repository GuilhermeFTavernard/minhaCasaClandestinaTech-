package br.edu.ufersa.minhacasaclandestinatech.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sistema {

    // Atributos
    private List<Equipamento> equipamentos;
    private List<Responsavel> responsaveis;
    private List<Local>       locais;

    // Construtores
    public Sistema() {
        this.equipamentos = new ArrayList<>();
        this.responsaveis = new ArrayList<>();
        this.locais       = new ArrayList<>();
    }

    // Gerenciamento de coleções
    public void adicionarEquipamento(Equipamento equipamento) {
        Objects.requireNonNull(equipamento, "Equipamento não pode ser nulo.");
        equipamentos.add(equipamento);
    }

    /** Adiciona um responsável ao sistema. */
    public void adicionarResponsavel(Responsavel responsavel) {
        Objects.requireNonNull(responsavel, "Responsável não pode ser nulo.");
        responsaveis.add(responsavel);
    }

    /** Adiciona um local ao sistema. */
    public void adicionarLocal(Local local) {
        Objects.requireNonNull(local, "Local não pode ser nulo.");
        locais.add(local);
    }

    // -------------------------------------------------------------------------
    // Métodos de pesquisa
    // -------------------------------------------------------------------------

    /**
     * Pesquisa equipamentos cujo nome contenha o mesmo nome do equipamento de referência.
     * A comparação é case-insensitive.
     *
     * @param referencia equipamento com o nome a ser buscado
     */
    public void pesquisarEquipamentoPorNome(Equipamento referencia) {
        Objects.requireNonNull(referencia, "Referência de equipamento não pode ser nula.");
        String nomeBusca = referencia.getNome().toLowerCase();

        System.out.printf("=== Pesquisa por Nome: '%s' ===%n", referencia.getNome());

        List<Equipamento> resultado = equipamentos.stream()
                .filter(e -> e.getNome().toLowerCase().contains(nomeBusca))
                .toList();

        imprimirResultado(resultado);
    }

    /**
     * Pesquisa equipamentos pelo número de série do equipamento de referência.
     *
     * @param referencia equipamento com o número de série a ser buscado
     */
    public void pesquisarPorNumeroSerie(Equipamento referencia) {
        Objects.requireNonNull(referencia, "Referência de equipamento não pode ser nula.");

        System.out.printf("=== Pesquisa por Nº de Série: %d ===%n", referencia.getNumeroSerie());

        List<Equipamento> resultado = equipamentos.stream()
                .filter(e -> e.getNumeroSerie() == referencia.getNumeroSerie())
                .toList();

        imprimirResultado(resultado);
    }

    /**
     * Lista todos os equipamentos e exibe o responsável de referência.
     *
     * @param responsavel responsável utilizado como filtro de contexto
     */
    public void pesquisarPorResponsavel(Responsavel responsavel) {
        Objects.requireNonNull(responsavel, "Responsável não pode ser nulo.");

        System.out.printf("=== Pesquisa por Responsável: '%s' ===%n", responsavel.getNome());

        // Verifica se o responsável está registrado no sistema
        boolean encontrado = responsaveis.stream()
                .anyMatch(r -> r.getNome().equalsIgnoreCase(responsavel.getNome()));

        if (!encontrado) {
            System.out.println("Responsável não encontrado no sistema.");
            return;
        }

        // Exibe todos os equipamentos sob esse responsável (lógica de associação futura)
        System.out.printf("Responsável '%s' localizado. Equipamentos disponíveis no sistema: %d%n",
                responsavel.getNome(), equipamentos.size());
        imprimirResultado(equipamentos);
    }

    /**
     * Lista todos os equipamentos registrados no local informado.
     *
     * @param local local de referência para a pesquisa
     */
    public void pesquisarPorLocal(Local local) {
        Objects.requireNonNull(local, "Local não pode ser nulo.");

        System.out.printf("=== Pesquisa por Local: '%s / %s' ===%n",
                local.getNomeCasa(), local.getNomeCompartimento());

        boolean encontrado = locais.stream()
                .anyMatch(l -> l.getNomeCasa().equalsIgnoreCase(local.getNomeCasa())
                        && l.getNomeCompartimento().equalsIgnoreCase(local.getNomeCompartimento()));

        if (!encontrado) {
            System.out.println("Local não encontrado no sistema.");
            return;
        }

        System.out.printf("Local '%s / %s' encontrado. Equipamentos disponíveis: %d%n",
                local.getNomeCasa(), local.getNomeCompartimento(), equipamentos.size());
        imprimirResultado(equipamentos);
    }

    // -------------------------------------------------------------------------
    // Helpers internos
    // -------------------------------------------------------------------------

    private void imprimirResultado(List<Equipamento> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum equipamento encontrado.");
        } else {
            lista.forEach(e -> System.out.println("  -> " + e));
        }
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // Getters (somente leitura via cópia defensiva)
    // -------------------------------------------------------------------------

    public List<Equipamento> getEquipamentos() { return List.copyOf(equipamentos); }
    public List<Responsavel> getResponsaveis()  { return List.copyOf(responsaveis); }
    public List<Local>       getLocais()         { return List.copyOf(locais); }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Sistema{equipamentos=%d, responsaveis=%d, locais=%d}"
                .formatted(equipamentos.size(), responsaveis.size(), locais.size());
    }
}
