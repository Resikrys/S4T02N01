package cat.itacademy.s04.t02.n01.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FruitRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantityKilos;
}
