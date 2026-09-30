# Critérios de Aceitação - Módulo de Gestão de Veículos

Este documento especifica os critérios de aceitação para as funcionalidades do sistema de gerenciamento de veículos, estruturados com base em técnicas formais e empíricas de teste de software (como Partição de Equivalência, Análise de Valor Limite e Descoberta de Erro).

---

## 1. Cadastro de Veículo Válido (Caminho Feliz)

* **História de Usuário:** Como Usuário, devo cadastrar um veículo com credenciais válidas.
* **Técnica Aplicada:** Partição de Equivalência (Classe Válida).

### Cenário: Cadastro com sucesso de um novo veículo
* **Pré-condições:** 
  * A aplicação deve estar em execução e a página de gestão de veículos acessível.
  * A placa informada não deve existir previamente no banco de dados.
* **Dado** que o usuário preenche o campo "Placa" com o valor `ABC-1234`
* **E** preenche o campo "Modelo" com o valor `Honda Civic`
* **E** preenche o campo "Proprietário" com o valor `Carlos Silva`
* **Quando** o usuário clica no botão "Cadastrar"
* **Então** o sistema deve processar a requisição e retornar o status HTTP `201 Created`
* **E** a interface deve exibir o veículo na tabela de frota com o status `ATIVO`

---

## 2. Tentativa de Cadastro com Placa Inválida ou Duplicada (Descoberta de Erro)

* **História de Usuário:** Como Usuário, não deve ser possível cadastrar um veículo com placa inválida ou já existente.
* **Técnica Aplicada:** Descoberta de Erro (*Error Guessing*) e Análise de Valor Limite / Tipagem.

### Cenário: Tentativa de cadastro violando a regra de duplicidade ou restrição
* **Pré-condições:** 
  * Já existe um veículo cadastrado com a placa `DUP-0001`.
* **Dado** que o usuário preenche o campo "Placa" com o valor duplicado `DUP-0001` ou com formato inválido/vazio
* **E** preenche os demais campos com dados válidos (`Gol`, `Ana`)
* **Quando** o usuário clica no botão "Cadastrar"
* **Então** o sistema deve interceptar a violação de regra de negócio
* **E** retornar o status HTTP `400 Bad Request` com a mensagem de erro descritiva (*"Veículo com placa já cadastrada."*)
* **E** a interface não deve duplicar o registro na tabela de frota

---

## 3. Cadastro e Desativação de um Veículo

* **História de Usuário:** Como usuário, devo cadastrar um veículo e desativá-lo.
* **Técnica Aplicada:** Transição de Estados.

### Cenário: Transição bem-sucedida do estado Ativo para Inativo
* **Pré-condições:** 
  * O veículo de placa `DES-1234` está previamente cadastrado e com status `ATIVO`.
* **Dado** que o usuário visualiza o veículo `DES-1234` na tabela com o indicador `ATIVO`
* **Quando** o usuário clica no botão "Desativar" correspondente à linha do veículo
* **E** confirma a ação no alerta de confirmação do navegador
* **Então** o sistema deve enviar uma requisição `PATCH` para a rota `/api/veiculos/DES-1234/desativar`
* **E** retornar o status HTTP `204 No Content`
* **E** a interface deve atualizar a exibição do status do veículo para `INATIVO`, ocultando o botão de desativação e habilitando o fluxo alternativo

---

## 4. Tentativa de Reativação de Veículo (Cenário de Falha Proposital / Descoberta de Erro)

* **História de Usuário:** Como usuário, devo tentar reativar um veículo (cenário onde a funcionalidade falha propositalmente ou não é suportada pelo servidor).
* **Técnica Aplicada:** Descoberta de Erro (*Error Guessing* / Limitação de API).

### Cenário: Comportamento ao interagir com uma funcionalidade não implementada no back-end
* **Pré-condições:** 
  * O veículo `DES-1234` encontra-se no estado `INATIVO` e exibe o botão "Reativar" na interface.
* **Dado** que o usuário clica no botão "Reativar" para o veículo inativo
* **E** confirma a intenção na caixa de diálogo
* **Quando** a interface envia a requisição de reativação para o endpoint `/api/veiculos/DES-1234/reativar` (rota inexistente no servidor)
* **Então** o servidor deve responder com o status HTTP `404 Not Found`
* **E** o front-end deve capturar a falha e exibir um alerta amigável informando que a operação falhou devido à indisponibilidade ou ausência do recurso no servidor