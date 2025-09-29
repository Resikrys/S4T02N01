package cat.itacademy.s04.t02.n01.dto;

public class FruitResponse {

    private Integer id;
    private String name;
    private Integer quantityKilos;

    public FruitResponse() {}

    public FruitResponse(Integer id, String name, Integer quantityKilos) {
        this.id = id;
        this.name = name;
        this.quantityKilos = quantityKilos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
