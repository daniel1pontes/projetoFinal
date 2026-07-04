package danielpontes.projetofinal.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handle404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handle400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity handleRegraDeNegocio(ValidacaoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Cobre violações de restrições únicas do banco (ex: CNPJ ou e-mail duplicado)
     * que antes resultavam em 500 Internal Server Error.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroPadraoDTO> handleIntegridade(DataIntegrityViolationException ex) {
        logger.warn("Violação de integridade de dados: {}", ex.getMostSpecificCause().getMessage());
        String mensagem = "Não foi possível concluir a operação: registro duplicado ou restrição de integridade violada.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroPadraoDTO(HttpStatus.BAD_REQUEST.value(), mensagem, Instant.now()));
    }

    /**
     * Cobre falhas de autenticação (ex: e-mail/senha inválidos no login),
     * que antes resultavam em 500 Internal Server Error.
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErroPadraoDTO> handleAutenticacao(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErroPadraoDTO(HttpStatus.UNAUTHORIZED.value(), "E-mail ou senha inválidos.", Instant.now()));
    }

    /**
     * Cobre tentativas de ação sobre um recurso que não pertence ao usuário logado
     * (ex: cancelar reserva de outra pessoa) e que não é ADMIN.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroPadraoDTO> handleAcessoNegado(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErroPadraoDTO(HttpStatus.FORBIDDEN.value(), ex.getMessage(), Instant.now()));
    }

    /**
     * Fallback para qualquer exceção não tratada explicitamente, garantindo que
     * o cliente sempre receba um corpo JSON padronizado em vez de um stacktrace.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadraoDTO> handleGenerico(Exception ex) {
        logger.error("Erro não tratado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroPadraoDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor.", Instant.now()));
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    public record ErroPadraoDTO(int status, String mensagem, Instant timestamp) {}
}