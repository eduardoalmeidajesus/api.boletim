package br.edu.utfpr.td.tsi.trabalho1.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BoletimFurtoVeiculo {
    private String identificador;
    private String crime;
    private LocalDate dataOcorrencia;
    private String periodoOcorrencia;
    private List<Parte> partes;
    private Endereco localOcorrencia;
    private Veiculo veiculoFurtado;

    public BoletimFurtoVeiculo() {
        this.identificador = UUID.randomUUID().toString();
    }

    public BoletimFurtoVeiculo(String crime, LocalDate dataOcorrencia, String periodoOcorrencia,
                              List<Parte> partes, Endereco localOcorrencia, Veiculo veiculoFurtado) {
        this();
        this.crime = crime;
        this.dataOcorrencia = dataOcorrencia;
        this.periodoOcorrencia = periodoOcorrencia;
        this.partes = partes;
        this.localOcorrencia = localOcorrencia;
        this.veiculoFurtado = veiculoFurtado;
    }

    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            this.identificador = UUID.randomUUID().toString();
        } else {
            this.identificador = identificador;
        }
    }

    public String getCrime() { return crime; }
    public void setCrime(String crime) { this.crime = crime; }

    public LocalDate getDataOcorrencia() { return dataOcorrencia; }
    public void setDataOcorrencia(LocalDate dataOcorrencia) { this.dataOcorrencia = dataOcorrencia; }

    public String getPeriodoOcorrencia() { return periodoOcorrencia; }
    public void setPeriodoOcorrencia(String periodoOcorrencia) { this.periodoOcorrencia = periodoOcorrencia; }

    public List<Parte> getPartes() { return partes; }
    public void setPartes(List<Parte> partes) { this.partes = partes; }

    public Endereco getLocalOcorrencia() { return localOcorrencia; }
    public void setLocalOcorrencia(Endereco localOcorrencia) { this.localOcorrencia = localOcorrencia; }

    public Veiculo getVeiculoFurtado() { return veiculoFurtado; }
    public void setVeiculoFurtado(Veiculo veiculoFurtado) { this.veiculoFurtado = veiculoFurtado; }
}