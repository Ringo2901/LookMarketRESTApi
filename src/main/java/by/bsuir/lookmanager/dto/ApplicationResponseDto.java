package by.bsuir.lookmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseDto<T> {
    private int code;
    private String message;
    private T payload;
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T getPayload() {
        return payload;
    }
}
