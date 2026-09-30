describe('Testes de Aceitação - Gestão de Veículos (E2E)', () => {
  const FRONT_URL = 'http://localhost:8080/veiculos.html';
  const API_URL = 'http://localhost:8080/api/veiculos';

  // Gerador de placa única para evitar conflitos de duplicidade nos testes
  const gerarPlaca = () => `ACC-${Math.floor(1000 + Math.random() * 9000)}`;

  beforeEach(() => {
    cy.visit(FRONT_URL);
  });

  it('CT01 - Como Usuário, devo cadastrar um veículo com credenciais válidas', () => {
    const placaTest = gerarPlaca();

    // Preenche os campos do formulário na interface
    cy.get('#placa').clear().type(placaTest);
    cy.get('#modelo').clear().type('Chevrolet Onix');
    cy.get('#proprietario').clear().type('Ana Souza');

    // Submete o formulário
    cy.get('button[type="submit"]').click();

    // Valida a mensagem de sucesso na tela
    cy.get('#mensagem')
      .should('not.have.class', 'hidden')
      .and('contain', 'Veículo cadastrado com sucesso!');

    // Valida que o veículo recém-cadastrado apareceu na tabela da frota
    cy.get('#tabela-veiculos').should('contain', placaTest);
    cy.get('#tabela-veiculos').should('contain', 'Chevrolet Onix');
    cy.get('#tabela-veiculos').should('contain', 'ATIVO');
  });

  it('CT02 - Como Usuário, não deve ser possível cadastrar um veículo com placa duplicada (Descoberta de Erro / Validação)', () => {
    const placaTest = gerarPlaca();
    const dadosVeiculo = {
      placa: placaTest,
      modelo: 'Volkswagen Gol',
      proprietario: 'Carlos Eduardo'
    };

    // Pré-requisito: Cadastra o primeiro veículo diretamente via API para garantir estado
    cy.request('POST', API_URL, dadosVeiculo);
    cy.reload(); // Recarrega a página para atualizar a tabela

    // Tenta cadastrar na interface a mesma placa (Cenário de Erro)
    cy.get('#placa').clear().type(placaTest);
    cy.get('#modelo').clear().type('Volkswagen Gol Duplicado');
    cy.get('#proprietario').clear().type('Outro Dono');
    cy.get('button[type="submit"]').click();

    // Valida que a mensagem de erro foi tratada e exibida na interface
    cy.get('#mensagem')
      .should('not.have.class', 'hidden')
      .and('contain', 'Erro:');
  });

  it('CT03 - Como usuário, devo cadastrar um veículo e desativá-lo', () => {
    const placaTest = gerarPlaca();

    // Cadastra via API para agilizar o teste de estado
    cy.request('POST', API_URL, {
      placa: placaTest,
      modelo: 'Fiat Palio',
      proprietario: 'Marcos Paulo'
    });

    cy.reload();

    // Localiza a linha do veículo na tabela e clica no botão "Desativar"
    cy.contains('tr', placaTest).within(() => {
      cy.get('span').should('have.text', 'ATIVO');
      cy.contains('button', 'Desativar').click();
    });

    // Valida que o status visual mudou para INATIVO
    cy.contains('tr', placaTest).within(() => {
      cy.get('span').should('have.text', 'INATIVO');
    });
  });

  it('CT04 - Como usuário, devo tentar reativar um veículo e observar a falha intencional (Descoberta de Erro / Funcionalidade Não Implementada)', () => {
    const placaTest = gerarPlaca();

    // Cadastra e desativa via API
    cy.request('POST', API_URL, {
      placa: placaTest,
      modelo: 'Renault Kwid',
      proprietario: 'Juliana'
    });
    cy.request('PATCH', `${API_URL}/${placaTest}/desativar`);

    cy.reload();

    // Intercepta a janela do navegador para capturar o alerta gerado pelo botão que "não funciona"
    cy.window().then((win) => {
      cy.stub(win, 'alert').as('alertaJanela');
    });

    // Localiza o veículo inativo e clica no botão "Reativar"
    cy.contains('tr', placaTest).within(() => {
      cy.get('span').should('have.text', 'INATIVO');
      cy.contains('button', 'Reativar').click();
    });

    cy.wait(200);

    // Valida que a técnica de descoberta de erro identificou o comportamento de falha (endpoint ausente/404)
    cy.get('@alertaJanela').should(
      'have.been.calledWith',
      'Falha na operação: O endpoint de reativação retornou status 404 (Funcionalidade não implementada no servidor).'
    );
  });
});