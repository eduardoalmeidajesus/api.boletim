package br.edu.utfpr.td.tsi.trabalho1.service;

import java.util.List;
import br.edu.utfpr.td.tsi.trabalho1.model.BoletimFurtoVeiculo;
import br.edu.utfpr.td.tsi.trabalho1.model.Veiculo;

public interface BoletimFurtoVeiculoService {
    void cadastrar(BoletimFurtoVeiculo boletim);
    void atualizar(String identificador, BoletimFurtoVeiculo boletim);
    void remover(String identificador);
    BoletimFurtoVeiculo mascararPartes(BoletimFurtoVeiculo boletim);
    BoletimFurtoVeiculo localizarPorId(String identificador);
    List<BoletimFurtoVeiculo> listarTodos();
    List<BoletimFurtoVeiculo> listarPorCidade(String cidade);
    List<BoletimFurtoVeiculo> listarPorPeriodo(String periodo);
    List<Veiculo> listarVeiculosPorPlaca(String placa);
    List<Veiculo> listarVeiculosPorCor(String cor);
    List<Veiculo> listarVeiculosPorTipo(String tipoVeiculo);
    void carregarDadosCSV(String caminhoArquivo);
}