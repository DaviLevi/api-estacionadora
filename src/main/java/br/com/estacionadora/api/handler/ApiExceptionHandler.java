package br.com.estacionadora.api.handler;



import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import br.com.estacionadora.api.contract.error.Erro;
import br.com.estacionadora.domain.exception.EntidadeNaoEncontradaException;
import br.com.estacionadora.domain.exception.VeiculoJaEstacionadoException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private final String MENSAGEM_ERRO_GENERICA = "Houve um erro interno ao contatar "
									+ "nossa API. Por favor, nos consulte mais tarde. Se o problema persistir, "
									+ "contate o administrador.";
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(VeiculoJaEstacionadoException.class)
	protected ResponseEntity<?> handleVeiculoJaEstacionadoException(VeiculoJaEstacionadoException ex, WebRequest request) {
		
		String causa = "Conflito de entidades";
		
		String descricao = ex.getMessage();
		
		Erro erro = Erro.builder().codigo(400).resumo(causa).descricao(descricao).timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).build();
	
		return handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	protected ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {
		
		String causa = "Entidade nao encontrada";
		
		String descricao = ex.getMessage();
		
		Erro erro = Erro.builder().codigo(404).resumo(causa).descricao(descricao).timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).build();
	
		return handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch(
					(MethodArgumentTypeMismatchException) ex, headers, status, request);
		}
	
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String causa = "Parametro invalido";

		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Erro erro = Erro.builder().timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).codigo(status.value()).resumo(causa).descricao(detail).build();
		
		return handleExceptionInternal(ex, erro, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = NestedExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request); 
		}
		
		String causa = "Payload incompreensivel";
		String descricao = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		Erro erro = Erro.builder().timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).codigo(status.value()).resumo(causa).descricao(descricao).build();
		
		return handleExceptionInternal(ex, erro, headers, status, request);
	}
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String path = joinPath(ex.getPath());

		String causa = "Payload incompreensivel";
		String descricao = String.format("A propriedade '%s' não existe. "
				+ "Corrija ou remova essa propriedade e tente novamente.", path);
		Erro erro = Erro.builder().timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).codigo(status.value()).resumo(causa).descricao(descricao).build();
		
		return handleExceptionInternal(ex, erro, headers, status, request);
	}
	
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());
		
		String resumo = "Payload incompreensivel";
		String descricao = String.format("A propriedade '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Erro erro = Erro.builder().timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).codigo(status.value()).resumo(resumo).descricao(descricao).build();
		
		return handleExceptionInternal(ex, erro, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		
		String resumo = String.format("O recurso %s, que você tentou acessar, é inexistente.", 
				ex.getRequestURL());
		
		Erro problem = Erro.builder().timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).codigo(status.value()).resumo(resumo).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if(body == null) {
			body = Erro.builder().codigo(500).timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).resumo(MENSAGEM_ERRO_GENERICA).build();
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private String joinPath(List<Reference> references) {
		return references.stream()
			.map(ref -> ref.getFieldName())
			.collect(Collectors.joining("."));
	}
	
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		
		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

	    return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
			HttpStatus status, WebRequest request, BindingResult bindingResult) {
		String resumo = "Dados invalidos";
	    String descricao = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
	    
	    List<Erro.Problema> problemas = bindingResult.getAllErrors().stream()
	    		.map(objectError -> {
	    			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
	    			
	    			String name = objectError.getObjectName();
	    			
	    			if (objectError instanceof FieldError) {
	    				name = ((FieldError) objectError).getField();
	    			}
	    			
	    			return Erro.Problema.builder()
	    				.propriedade(name)
	    				.descricao(message)
	    				.build();
	    		})
	    		.collect(Collectors.toList());
	    
	    Erro erro = Erro.builder()
	    		.timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
	    		.codigo(status.value())
	    		.resumo(resumo)
	    		.descricao(descricao)
	    		.problemas(problemas)
	    		.build();
	    
	    return handleExceptionInternal(ex, erro, headers, status, request);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
		String causa = "Erro de sistema";
		String detail = MENSAGEM_ERRO_GENERICA;

		logger.error(ex.getMessage(), ex);
		
		Erro erro = Erro.builder().timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).codigo(status.value()).resumo(causa).descricao(detail).build();
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}
	
}
