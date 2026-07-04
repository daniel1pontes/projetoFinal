
## Mapeamento de Conceitos: Aula vs. Projeto Final

Cada conceito trabalhado na API de Clínica Veterinária de aula deve possuir o seu equivalente no projeto final:


1. Relacionamento OneToOne (Usuario <-> Endereco): Relação de perfil ou endereço associada a um usuário (@OneToOne).

2. Relacionamento OneToMany / ManyToOne (Usuario <-> Pet): Relação de posse ou vinculação entre entidades (@ManyToOne / @OneToMany).

3. Entidade de Transação (Consulta <-> Pet + Veterinario): Uma entidade central que represente a ação principal (ex: Reserva, CompraIngresso, EntregaAssinatura).

4. Validação de Regra de Negócio (validaDuplicidade de Consultas): Regra lógica impedindo agendamentos no mesmo horário ou transações duplicadas inválidas.

5. Métricas In-Memory (obterResumo de Consultas): Endpoint de resumo/dashboard que calcula estatísticas em memória usando loops ou streams do Java.

6. Segurança JWT (SecurityConfig, TokenService): Autenticação stateless com JWT, login, cadastro público e proteção das demais rotas.

7. Documentação Interativa (Swagger / OpenAPI): Rotas e parâmetros de entrada/saída documentados com anotações OpenAPI.


---

## Requisitos Técnicos Obrigatórios (Back-End)

### 1. Estrutura e Arquitetura

- Tecnologias: Java 21+, Gradle, Spring Boot 3.x/4.x.

- Banco de Dados: Configuração de banco de dados relacional físico (MySQL ou PostgreSQL). É obrigatória a configuração do driver correspondente e das credenciais de acesso no arquivo application.properties ou application.yml.

- Organização de Código: Divisão clara entre Controllers, Services, Repositories, Models (Entities e DTOs) e Exceptions.

- Padrão DTO: Separação de DTOs de entrada (FormDTO com validações) e DTOs de saída.

- Lombok: Utilização de anotações para redução de boilerplate.

### 2. Validação e Exceções

- Validação dos dados de entrada utilizando Jakarta Validation (@NotBlank, @Email, @NotNull, @Size, etc.).

- Tratamento global de erros utilizando @RestControllerAdvice retornando mensagens de erro padronizadas em JSON para:

- 400 Bad Request em caso de campos inválidos nas validações ou quando regras de negócio forem violadas.

- 404 Not Found quando um registro não for localizado pelo ID.

### 3. Segurança

- Rotas públicas restritas ao cadastro de usuário (POST /usuarios), login (POST /auth/login) e documentação Swagger.

- Demais rotas da API protegidas exigindo o cabeçalho Authorization: Bearer <TOKEN>.


---

### Tema 1: Gestão de Coworking (Reserva de Espaços)

Descrição: Sistema de filiais de escritórios compartilhados, permitindo que clientes cadastrados reservem salas de reunião ou mesas de trabalho por períodos específicos.


#### Entidades Principais

1. Usuario: Dados do cliente ou administrador.

2. Endereco: Endereço do usuário (vinculado via @OneToOne).

3. Filial: Representa uma unidade física do coworking.

4. Espaco: Salas de reunião ou mesas de trabalho de uma Filial (@ManyToOne).

5. Reserva: Transação (@ManyToOne com Usuario e @ManyToOne com Espaco) contendo data, hora de início e de término.




#### Regra de Negócio Crítica

- Um espaço não pode ser reservado se houver sobreposição de horários (data, início e fim) para o mesmo espaço. Rejeitar o agendamento conflitante com status 400 Bad Request.




#### Endpoint de Resumo estatístico (GET /reservas/resumo)

Calcular em memória e retornar:

- Total de reservas realizadas.

- Quantidade de reservas por tipo de espaço.

- Duração média das reservas (em horas).

- Receita total estimada (se cada espaço tiver um valor por hora).

---
