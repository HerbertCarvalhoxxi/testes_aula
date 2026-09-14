describe('Monitor de Estufa - Análise de Valor Limite e Alertas (E2E)', () => {
  
  beforeEach(() => {
    cy.visit('http://localhost:8080');
  });

  it('Cenário 1: Valor Válido Padrão (22°C)', () => {
    cy.get('#temperatura-display').should('contain', '22°C');
    cy.get('#status-display').should('have.text', 'OK');
    cy.get('#app-container').should('have.class', 'bg-green-500');
  });

  it('Cenário 2: Limite Superior (30°C -> 31°C) validando Alerta e Botão (+)', () => {
    // Intercepta a janela do navegador e substitui o alert() real por um espião
    cy.window().then((win) => {
      cy.stub(win, 'alert').as('alertaJanela');
    });

    // Usa o input numérico para pular direto para o limite superior (30°C)
    cy.get('#input-temp').clear().type('30');
    cy.contains('button', 'Ajustar').click();

    // VALOR LIMITE (Válido): 30°C
    cy.get('#temperatura-display').should('contain', '30°C');
    cy.get('#status-display').should('have.text', 'OK');
    cy.get('#app-container').should('have.class', 'bg-green-500');

    // VALOR FORA DO LIMITE (Inválido): 31°C usando o botão de incremento
    cy.contains('button', '+').click();
    cy.get('#temperatura-display').should('contain', '31°C');
    cy.get('#status-display').should('have.text', 'ALERTA');
    cy.get('#app-container').should('have.class', 'bg-red-500');

    // PAUSA MÁGICA: Aguarda 200ms para garantir que o setTimeout do front-end executou
    cy.wait(200);

    // Verifica se o espião interceptou o texto correto
    cy.get('@alertaJanela').should('have.been.calledWith', 'ALERTA: Temperatura de 31°C excedeu o limite tolerável da estufa!');
  });

  it('Cenário 3: Limite Inferior (15°C -> 14°C) validando Alerta e Input de digitação', () => {
    // Repete a interceptação da janela
    cy.window().then((win) => {
      cy.stub(win, 'alert').as('alertaJanela');
    });

    // VALOR LIMITE (Válido): 15°C através do input
    cy.get('#input-temp').clear().type('15');
    cy.contains('button', 'Ajustar').click();

    cy.get('#temperatura-display').should('contain', '15°C');
    cy.get('#status-display').should('have.text', 'OK');
    cy.get('#app-container').should('have.class', 'bg-green-500');

    // VALOR FORA DO LIMITE (Inválido): 14°C inserindo também pelo input
    cy.get('#input-temp').clear().type('14');
    cy.contains('button', 'Ajustar').click();

    cy.get('#temperatura-display').should('contain', '14°C');
    cy.get('#status-display').should('have.text', 'ALERTA');
    cy.get('#app-container').should('have.class', 'bg-blue-500');

    // PAUSA MÁGICA
    cy.wait(200);

    // Verifica se o espião interceptou o texto correto de congelamento
    cy.get('@alertaJanela').should('have.been.calledWith', 'ALERTA: Temperatura de 14°C excedeu o limite tolerável da estufa!');
  });
});