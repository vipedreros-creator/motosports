package com.motosport.rent.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.motosport.rent.client.BikeClient;
import com.motosport.rent.client.CustomerClient;
import com.motosport.rent.dto.BikeDto;
import com.motosport.rent.dto.CustomerDto;
import com.motosport.rent.dto.RentDto;
import com.motosport.rent.dto.ResponseDto;
import com.motosport.rent.exception.BikeNoDisponibleException;
import com.motosport.rent.model.Rent;
import com.motosport.rent.repository.RentRepository;

@ExtendWith(MockitoExtension.class)
public class RentServiceImplTest {

    @Mock
    private RentRepository repository;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private BikeClient bikeClient;

    private RentServiceImpl rentService;

    private BikeDto buildBikeDto(boolean disponible) {
        return new BikeDto(1L, "Kawasaki", "Ninja 400", "ABC123", 4500000, 2022, "Verde", 12000, disponible);
    }

    private CustomerDto buildCustomerDto() {
        return new CustomerDto(1L, "12345678-9", "Juan", "Perez", 987654321,
                "juan@correo.com", "LIC12345", LocalDate.now().plusYears(2), LocalDate.now());
    }

    private Rent buildRent() {
        return new Rent(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(5), "Sin observaciones");
    }

    private RentDto buildDto() {
        return new RentDto(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(5), "Sin observaciones");
    }

    @Test
    void addRent_shouldSaveWhenBikeDisponible() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(customerClient.getCustomerById(1L)).thenReturn(buildCustomerDto());
        when(bikeClient.getBikeById(1L)).thenReturn(buildBikeDto(true));
        when(repository.save(any(Rent.class))).thenReturn(buildRent());

        RentDto result = rentService.addRent(buildDto());

        assertNotNull(result);
        assertEquals(1L, result.bikeId());
        verify(bikeClient).updateBike(eq(1L), any(BikeDto.class));
        verify(repository).save(any(Rent.class));
    }

    @Test
    void addRent_shouldThrowWhenBikeNoDisponible() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(customerClient.getCustomerById(1L)).thenReturn(buildCustomerDto());
        when(bikeClient.getBikeById(1L)).thenReturn(buildBikeDto(false));

        assertThrows(BikeNoDisponibleException.class, () -> rentService.addRent(buildDto()));
    }

    @Test
    void addRent_shouldThrowWhenFechaFinAntesDeInicio() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(customerClient.getCustomerById(1L)).thenReturn(buildCustomerDto());
        when(bikeClient.getBikeById(1L)).thenReturn(buildBikeDto(true));
        RentDto dtoInvalido = new RentDto(null, 1L, 1L, LocalDate.now(), LocalDate.now().minusDays(1), "obs");

        assertThrows(RuntimeException.class, () -> rentService.addRent(dtoInvalido));
    }

    @Test
    void getRent_shouldReturnDtoWhenExists() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(repository.findById(1L)).thenReturn(Optional.of(buildRent()));

        RentDto result = rentService.getRent(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getRent_shouldThrowWhenNotFound() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rentService.getRent(99L));
    }

    @Test
    void getAllRents_shouldReturnListOfDtos() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(repository.findAll()).thenReturn(List.of(buildRent()));

        List<RentDto> result = rentService.getAllRents();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateRent_shouldModifyAndReturnDto() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        Rent existing = buildRent();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        RentDto updatedDto = new RentDto(1L, 2L, 2L, LocalDate.now(), LocalDate.now().plusDays(10), "Nueva obs");

        RentDto result = rentService.updateRent(1L, updatedDto);

        assertNotNull(result);
        assertEquals(2L, result.bikeId());
        verify(repository).save(existing);
    }

    @Test
    void updateRent_shouldThrowWhenNotFound() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rentService.updateRent(99L, buildDto()));
    }

    @Test
    void deleteRent_shouldFreeUpBikeAndReturnMessage() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(repository.findById(1L)).thenReturn(Optional.of(buildRent()));
        when(bikeClient.getBikeById(1L)).thenReturn(buildBikeDto(false));

        ResponseDto result = rentService.deleteRent(1L);

        assertEquals("Rent eliminado correctamente", result.message());
        verify(bikeClient).updateBike(eq(1L), any(BikeDto.class));
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteRent_shouldThrowWhenNotFound() {
        rentService = new RentServiceImpl(repository, customerClient, bikeClient);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rentService.deleteRent(99L));
    }
}