package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Fruit fruit = new Fruit(1, "Banana", 15); //id, string name, int kilos

        // WHEN: El repositorio mockeado guarda la fruta
        when(fruitRepository.save(fruit)).thenReturn(fruit);

        // THEN: La fruta es creada exitosamente
        Fruit createdFruit = fruitService.createFruit(fruit);
        assertNotNull(createdFruit);
        assertEquals("Banana", createdFruit.getName());
        verify(fruitRepository, times(1)).save(fruit);
    }

    @Test
    void list() {
    }

    @Test
    void getFruitById() {
    }

    @Test
    void updateFruit() {
    }

    @Test
    void deleteFruit() {
    }
}