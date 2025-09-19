package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FruitServiceImplTest {

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private FruitServiceImpl fruitService;

    @Test
    public void testCreateFruit_shouldSaveAndReturnFruit() {

        Fruit fruit = new Fruit("Banana", 15);

        when(fruitRepository.save(fruit)).thenReturn(fruit);

        Fruit createdFruit = fruitService.createFruit(fruit);
        assertNotNull(createdFruit);
        assertEquals("Banana", createdFruit.getName());
        verify(fruitRepository, times(1)).save(fruit);
    }

    @Test
    public void testList_shouldReturnAllFruits() {

        Fruit fruit1 = new Fruit("Banana", 15);
        Fruit fruit2 = new Fruit("Watermelon", 47);
        List<Fruit> fruits = List.of(fruit1, fruit2);

        when(fruitRepository.findAll()).thenReturn(fruits);

        List<Fruit> foundFruits = fruitService.list();
        assertFalse(foundFruits.isEmpty());
        assertEquals(2, foundFruits.size());
        verify(fruitRepository, times(1)).findAll();
    }

    @Test
    public void testGetFruitById_shouldReturnFruitIfExists() {

        int id = 1;
        Fruit fruit = new Fruit("Banana", 15);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(fruit));

        Fruit foundFruit = fruitService.getFruitById(id);
        assertNotNull(foundFruit);
        assertEquals(fruit.getName(), foundFruit.getName());
        verify(fruitRepository, times(1)).findById(id);
    }

    @Test
    public void testGetFruitById_shouldReturnNullIfNotFound() {

        int id = 99;

        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        Fruit foundFruit = fruitService.getFruitById(id);
        assertNull(foundFruit);
        verify(fruitRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateFruit_shouldReturnUpdatedFruit_whenFruitExists() {

        int id = 1;
        Fruit existingFruit = new Fruit("Banana", 10);
        Fruit updatedFruitData = new Fruit("Mango", 20);

        when(fruitRepository.findById(id)).thenReturn(Optional.of(existingFruit));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruitData);

        Fruit result = fruitService.updateFruit(id, updatedFruitData);
        assertNotNull(result);
        assertEquals("Mango", result.getName());
        assertEquals(20, result.getQuantityKilos());
        verify(fruitRepository, times(1)).findById(id);
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    public void testUpdateFruit_shouldReturnNull_whenFruitDoesNotExist() {

        int id = 99;
        Fruit updatedFruitData = new Fruit("Watermelon", 50);

        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        Fruit result = fruitService.updateFruit(id, updatedFruitData);
        assertNull(result);
        verify(fruitRepository, times(1)).findById(id);
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    public void testDeleteFruit_shouldReturnTrue_whenFruitExists() {

        int id = 1;

        fruitService.deleteFruit(id);

        verify(fruitRepository, times(1)).deleteById(id);
    }
}