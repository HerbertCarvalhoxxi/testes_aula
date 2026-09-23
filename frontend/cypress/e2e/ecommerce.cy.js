describe('E-commerce - Testes de Sistema (Fluxos Lógicos)', () => {

  beforeEach(() => {
    // Aceder à página principal servida pelo Live Server ou local
    cy.visit('http://127.0.0.1:5500/frontend/index.html');
  });

  // Fluxo 1: Caminho Feliz (Happy Path)
  it('Fluxo 1: Deve efetuar compra com sucesso no caminho ideal', () => {
    // Login com o utilizador com saldo suficiente
    cy.get('#email').clear().type('com_saldo@email.com');
    cy.get('#senha').clear().type('123456');
    cy.get('#auth-btn').click();

    // Validar transição para a tela da loja
    cy.get('#store-screen').should('be.visible');
    cy.get('#user-saldo').should('contain', '3000.00');

    // Adicionar produto ao carrinho (Notebook Gamer - R$ 2500.00)
    // Substitua esta linha:
// cy.contains('div', 'Notebook').find('button').click();

// Por esta linha mais precisa:
    cy.contains('h3', 'Notebook').parents('div.bg-white').find('button').click();
    cy.get('#cart-count').should('contain', '1');

    // Finalizar compra
    cy.contains('Efetuar Compra').click();

    // Validar sucesso e atualização de saldo
    cy.contains('Compra efetuada com sucesso!').should('be.visible');
    cy.get('#user-saldo').should('contain', '500.00'); // 3000 - 2500
    cy.get('#cart-count').should('contain', '0');
  });

  // Fluxo 2: Tentar comprar com saldo insuficiente
  it('Fluxo 2: Deve bloquear compra caso o saldo seja insuficiente', () => {
    // Registar um utilizador novo com saldo padrão baixo ou forçar login com o sem_saldo
    cy.get('#email').clear().type('sem_saldo@email.com');
    cy.get('#senha').clear().type('123456');
    cy.get('#auth-btn').click();

    cy.get('#store-screen').should('be.visible');
    cy.get('#user-saldo').should('contain', '10.00');

    // Tentar comprar o Notebook Gamer (R$ 2500.00) adicionando ao carrinho
    cy.contains('h3', 'Notebook Gamer').parents('div.bg-white').find('button').click();
    
    // Tentar finalizar a compra
    cy.contains('Efetuar Compra').click();

    // Validar mensagem de erro gerada pelo back-end
    cy.contains('Saldo insuficiente para realizar a compra.').should('be.visible');
  });

  // Fluxo 3: Tentar finalizar com o carrinho vazio (Escolha analógica do nosso planeamento)
  it('Fluxo 3: Deve bloquear checkout se o carrinho estiver vazio', () => {
    cy.get('#email').clear().type('com_saldo@email.com');
    cy.get('#senha').clear().type('123456');
    cy.get('#auth-btn').click();

    cy.get('#store-screen').should('be.visible');
    cy.get('#cart-count').should('contain', '0');

    // Clicar em finalizar compra sem itens
    cy.contains('Efetuar Compra').click();

    // Validar mensagem de restrição do negócio
    cy.contains('Seu carrinho está vazio. Adicione produtos antes de finalizar a compra.').should('be.visible');
  });

  // Fluxo 4: Tentar adicionar produto sem estoque
  it('Fluxo 4: Deve impedir transação se o produto estiver sem estoque', () => {
    cy.get('#email').clear().type('com_saldo@email.com');
    cy.get('#senha').clear().type('123456');
    cy.get('#auth-btn').click();

    cy.get('#store-screen').should('be.visible');

    // Tentar adicionar o "Tênis Esgotado" (Estoque = 0)
    cy.contains('h3', 'Esgotado').parents('div.bg-white').find('button').click();

    // Validar alerta visual do Toast indicando falta de estoque
    cy.contains('Produto sem estoque suficiente').should('be.visible');
    cy.get('#cart-count').should('contain', '0');
  });

  // Fluxo 5: Tentar realizar login sem sucesso (Credenciais inválidas)
  it('Fluxo 5: Deve exibir erro ao tentar login com credenciais inválidas', () => {
    cy.get('#email').clear().type('email_inexistente@email.com');
    cy.get('#senha').clear().type('senhaerrada');
    cy.get('#auth-btn').click();

    // Validar que a tela da loja não abriu e a mensagem de erro apareceu
    cy.get('#store-screen').should('not.be.visible');
    cy.get('#auth-erro').should('contain', 'E-mail ou senha inválidos');
  });

});