# Sistema de Controle de Estoque — minhaCasaClandestinaTech

Aplicação desktop em **JavaFX** que replica o protótipo do Figma enviado:
um sistema de controle de estoque doméstico com Equipamentos, Locais,
Responsáveis, Clientes, Vendas, Compras e Relatórios.

## Requisitos

- **JDK 17** ou superior instalado e configurado (`JAVA_HOME`).
- **Maven 3.8+** instalado e com acesso à internet (para baixar as
  dependências do JavaFX no Maven Central na primeira execução).

> ⚠️ Este projeto foi desenvolvido em um ambiente sem acesso à rede do Maven
> Central, então **não foi possível compilar/rodar `mvn` aqui** para validar
> a build. Todo o código foi escrito e revisado manualmente com atenção à
> sintaxe (FXML, fx:id, imports, chaves/parênteses balanceados), mas é
> recomendável rodar `mvn compile` na sua máquina antes de mexer no código,
> para garantir que tudo compila no seu ambiente.

## Como executar

Na raiz do projeto (onde está o `pom.xml`):

```bash
mvn javafx:run
```

Isso baixa as dependências do JavaFX 21 (controls + fxml), compila o projeto
e abre a aplicação.

Alternativamente, para apenas compilar:

```bash
mvn compile
```

## Estrutura do projeto

```
estoque-javafx/
├── pom.xml
└── src/main/
    ├── java/com/minhacasa/estoque/
    │   ├── Main.java                 # ponto de entrada (Application)
    │   ├── AppContext.java           # acesso global ao MainController
    │   ├── model/                    # Equipamento, Local, Responsavel, Cliente,
    │   │                             # Venda, ItemVenda, Compra, StatusVenda
    │   ├── data/DataStore.java       # "banco de dados" em memória (com dados de exemplo)
    │   ├── util/                     # FormatUtil (moeda/data pt-BR), AlertUtil
    │   └── controller/                # um controller por tela/modal
    └── resources/com/minhacasa/estoque/
        ├── fxml/                     # uma tela/modal por arquivo .fxml
        └── css/styles.css            # estilo visual global (cores, sidebar, cards, tabelas, modais)
```

## Funcionalidades

- **Dashboard**: indicadores gerais (equipamentos, vendas ativas, clientes,
  locais) e lista das últimas vendas ativas.
- **Equipamentos**: listagem com busca e filtro por Nome / Nº Série /
  Responsável / Local, cadastro e edição via modal, exclusão.
- **Locais**: casa + compartimento + responsável, CRUD via modal.
- **Responsáveis**: nome, endereço, telefone. Não é possível excluir um
  responsável vinculado a equipamentos ou locais.
- **Clientes**: nome, CPF, endereço. Não é possível excluir um cliente com
  vendas registradas.
- **Vendas**: cadastro de venda com múltiplos itens (equipamento + quantidade),
  baixa automática no estoque, cancelamento (devolve os itens ao estoque) e
  visualização/impressão da nota de venda.
- **Compras**: registro de entradas de estoque, que incrementam automaticamente
  a quantidade do equipamento selecionado.
- **Relatórios**: relatório de vendas por período (com indicadores,
  equipamentos mais vendidos e detalhamento das vendas ativas) e relatório de
  posição de estoque.

Os dados são mantidos **em memória** (não há persistência em banco de dados
ou arquivo) — ao fechar a aplicação, tudo volta ao estado inicial de exemplo
definido em `DataStore.java`.

## Padrão dos modais

Todos os modais de cadastro/edição seguem o mesmo padrão visual do modal
"Novo/Editar Equipamento": cartão branco centralizado sobre um fundo
escurecido, título + botão de fechar (✕) no topo, campos com rótulo azul em
caixa alta, e botões "Cancelar" / "✓ Salvar" no rodapé.
