# ArenaConecta - Entrega Parte 1

## A. Histórias de Usuário

### 1. User Story (Âncora) - Contexto: Scheduling
**Persona:** Cliente  
**História:** Como Cliente, quero agendar uma visita para um horário específico para que eu possa garantir meu acesso às instalações da arena sem enfrentar filas ou superlotação.

**Critérios de Aceite:**
1. **Validação de Capacidade:** O sistema deve validar se o horário escolhido possui vagas disponíveis (comparando agendamentos atuais vs. capacidade máxima configurada) antes de confirmar.
2. **Unicidade de Agendamento:** O sistema deve impedir que o mesmo cliente realize dois agendamentos simultâneos para o mesmo bloco de horário (Visit).
3. **Persistência e Status:** Após a conclusão bem-sucedida, o agendamento deve ser salvo com o status "CONFIRMADO" e o cliente deve ser redirecionado para a tela de visualização do ticket.

---

### 2. Job Story - Contexto: Waitlist (Evento/Serviço)
**Persona:** Cliente  
**História:** Quando uma vaga de visita for liberada por desistência de outro usuário, eu quero receber uma notificação automática para que eu possa realizar o agendamento imediatamente antes que a vaga seja preenchida por outra pessoa.

**Critérios de Aceite:**
1. **Gatilho de Cancelamento:** O sistema deve disparar um Domain Event de "VisitVacated" sempre que um registro em ScheduledVisit for excluído ou cancelado.
2. **Prioridade na Fila:** A notificação deve ser enviada prioritariamente para os clientes que entraram primeiro na WaitlistEntry (ordem cronológica de criação).
3. **Link Direto:** A notificação interna deve conter um link funcional que leve o usuário diretamente ao formulário de reserva com o ID da visita já pré-preenchido.

---

### 3. Enabler Story - Contexto: IAM / Security (Técnica)
**Persona:** Cliente (Beneficiário da segurança)  
**História:** Implementar a expiração automática de tokens de sessão e validação via Interceptor para garantir que sessões inativas sejam encerradas, protegendo as informações sensíveis do perfil do cliente.

**Critérios de Aceite:**
1. **Atributo de Expiração:** O TokenService deve ser atualizado para incluir um timestamp de expiração de exatamente 2 horas em cada token gerado.
2. **Bloqueio de Acesso:** O AuthInterceptor deve validar o timestamp e a integridade do token em todas as rotas protegidas, redirecionando para `/login?error=expired` caso expire.
3. **Mensagem Informativa:** A página de Login deve detectar o parâmetro de erro e exibir uma mensagem clara informando que a sessão expirou por inatividade.

---

## B. BDD (Gherkin)

**Funcionalidade:** Agendamento de Visitas  
**Contexto:** O cliente está autenticado e na página de agendamento.

**Cenário 1: Agendamento realizado com sucesso (Caminho Feliz)**
- **Dado** que a visita "Tour Arena" para 20/06/2026 às 10:00 possui 5 vagas disponíveis
- **E** que eu ainda não possuo agendamento para este horário
- **Quando** eu solicito o agendamento para 2 pessoas
- **Então** o sistema deve salvar meu agendamento com status "CONFIRMADO"
- **E** eu devo ser redirecionado para a página do Ticket.

**Cenário 2: Falha por falta de capacidade (Exceção)**
- **Dado** que a visita "Tour Arena" para 20/06/2026 às 10:00 possui apenas 1 vaga disponível
- **Quando** eu tento agendar para 3 pessoas
- **Então** o sistema deve exibir a mensagem "Desculpe, a capacidade máxima para este horário foi atingida"
- **E** nenhum agendamento deve ser criado no banco de dados.

---

## C. TDD (Evidência do Ciclo Red-Green-Refactor)

O desenvolvimento da lógica de validação de capacidade na classe `SchedulingService` seguiu o ciclo TDD:

1. **Fase RED:** Criação do teste unitário `shouldFailWhenCapacityExceeded` na classe `SchedulingServiceTest`. O teste falhou inicialmente pois a lógica de validação ainda não existia ou o método `scheduleVisit` estava vazio.
2. **Fase GREEN:** Implementação do método `validateCapacity` no `SchedulingService`. Ao rodar os testes novamente com `mvn test`, o teste passou com sucesso.
3. **Fase REFACTOR:** Refatoração do código para extrair a lógica de contagem de pessoas para o repositório (`countBookedPeopleByVisit`) e centralizar as mensagens de erro, mantendo a legibilidade e seguindo padrões de Clean Code.

*Nota: Teste realizado no domínio, isolado de banco de dados utilizando Mockito para simular o comportamento do repositório.*

---

## D. Proposta de Esteira CI/CD

**Ferramenta Sugerida:** GitHub Actions

1. **Trigger:** Push ou Pull Request para a branch `main`.
2. **Build & Test:**
   - Setup do ambiente Java 17.
   - Execução de testes unitários e de integração (`mvn test`).
   - Verificação de estilo de código (Checkstyle/Linter).
3. **Security Scan:** Execução de ferramentas como Snyk ou OWASP Dependency-Check.
4. **Dockerization:** Build da imagem Docker e push para o GitHub Container Registry (GHCR).
5. **Deployment:** Atualização do serviço no provedor Cloud (Ex: Render ou AWS ECS) via webhook ou CLI.

---

## E. Arquitetura Cloud (Planejamento)

### Diagrama de Componentes (Visão Geral)
1. **Borda (Cloudflare/CDN):** Proteção DDoS, WAF e cache de assets estáticos.
2. **Gateway (Spring Cloud Gateway/Nginx):** Ponto de entrada único e roteamento.
3. **Serviços (ECS/K8s):** Bounded Contexts (Scheduling, Waitlist, IAM) rodando como microserviços.
4. **Fila (RabbitMQ/SQS):** Comunicação assíncrona para Domain Events (Ex: VisitVacated).
5. **Cache (Redis):** Sessões e dados de alta frequência (capacidade de visitas).
6. **Banco (PostgreSQL):** Persistência relacional de alta consistência.
7. **Observabilidade (Prometheus/Grafana/ELK):** Métricas e Logs.

### Regimes de Carga
| Regime | Usuários Simultâneos | Requisições/Seg (RPS) | Contexto |
|---|---|---|---|
| **Baixo** | < 100 | ~10 | Dias comuns sem eventos. |
| **Médio** | 1.000 - 5.000 | ~500 | Abertura de agenda para visitas em feriados. |
| **Alto** | 50.000+ | 5.000+ | Grandes eventos (ex: Final de Campeonato) ou abertura de vendas de ingressos. |

### Justificativa de Elasticidade
- **Elástico/Stateful:** O Banco de Dados e o Redis são stateful e requerem escalonamento vertical ou réplicas de leitura para lidar com carga alta, mantendo a integridade.
- **Serverless (Functions):** O serviço de **Notificações** e a geração de **Relatórios do Dashboard** podem ser convertidos para Serverless (Lambda/Cloud Functions). Isso reduz custos, pois só rodam quando há eventos na fila ou demanda de exportação de dados, escalando automaticamente para zero em períodos de inatividade.
- **Microserviços (Fargate/ECS):** Os serviços de agendamento e IAM devem permanecer em containers elásticos para garantir baixa latência e controle refinado sobre as regras de negócio complexas.
