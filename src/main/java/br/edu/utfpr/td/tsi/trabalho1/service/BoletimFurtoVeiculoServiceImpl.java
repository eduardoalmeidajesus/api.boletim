package br.edu.utfpr.td.tsi.trabalho1.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.edu.utfpr.td.tsi.trabalho1.model.BoletimFurtoVeiculo;
import br.edu.utfpr.td.tsi.trabalho1.model.Emplacamento;
import br.edu.utfpr.td.tsi.trabalho1.model.Endereco;
import br.edu.utfpr.td.tsi.trabalho1.model.Veiculo;

@Component
public class BoletimFurtoVeiculoServiceImpl implements BoletimFurtoVeiculoService {

    private final List<BoletimFurtoVeiculo> boletins = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void cadastrar(BoletimFurtoVeiculo boletim) {
        if (boletim == null) throw new IllegalArgumentException("Boletim nulo");
        validarBoletim(boletim);
        boletins.add(boletim);
    }

    @Override
    public void atualizar(String identificador, BoletimFurtoVeiculo boletim) {
        if (identificador == null) throw new IllegalArgumentException("Identificador inválido");
        validarBoletim(boletim);
        remover(identificador);
        boletim.setIdentificador(identificador);
        boletins.add(boletim);
    }

    @Override
    public void remover(String identificador) {
        boletins.removeIf(b -> b.getIdentificador().equals(identificador));
    }

    @Override
    public BoletimFurtoVeiculo localizarPorId(String identificador) {
        return boletins.stream()
                .filter(b -> b.getIdentificador().equals(identificador))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BoletimFurtoVeiculo> listarTodos() {
        return new ArrayList<>(boletins);
    }

    @Override
    public List<BoletimFurtoVeiculo> listarPorCidade(String cidade) {
        return boletins.stream()
                .filter(b -> b.getLocalOcorrencia() != null && cidade.equalsIgnoreCase(b.getLocalOcorrencia().getCidade()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BoletimFurtoVeiculo> listarPorPeriodo(String periodo) {
        return boletins.stream()
                .filter(b -> periodo != null && periodo.equalsIgnoreCase(b.getPeriodoOcorrencia()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Veiculo> listarVeiculosPorPlaca(String placa) {
        return boletins.stream()
                .map(BoletimFurtoVeiculo::getVeiculoFurtado)
                .filter(v -> v != null && v.getEmplacamento() != null && placa.equalsIgnoreCase(v.getEmplacamento().getPlaca()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Veiculo> listarVeiculosPorCor(String cor) {
        return boletins.stream()
                .map(BoletimFurtoVeiculo::getVeiculoFurtado)
                .filter(v -> v != null && cor.equalsIgnoreCase(v.getCor()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Veiculo> listarVeiculosPorTipo(String tipoVeiculo) {
        return boletins.stream()
                .map(BoletimFurtoVeiculo::getVeiculoFurtado)
                .filter(v -> v != null && tipoVeiculo.equalsIgnoreCase(v.getTipoVeiculo()))
                .collect(Collectors.toList());
    }

    @Override
    public void carregarDadosCSV(String caminhoArquivo) {
        boletins.clear();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(caminhoArquivo);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {

            if (inputStream == null) throw new RuntimeException("Arquivo não encontrado");

            br.readLine();

            String linha;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] valores = linha.split("\t", -1);
                
                if (valores.length < 50) continue;

                try {
                    BoletimFurtoVeiculo boletim = criarBoletimFromCSV(valores, formatter);
                    if (boletim != null) {
                        boletins.add(boletim);
                    }
                } catch (Exception e) {
                    continue;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo CSV");
        }
    }

    private BoletimFurtoVeiculo criarBoletimFromCSV(String[] linha, DateTimeFormatter formatter) {
        try {
            String placa = getValor(linha, 44);
            String estadoVeiculo = getValor(linha, 45);
            String cidadeVeiculo = getValor(linha, 46);
            String corVeiculo = getValor(linha, 47);
            String marcaVeiculo = getValor(linha, 48);
            String anoFabricacaoStr = getValor(linha, 49);
            String tipoVeiculo = getValor(linha, 51);

            if (placa == null || placa.trim().isEmpty()) return null;

            Emplacamento emplacamento = new Emplacamento(placa, estadoVeiculo, cidadeVeiculo);

            Integer ano = parseAno(anoFabricacaoStr);
            String tipoNormalizado = normalizarTipoVeiculo(tipoVeiculo);

            Veiculo veiculo = new Veiculo(ano, corVeiculo, marcaVeiculo, tipoNormalizado, emplacamento);

            String logradouro = getValor(linha, 13);
            String numero = getValor(linha, 14);
            String bairro = getValor(linha, 15);
            String cidadeOcorrencia = getValor(linha, 16);
            String estadoOcorrencia = getValor(linha, 17);

            Endereco endereco = new Endereco(logradouro, numero, bairro, cidadeOcorrencia, estadoOcorrencia);

            String crime = getValor(linha, 24);
            String dataOcorrenciaStr = getValor(linha, 5);
            String periodoOcorrencia = getValor(linha, 7);

            BoletimFurtoVeiculo boletim = new BoletimFurtoVeiculo();
            boletim.setCrime(crime != null ? crime : "Furto de veículo");
            boletim.setDataOcorrencia(parseData(dataOcorrenciaStr, formatter));
            boletim.setPeriodoOcorrencia(periodoOcorrencia);
            boletim.setLocalOcorrencia(endereco);
            boletim.setVeiculoFurtado(veiculo);
            
            return boletim;

        } catch (Exception e) {
            return null;
        }
    }

    private String normalizarTipoVeiculo(String tipo) {
        if (tipo == null) return "DESCONHECIDO";
        String tipoUpper = tipo.toUpperCase();
        if (tipoUpper.contains("AUTOMOVEL")) return "CARRO";
        if (tipoUpper.contains("MOTOCICLO")) return "MOTO";
        if (tipoUpper.contains("CAMINHONETE") || tipoUpper.contains("CAMINHAO")) return "CAMINHAO";
        if (tipoUpper.contains("CAMIONETA")) return "CAMIONETA";
        if (tipoUpper.contains("REBOQUE")) return "REBOQUE";
        if (tipoUpper.contains("UTILITARIO")) return "UTILITARIO";
        return tipo;
    }

    private String getValor(String[] arr, int idx) {
        if (idx < 0 || idx >= arr.length) return null;
        String v = arr[idx];
        return (v == null || v.trim().isEmpty()) ? null : v.trim();
    }

    private LocalDate parseData(String dataStr, DateTimeFormatter formatter) {
        if (dataStr == null) return null;
        try {
            if (dataStr.contains(" ")) dataStr = dataStr.split(" ")[0];
            return LocalDate.parse(dataStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseAno(String anoStr) {
        if (anoStr == null) return null;
        try {
            String apenasNumeros = anoStr.replaceAll("[^0-9]", "");
            if (apenasNumeros.length() == 4) {
                int ano = Integer.parseInt(apenasNumeros);
                if (ano >= 1950 && ano <= 2030) return ano;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private void validarBoletim(BoletimFurtoVeiculo boletim) {
        if (boletim.getCrime() == null || boletim.getCrime().trim().isEmpty()) throw new IllegalArgumentException("Crime é obrigatório");
        if (boletim.getDataOcorrencia() == null) throw new IllegalArgumentException("Data da ocorrência é obrigatória");
        if (boletim.getVeiculoFurtado() == null) throw new IllegalArgumentException("Veículo furtado é obrigatório");
    }

    @Override
    public BoletimFurtoVeiculo mascararPartes(BoletimFurtoVeiculo boletim) {
        if (boletim == null) return null;
        BoletimFurtoVeiculo copia = new BoletimFurtoVeiculo();
        copia.setIdentificador(boletim.getIdentificador());
        copia.setCrime(boletim.getCrime());
        copia.setDataOcorrencia(boletim.getDataOcorrencia());
        copia.setPeriodoOcorrencia(boletim.getPeriodoOcorrencia());
        copia.setLocalOcorrencia(boletim.getLocalOcorrencia());
        copia.setVeiculoFurtado(boletim.getVeiculoFurtado());
        copia.setPartes(null);
        return copia;
    }
}