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
        // GIVEN: Un objeto Fruit
        Fruit fruit = new Fruit("Banana", 15); //id, string name, int kilos

        // WHEN: El repositorio mockeado guarda la fruta
        when(fruitRepository.save(fruit)).thenReturn(fruit);

        // THEN: La fruta es creada exitosamente
        Fruit createdFruit = fruitService.createFruit(fruit);
        assertNotNull(createdFruit);
        assertEquals("Banana", createdFruit.getName());
        verify(fruitRepository, times(1)).save(fruit);
    }

    @Test
    public void testList_shouldReturnAllFruits() {
        // GIVEN: Una lista de frutas
        Fruit fruit1 = new Fruit("Banana", 15);
        Fruit fruit2 = new Fruit("Watermelon", 47);
        List<Fruit> fruits = List.of(fruit1, fruit2);

        // WHEN: El repositorio mockeado devuelve la lista
        when(fruitRepository.findAll()).thenReturn(fruits);

        // THEN: El servicio devuelve la lista completa
        List<Fruit> foundFruits = fruitService.list();
        assertFalse(foundFruits.isEmpty());
        assertEquals(2, foundFruits.size());
        verify(fruitRepository, times(1)).findAll();
    }

    @Test
    public void testGetFruitById_shouldReturnFruitIfExists() {
        // GIVEN: Un ID y un objeto Fruit
        int id = 1;
        Fruit fruit = new Fruit("Banana", 15);

        // WHEN: El repositorio mockeado encuentra la fruta
        when(fruitRepository.findById(id)).thenReturn(Optional.of(fruit));

        // THEN: El servicio devuelve la fruta
        Fruit foundFruit = fruitService.getFruitById(id);
        assertNotNull(foundFruit);
        assertEquals(fruit.getName(), foundFruit.getName());
        verify(fruitRepository, times(1)).findById(id);
    }

    @Test
    public void testGetFruitById_shouldReturnNullIfNotFound() {
        // GIVEN: Un ID que no existe
        int id = 99;

        // WHEN: El repositorio mockeado no encuentra la fruta
        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        // THEN: El servicio devuelve null
        Fruit foundFruit = fruitService.getFruitById(id);
        assertNull(foundFruit);
        verify(fruitRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateFruit_shouldReturnUpdatedFruit_whenFruitExists() {
        // GIVEN: Un ID y una fruta para actualizar
        int id = 1;
        Fruit existingFruit = new Fruit("Banana", 10);
        Fruit updatedFruitData = new Fruit("Mango", 20);

        // WHEN: El repositorio mockeado encuentra la fruta y la guarda
        when(fruitRepository.findById(id)).thenReturn(Optional.of(existingFruit));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruitData);

        // THEN: El servicio devuelve la fruta actualizada
        Fruit result = fruitService.updateFruit(id, updatedFruitData);
        assertNotNull(result);
        assertEquals("Mango", result.getName());
        assertEquals(20, result.getQuantityKilos());
        verify(fruitRepository, times(1)).findById(id);
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    public void testUpdateFruit_shouldReturnNull_whenFruitDoesNotExist() {
        // GIVEN: Un ID que no existe
        int id = 99;
        Fruit updatedFruitData = new Fruit("Watermelon", 50);

        // WHEN: El repositorio mockeado no encuentra la fruta
        when(fruitRepository.findById(id)).thenReturn(Optional.empty());

        // THEN: El servicio devuelve null
        Fruit result = fruitService.updateFruit(id, updatedFruitData);
        assertNull(result);
        verify(fruitRepository, times(1)).findById(id);
        verify(fruitRepository, never()).save(any(Fruit.class));
    }

    @Test
    public void testDeleteFruit_shouldReturnTrue_whenFruitExists() {
        // GIVEN: Un ID de una fruta existente
        int id = 1;

        // WHEN: El servicio elimina la fruta
        // No es necesario mockear el retorno de deleteById, solo el comportamiento
        fruitService.deleteFruit(id);

        // THEN: Se verifica que el método de eliminación se ha llamado una vez
        verify(fruitRepository, times(1)).deleteById(id);
    }
}