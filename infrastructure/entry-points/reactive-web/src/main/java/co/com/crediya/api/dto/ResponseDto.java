package co.com.crediya.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
}