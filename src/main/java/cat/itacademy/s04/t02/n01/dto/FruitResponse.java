package cat.itacademy.s04.t02.n01.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FruitResponse {

    private Long id;
    private String name;
    private int quantityKilos;

}
