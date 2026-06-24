UNDERTALE 3 - JavaFX FXML + PostgreSQL

Como configurar:

1) Crie o banco no PostgreSQL:
   CREATE DATABASE undertale3;

2) Abra o arquivo:
   src/main/java/com/mycompany/undertale_3/dao/ConnectionFactory.java

   Altere se precisar:
   URL      = jdbc:postgresql://localhost:5432/undertale3
   USER     = postgres
   PASSWORD = postgres

3) Rode no NetBeans:
   Botão direito no projeto > Clean and Build
   Depois Run

   IMPORTANTE: este projeto vem com nbactions.xml para o botão Run usar javafx:run.
   Se aparecer erro "JavaFX runtime components are missing", não rode com exec:exec.

Ou pelo terminal, dentro da pasta Undertale3_FXML:
   mvn clean javafx:run

O projeto cria automaticamente as tabelas:
- jogadores
- runs
- upgrades_jogador

Funções implementadas:
- Menu com nome do jogador
- Busca/criação de jogador no banco
- Botões de Ranking, Créditos e Encerrar
- CSS JavaFX com tema escuro/roxo
- Gameplay com mapa, inimigos, moedas, pontuação e inventário
- Inventário em Stage separado
- Loja com compra de itens
- Upgrades permanentes salvos no banco
- Ranking consultado no banco
- Boss desbloqueado após derrotar 3 inimigos
- Resultado da run salvo no banco
