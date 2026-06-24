# Xadrez

Jogo de xadrez para dois jogadores com interface gráfica em Java Swing, implementando as regras completas do xadrez FIDE, incluindo roque, en passant, promoção de peão, xeque, xeque-mate e afogamento.

---

## Sumário

- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e execução](#instalação-e-execução)
- [Como jogar](#como-jogar)
- [Salvar e carregar partidas](#salvar-e-carregar-partidas)
- [Arquitetura](#arquitetura)
- [Estrutura de pacotes](#estrutura-de-pacotes)
- [Regras implementadas](#regras-implementadas)
- [Formato do arquivo de partida](#formato-do-arquivo-de-partida)

---

## Funcionalidades

- Tabuleiro 8×8 com peças em Unicode (♔ ♕ ♖ ♗ ♘ ♙)
- Destaque visual da peça selecionada e dos movimentos legais
- Validação completa de movimentos (nenhum movimento que deixe o próprio rei em xeque é permitido)
- Roque curto (O-O) e roque longo (O-O-O)
- Captura en passant
- Promoção automática de peão para Dama ao atingir a última fileira
- Detecção de xeque, xeque-mate e afogamento
- Histórico de jogadas em notação algébrica padrão (ex.: `1. e4 b5`)
- Salvar e carregar partidas em arquivo de texto
- Exibição dos nomes dos jogadores configurados no início da partida

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|------------|--------------|
| Java (JDK) | 8            |
| Maven      | 3.6          |

Verifique as instalações:

```bash
java -version
mvn -version
```

---

## Instalação e execução

### 1. Clone o repositório

```bash
git clone https://github.com/iury-Ghub/Xadrez.git
cd Xadrez
```

### 2. Compile o projeto

```bash
mvn compile
```

### 3. Execute

```bash
mvn exec:java
```

Ou gere e execute o JAR:

```bash
mvn package
java -jar target/xadrez-mvp-1.0.0.jar
```

---

## Como jogar

1. Ao iniciar, insira os nomes dos dois jogadores nas caixas de diálogo.
2. As Brancas sempre jogam primeiro.
3. **Clique** na peça que deseja mover — ela ficará destacada em verde e os destinos legais aparecerão em amarelo.
4. **Clique** em um destino destacado para executar o movimento.
5. Clicar na mesma peça novamente cancela a seleção; clicar em outra peça da mesma cor seleciona a nova peça.
6. A barra lateral exibe o turno atual, o status da partida e o histórico de jogadas.
7. Use o botão **Novo jogo** para reiniciar a qualquer momento.

---

## Salvar e carregar partidas

- **Salvar** — grava todos os movimentos da partida atual no arquivo `partida.txt` no diretório de execução.
- **Carregar** — lê o arquivo `partida.txt` e repete os movimentos automaticamente, restaurando o estado da partida.

> O arquivo é sobrescrito a cada salvamento. Renomeie-o se quiser preservar várias partidas.

---

## Arquitetura

O projeto segue o padrão **MVP (Model-View-Presenter)**, separando claramente as responsabilidades:

```
┌─────────────────────────────────────────────────────┐
│                      View (UI)                      │
│  ChessFrame  ──►  ChessBoardPanel                   │
│  (janela principal)   (tabuleiro 8×8 de JButtons)   │
└────────────────────────┬────────────────────────────┘
                         │ eventos de clique
                         ▼
┌─────────────────────────────────────────────────────┐
│                   Controller                        │
│               ChessController                       │
│  (traduz eventos da UI em chamadas ao modelo)       │
└────────────────────────┬────────────────────────────┘
                         │ chamadas de método
                         ▼
┌─────────────────────────────────────────────────────┐
│                      Model                          │
│   Game ──► Board ──► Piece (subtipos)               │
│   (estado da partida, regras, validação de lances)  │
└─────────────────────────────────────────────────────┘
```

### Fluxo de um movimento

1. O usuário clica em uma casa → `SquareClickListener.onSquareClicked(Position)`
2. `ChessFrame` chama `ChessController.handleSquareClick(position)`
3. O controller delega para `Game.click(position)`
4. `Game` gerencia a máquina de estados: primeiro clique seleciona a peça e calcula os movimentos legais; segundo clique executa o movimento
5. A execução atualiza o `Board`, os direitos de roque, o estado de en passant e verifica xeque/xeque-mate/afogamento
6. `ChessFrame.refreshView()` redesenha o tabuleiro e atualiza o painel lateral

### Validação de movimentos legais

Para cada candidato de movimento pseudo-legal, o `Game` cria uma cópia do `Board` (`Board.copy()`), aplica o movimento nessa cópia e verifica se o rei ficaria em xeque. Apenas movimentos que deixam o rei seguro são incluídos na lista de movimentos legais.

---

## Estrutura de pacotes

```
src/
└── chess/
    ├── Main.java                        # Ponto de entrada; coleta nomes e inicializa MVC
    ├── controller/
    │   └── ChessController.java         # Fachada entre View e Model
    ├── model/
    │   ├── Alliance.java                # Enum WHITE / BLACK com nome de exibição
    │   ├── Board.java                   # Grade 8×8; movimentação, cópia e detecção de ataques
    │   ├── Game.java                    # Máquina de estados da partida e regras
    │   ├── GameState.java               # Enum ACTIVE / CHECK / CHECKMATE / STALEMATE
    │   ├── MoveNotationFormatter.java   # Gera notação algébrica padrão
    │   ├── MoveRecord.java              # Registro imutável de um lance
    │   ├── Position.java                # (linha, coluna) com conversão algébrica
    │   └── pieces/
    │       ├── Piece.java               # Classe abstrata base
    │       ├── SlidingPiece.java        # Base para peças deslizantes (Bispo, Torre, Dama)
    │       ├── Bishop.java
    │       ├── King.java
    │       ├── Knight.java
    │       ├── Pawn.java
    │       ├── Queen.java
    │       └── Rook.java
    ├── ui/
    │   ├── ChessBoardPanel.java         # Grade de JButtons com renderização visual
    │   ├── ChessFrame.java              # Janela principal com tabuleiro e painel lateral
    │   └── SquareClickListener.java     # Interface de callback para cliques
    └── util/
        ├── DebugLog.java                # Log de operações no console
        └── GameFileManeger.java         # Serialização/desserialização de partidas
```

---

## Regras implementadas

| Regra | Suporte |
|-------|---------|
| Movimentos básicos de todas as peças | Sim |
| Prevenção de auto-xeque | Sim |
| Xeque e xeque-mate | Sim |
| Afogamento (stalemate) | Sim |
| Roque curto (O-O) | Sim |
| Roque longo (O-O-O) | Sim |
| En passant | Sim |
| Promoção de peão | Sim (automática para Dama) |
| Empate por repetição / material insuficiente | Não |

---

## Formato do arquivo de partida

O arquivo `partida.txt` armazena um par de movimentos por linha (brancas e pretas), usando notação algébrica de origem-destino:

```
e2-e4 b7-b5
d2-d3 c7-c6
g1-f3 e7-e6
e1-g1 g8-f6
```

Cada token tem o formato `<casa_origem>-<casa_destino>`, onde as casas seguem a notação padrão (`a1`–`h8`).