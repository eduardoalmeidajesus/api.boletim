const API_BASE = 'http://localhost:8080/api/boletins';

function showSection(sectionId) {
    document.querySelectorAll('.section').forEach(section => {
        section.style.display = 'none';
    });
    document.getElementById(sectionId).style.display = 'block';
}

function mostrarMensagem(mensagem, tipo = 'info') {
    const div = document.getElementById('mensagens');
    div.innerHTML = `<div class="mensagem ${tipo}">${mensagem}</div>`;
    setTimeout(() => div.innerHTML = '', 3000);
}

// Cadastro de boletim
document.getElementById('formBoletim').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const boletim = {
        crime: document.getElementById('crime').value,
        dataOcorrencia: document.getElementById('dataOcorrencia').value,
        periodoOcorrencia: document.getElementById('periodoOcorrencia').value,
        localOcorrencia: {
            logradouro: document.getElementById('logradouro').value,
            numero: document.getElementById('numero').value,
            bairro: document.getElementById('bairro').value,
            cidade: document.getElementById('cidade').value,
            estado: document.getElementById('estado').value
        },
        veiculoFurtado: {
            anoFabricacao: parseInt(document.getElementById('anoFabricacao').value),
            cor: document.getElementById('cor').value,
            marca: document.getElementById('marca').value,
            tipoVeiculo: document.getElementById('tipoVeiculo').value,
            emplacamento: {
                placa: document.getElementById('placa').value,
                estado: document.getElementById('estadoPlaca').value,
                cidade: document.getElementById('cidadePlaca').value
            }
        }
    };

    try {
        const response = await fetch(API_BASE, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(boletim)
        });

        if (response.ok) {
            mostrarMensagem('Boletim cadastrado com sucesso!', 'sucesso');
            document.getElementById('formBoletim').reset();
        } else {
            const erro = await response.json();
            mostrarMensagem('Erro: ' + erro.erro, 'erro');
        }
    } catch (error) {
        mostrarMensagem('Erro de conexão: ' + error.message, 'erro');
    }
});

// Listar todos os boletins
async function listarTodos() {
    try {
        const response = await fetch(API_BASE);
        const boletins = await response.json();
        
        const lista = document.getElementById('listaBoletins');
        lista.innerHTML = boletins.map(boletim => `
            <div class="boletim-card">
                <h4>Boletim ${boletim.identificador}</h4>
                <p><strong>Crime:</strong> ${boletim.crime}</p>
                <p><strong>Data:</strong> ${boletim.dataOcorrencia}</p>
                <p><strong>Período:</strong> ${boletim.periodoOcorrencia}</p>
                <p><strong>Local:</strong> ${boletim.localOcorrencia.cidade} - ${boletim.localOcorrencia.estado}</p>
                <p><strong>Veículo:</strong> ${boletim.veiculoFurtado.marca} ${boletim.veiculoFurtado.cor} - ${boletim.veiculoFurtado.emplacamento.placa}</p>
                <button onclick="excluirBoletim('${boletim.identificador}')">Excluir</button>
            </div>
        `).join('');
    } catch (error) {
        mostrarMensagem('Erro ao carregar boletins', 'erro');
    }
}

// Buscar por ID
async function buscarPorId() {
    const id = document.getElementById('buscaId').value;
    if (!id) return mostrarMensagem('Digite um ID', 'erro');
    
    try {
        const response = await fetch(`${API_BASE}/${id}`);
        if (response.ok) {
            const boletim = await response.json();
            document.getElementById('resultadoBusca').innerHTML = `
                <div class="boletim-card">
                    <h4>Boletim ${boletim.identificador}</h4>
                    <p><strong>Crime:</strong> ${boletim.crime}</p>
                    <p><strong>Data:</strong> ${boletim.dataOcorrencia}</p>
                    <p><strong>Período:</strong> ${boletim.periodoOcorrencia}</p>
                    <p><strong>Local:</strong> ${boletim.localOcorrencia.logradouro}, ${boletim.localOcorrencia.numero} - ${boletim.localOcorrencia.bairro}, ${boletim.localOcorrencia.cidade}</p>
                    <p><strong>Veículo:</strong> ${boletim.veiculoFurtado.marca} ${boletim.veiculoFurtado.cor} - ${boletim.veiculoFurtado.emplacamento.placa}</p>
                </div>
            `;
        } else {
            mostrarMensagem('Boletim não encontrado', 'erro');
        }
    } catch (error) {
        mostrarMensagem('Erro na busca', 'erro');
    }
}

// Buscar por cidade
async function buscarPorCidade() {
    const cidade = document.getElementById('buscaCidade').value;
    if (!cidade) return mostrarMensagem('Digite uma cidade', 'erro');
    
    try {
        const response = await fetch(`${API_BASE}/cidade/${encodeURIComponent(cidade)}`);
        const boletins = await response.json();
        exibirResultadosBusca(boletins);
    } catch (error) {
        mostrarMensagem('Erro na busca', 'erro');
    }
}

// Buscar por período
async function buscarPorPeriodo() {
    const periodo = document.getElementById('buscaPeriodo').value;
    if (!periodo) return mostrarMensagem('Selecione um período', 'erro');
    
    try {
        const response = await fetch(`${API_BASE}/periodo/${encodeURIComponent(periodo)}`);
        const boletins = await response.json();
        exibirResultadosBusca(boletins);
    } catch (error) {
        mostrarMensagem('Erro na busca', 'erro');
    }
}

function exibirResultadosBusca(boletins) {
    const resultado = document.getElementById('resultadoBusca');
    if (boletins.length === 0) {
        resultado.innerHTML = '<p>Nenhum boletim encontrado</p>';
        return;
    }
    
    resultado.innerHTML = boletins.map(boletim => `
        <div class="boletim-card">
            <h4>Boletim ${boletim.identificador}</h4>
            <p><strong>Crime:</strong> ${boletim.crime}</p>
            <p><strong>Data:</strong> ${boletim.dataOcorrencia}</p>
            <p><strong>Período:</strong> ${boletim.periodoOcorrencia}</p>
            <p><strong>Local:</strong> ${boletim.localOcorrencia.cidade}</p>
            <p><strong>Veículo:</strong> ${boletim.veiculoFurtado.marca} - ${boletim.veiculoFurtado.emplacamento.placa}</p>
        </div>
    `).join('');
}

// Buscar veículos
async function buscarPorPlaca() {
    const placa = document.getElementById('buscaPlaca').value;
    if (!placa) return mostrarMensagem('Digite uma placa', 'erro');
    
    try {
        const response = await fetch(`${API_BASE}/veiculos?placa=${placa}`);
        const veiculos = await response.json();
        exibirResultadosVeiculos(veiculos);
    } catch (error) {
        mostrarMensagem('Erro na busca', 'erro');
    }
}

async function buscarPorCor() {
    const cor = document.getElementById('buscaCor').value;
    if (!cor) return mostrarMensagem('Digite uma cor', 'erro');
    
    try {
        const response = await fetch(`${API_BASE}/veiculos?cor=${cor}`);
        const veiculos = await response.json();
        exibirResultadosVeiculos(veiculos);
    } catch (error) {
        mostrarMensagem('Erro na busca', 'erro');
    }
}

async function buscarPorTipo() {
    const tipo = document.getElementById('buscaTipo').value;
    if (!tipo) return mostrarMensagem('Selecione um tipo', 'erro');
    
    try {
        const response = await fetch(`${API_BASE}/veiculos?tipo=${tipo}`);
        const veiculos = await response.json();
        exibirResultadosVeiculos(veiculos);
    } catch (error) {
        mostrarMensagem('Erro na busca', 'erro');
    }
}

function exibirResultadosVeiculos(veiculos) {
    const resultado = document.getElementById('resultadoVeiculos');
    if (veiculos.length === 0) {
        resultado.innerHTML = '<p>Nenhum veículo encontrado</p>';
        return;
    }
    
    resultado.innerHTML = veiculos.map(veiculo => `
        <div class="veiculo-card">
            <h4>Veículo ${veiculo.emplacamento.placa}</h4>
            <p><strong>Marca:</strong> ${veiculo.marca}</p>
            <p><strong>Cor:</strong> ${veiculo.cor}</p>
            <p><strong>Tipo:</strong> ${veiculo.tipoVeiculo}</p>
            <p><strong>Ano:</strong> ${veiculo.anoFabricacao}</p>
            <p><strong>Emplacamento:</strong> ${veiculo.emplacamento.cidade} - ${veiculo.emplacamento.estado}</p>
        </div>
    `).join('');
}

// Excluir boletim
async function excluirBoletim(id) {
    if (!confirm('Tem certeza que deseja excluir este boletim?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
        if (response.ok) {
            mostrarMensagem('Boletim excluído com sucesso', 'sucesso');
            listarTodos();
        }
    } catch (error) {
        mostrarMensagem('Erro ao excluir', 'erro');
    }
}

// Carregar CSV
async function carregarCSV() {
    try {
        const response = await fetch(`${API_BASE}/carregar-csv`, { method: 'POST' });
        if (response.ok) {
            mostrarMensagem('Dados do CSV carregados com sucesso!', 'sucesso');
        } else {
            mostrarMensagem('Erro ao carregar CSV', 'erro');
        }
    } catch (error) {
        mostrarMensagem('Erro de conexão', 'erro');
    }
}

// Mostrar seção de listagem por padrão
showSection('cadastro');