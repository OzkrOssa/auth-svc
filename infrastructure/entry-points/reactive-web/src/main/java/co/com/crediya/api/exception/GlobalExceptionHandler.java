package co.com.crediya.api.exception;

import co.com.crediya.api.dto.ResponseDto;
import co.com.crediya.usecase.getuser.exception.UserNotFoundException;
import co.com.crediya.usecase.getusers.exception.UsersNotFoundException;
import co.com.crediya.usecase.registeruser.exception.EmailAlreadyExistsException;
import co.com.crediya.usecase.registeruser.exception.InvalidBaseSalaryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler{

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configurer.getWriters());
        this.setMessageReaders(configurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        HttpStatus status;
        String errorMessage = error.getMessage();

        status = switch (error) {
            case UserNotFoundException ignored -> HttpStatus.NOT_FOUND;
            case UsersNotFoundException ignored -> HttpStatus.NOT_FOUND;
            case EmailAlreadyExistsException ignored -> HttpStatus.CONFLICT;
            case InvalidBaseSalaryException ignored -> HttpStatus.BAD_REQUEST;
            case IllegalArgumentException ignored -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .success(false)
                .message(status.getReasonPhrase())
                .errors(List.of(errorMessage))
                .build();

        log.error("Error occurred while processing request [{} {}]: {}",
                request.method(), request.path(), errorMessage, error);
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
