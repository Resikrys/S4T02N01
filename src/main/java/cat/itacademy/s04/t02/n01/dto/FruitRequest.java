package cat.itacademy.s04.t02.n01.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FruitRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantityKilos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantityKilos() {
        return quantityKilos;
    }

    public void setQuantityKilos(Integer quantityKilos) {
        this.quantityKilos = quantityKilos;
    }
}
