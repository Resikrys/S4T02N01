package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitRequest;
import cat.itacademy.s04.t02.n01.model.Fruit;

import java.util.List;
import java.util.Optional;

public interface FruitService {
    Fruit createFruit(FruitRequest fruitRequest);
    Optional<Fruit> getFruitById(Integer id);
    List<Fruit> list();
    Optional<Fruit> updateFruit(Integer id, FruitRequest fruitRequest);
    void deleteFruit(Integer id);
}
