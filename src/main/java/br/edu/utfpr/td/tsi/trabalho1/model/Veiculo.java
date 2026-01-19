package br.edu.utfpr.td.tsi.trabalho1.model;

public class Veiculo {
    private Integer anoFabricacao;
    private String cor;
    private String marca;
    private String tipoVeiculo;
    private Emplacamento emplacamento;

    public Veiculo() {}

    public Veiculo(Integer anoFabricacao, String cor, String marca, String tipoVeiculo, Emplacamento emplacamento) {
        this.anoFabricacao = anoFabricacao;
        this.cor = cor;
        this.marca = marca;
        this.tipoVeiculo = tipoVeiculo;
        this.emplacamento = emplacamento;
    }

    public Integer getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(Integer anoFabricacao) { this.anoFabricacao = anoFabricacao; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getTipoVeiculo() { return tipoVeiculo; }
    public void setTipoVeiculo(String tipoVeiculo) { this.tipoVeiculo = tipoVeiculo; }

    public Emplacamento getEmplacamento() { return emplacamento; }
    public void setEmplacamento(Emplacamento emplacamento) { this.emplacamento = emplacamento; }
}