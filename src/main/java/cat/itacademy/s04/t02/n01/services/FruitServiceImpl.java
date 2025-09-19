package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FruitServiceImpl implements FruitService {
    @Autowired
    private FruitRepository fruitRepository;

    @Override
    public Fruit createFruit(Fruit fruit) {
        return fruitRepository.save(fruit);
    }

    @Override
    public List<Fruit> list() {
        return fruitRepository.findAll();
    }

    @Override
    public Fruit getFruitById(int id) {
        Optional<Fruit> fruit = fruitRepository.findById(id);
        return fruit.orElse(null);
    }

    @Override
    public Fruit updateFruit(int id, Fruit fruit) {
        return fruitRepository.findById(id).map(existingFruit -> {
            existingFruit.setName(fruit.getName());
            existingFruit.setQuantityKilos(fruit.getQuantityKilos());
            return fruitRepository.save(existingFruit);
        }).orElse(null);
    }

    @Override
    public void deleteFruit(int id) {
        fruitRepository.deleteById(id);
    }
}
