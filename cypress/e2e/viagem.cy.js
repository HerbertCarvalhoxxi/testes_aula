describe('Fluxo de Cadastro de Viagem E2E', () => {
  beforeEach(() => {
    // Acessa o nosso frontend
    cy.visit('http://localhost:8080/index.html');
  });

  it('Cenário Válido: Deve cadastrar usuário no limite de idade (18 anos)', () => {
    // Interage com os inputs mapeados pelos IDs
    cy.get('#nomeInput').type('Maria E2E');
    cy.get('#idadeInput').type('18'); // Limite inferior da classe válida
    cy.get('#btnReservar').click();

    // Valida a mudança de estado na UI
    cy.get('#mensagemRetorno')
      .should('have.class', 'sucesso')
      .and('contain.text', 'Passageiro adicionado com sucesso');
  });

  it('Cenário Inválido: Deve barrar usuário menor de idade (17 anos)', () => {
    cy.get('#nomeInput').type('Joãozinho QA');
    cy.get('#idadeInput').type('17'); // Limite superior da classe inválida
    cy.get('#btnReservar').click();

    // Verifica se a mensagem de erro da API foi repassada para a tela
    cy.get('#mensagemRetorno')
      .should('have.class', 'erro')
      .and('contain.text', 'Usuário deve ter 18 anos ou mais');
  });
});