    package pet.store.controller.error;
    import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

    @RestControllerAdvice
    @Slf4j
    public class GlobalErrorHandler {
	private enum LogStatus{
	STACK_TRACE, MESSAGE_ONLY
	}
	@Data
	private class ExceptionMessage{
	private String message;
	private String statusReason;
	private long statusCode;
	private String timestamp;
	private String uri;
	}
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)

	public Map<String,String> handlerNoSuchElementException(
	NoSuchElementException ex, WebRequest webRequest) {
	return buildExceptionMessage(ex, HttpStatus.NOT_FOUND,webRequest,
	LogStatus.MESSAGE_ONLY);	
	}
	
	private Map<String, String> buildExceptionMessage(NoSuchElementException ex, HttpStatus status,
	WebRequest webRequest, LogStatus logstatus) {
	
	if(logstatus == LogStatus.MESSAGE_ONLY) {
	log.error("Exception: {}", ex.toString());
	}
	else {
		log.error("Exception:", ex);
	}
	return Map.of("message", ex.toString());
	
	}

}
