package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.model.Fruit;

import java.util.List;

public interface FruitService {
    Fruit createFruit(Fruit fruit);
    List<Fruit> list();
    Fruit getFruitById(int id);
    Fruit updateFruit(int id, Fruit fruit);
    void deleteFruit(int id);
}
