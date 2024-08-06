package by.bsuir.lookmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListResponseDto<T> {
    private List<T> items;
    private Long totalItems;
}
