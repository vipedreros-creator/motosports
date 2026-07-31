package com.motosport.customer.controller;

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

import com.motosport.customer.dto.CustomerDto;
import com.motosport.customer.dto.ResponseDto;
import com.motosport.customer.service.CustomerService;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    private CustomerDto buildDto(Long id, String nombre) {
        return new CustomerDto(id, "12345678-9", nombre, "Perez", 987654321,
                nombre.toLowerCase() + "@correo.com", "LIC12345",
                LocalDate.now().plusYears(2), LocalDate.now());
    }

    @Test
    void getAllCustomer_shouldReturnListOfCustomers() {
        CustomerController controller = new CustomerController(customerService);
        CustomerDto customer = buildDto(1L, "Juan");
        when(customerService.getAllCustomer()).thenReturn(List.of(customer));

        ResponseEntity<List<CustomerDto>> result = controller.getAllCustomer();

        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Juan", result.getBody().get(0).nombre());
    }

    @Test
    void getCustomerById_shouldReturnCustomerDto() {
        CustomerController controller = new CustomerController(customerService);
        CustomerDto customer = buildDto(2L, "Maria");
        when(customerService.getCustomer(2L)).thenReturn(customer);

        ResponseEntity<CustomerDto> result = controller.getCustomerById(2L);

        assertNotNull(result.getBody());
        assertEquals("Maria", result.getBody().nombre());
    }

    @Test
    void createCustomer_shouldReturnCreatedCustomer() {
        CustomerController controller = new CustomerController(customerService);
        CustomerDto nuevo = buildDto(null, "Pedro");
        CustomerDto guardado = buildDto(3L, "Pedro");
        when(customerService.addCustomer(any(CustomerDto.class))).thenReturn(guardado);

        ResponseEntity<CustomerDto> result = controller.createCustomer(nuevo);

        assertNotNull(result.getBody());
        assertEquals(3L, result.getBody().id());
    }

    @Test
    void updateCustomer_shouldReturnUpdatedCustomer() {
        CustomerController controller = new CustomerController(customerService);
        CustomerDto actualizado = buildDto(4L, "Ana");
        when(customerService.updateCustomer(eq(4L), any(CustomerDto.class))).thenReturn(actualizado);

        ResponseEntity<CustomerDto> result = controller.updateCustomer(4L, actualizado);

        assertNotNull(result.getBody());
        assertEquals("Ana", result.getBody().nombre());
    }

    @Test
    void deleteCustomer_shouldReturnResponseDto() {
        CustomerController controller = new CustomerController(customerService);
        ResponseDto response = new ResponseDto("Cliente eliminado correctamente");
        when(customerService.deleteCustomer(5L)).thenReturn(response);

        ResponseEntity<ResponseDto> result = controller.deleteCustomer(5L);

        assertNotNull(result.getBody());
        assertEquals("Cliente eliminado correctamente", result.getBody().message());
        verify(customerService).deleteCustomer(5L);
    }
}