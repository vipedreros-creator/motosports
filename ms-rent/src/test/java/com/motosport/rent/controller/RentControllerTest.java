package com.motosport.rent.controller;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import com.motosport.rent.dto.RentDto;
import com.motosport.rent.dto.ResponseDto;
import com.motosport.rent.service.RentService;

@ExtendWith(MockitoExtension.class)
public class RentControllerTest {

    @Mock
    private RentService rentService;

    private RentDto buildDto(Long id) {
        return new RentDto(id, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(5), "Sin observaciones");
    }

    @Test
    void getAllRents_shouldReturnListOfRents() {
        RentController controller = new RentController(rentService);
        when(rentService.getAllRents()).thenReturn(List.of(buildDto(1L)));

        ResponseEntity<List<RentDto>> result = controller.getAllRents();

        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(1L, result.getBody().get(0).id());
    }

    @Test
    void getRentById_shouldReturnRentDto() {
        RentController controller = new RentController(rentService);
        when(rentService.getRent(2L)).thenReturn(buildDto(2L));

        ResponseEntity<RentDto> result = controller.getRentById(2L);

        assertNotNull(result.getBody());
        assertEquals(2L, result.getBody().id());
    }

    @Test
    void createRent_shouldReturnCreatedRent() {
        RentController controller = new RentController(rentService);
        RentDto creado = buildDto(3L);
        when(rentService.addRent(any(RentDto.class))).thenReturn(creado);

        ResponseEntity<RentDto> result = controller.createRent(buildDto(null));

        assertNotNull(result.getBody());
        assertEquals(3L, result.getBody().id());
    }

    @Test
    void updateRent_shouldReturnUpdatedRent() {
        RentController controller = new RentController(rentService);
        RentDto actualizado = buildDto(4L);
        when(rentService.updateRent(eq(4L), any(RentDto.class))).thenReturn(actualizado);

        ResponseEntity<RentDto> result = controller.updateRent(4L, actualizado);

        assertNotNull(result.getBody());
        assertEquals(4L, result.getBody().id());
    }

    @Test
    void deleteRent_shouldReturnResponseDto() {
        RentController controller = new RentController(rentService);
        ResponseDto response = new ResponseDto("Arriendo eliminado correctamente");
        when(rentService.deleteRent(5L)).thenReturn(response);

        ResponseEntity<ResponseDto> result = controller.deleteRent(5L);

        assertNotNull(result.getBody());
        assertEquals("Arriendo eliminado correctamente", result.getBody().message());
        verify(rentService).deleteRent(5L);
    }
}