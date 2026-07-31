package com.motosport.customer.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.motosport.customer.dto.CustomerDto;
import com.motosport.customer.dto.ResponseDto;
import com.motosport.customer.model.Customer;
import com.motosport.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    private CustomerServiceImpl customerService;

    private Customer buildCustomer() {
        return new Customer(1L, "12345678-9", "Juan", "Perez", 987654321,
                "juan@correo.com", "LIC12345",
                LocalDate.now().plusYears(2), LocalDate.now());
    }

    private CustomerDto buildDto() {
        return new CustomerDto(1L, "12345678-9", "Juan", "Perez", 987654321,
                "juan@correo.com", "LIC12345",
                LocalDate.now().plusYears(2), LocalDate.now());
    }

    @Test
    void addCustomer_shouldSaveAndReturnDto() {
        customerService = new CustomerServiceImpl(repository);
        when(repository.save(any(Customer.class))).thenReturn(buildCustomer());

        CustomerDto result = customerService.addCustomer(buildDto());

        assertNotNull(result);
        assertEquals("Juan", result.nombre());
        verify(repository).save(any(Customer.class));
    }

    @Test
    void getCustomer_shouldReturnDtoWhenExists() {
        customerService = new CustomerServiceImpl(repository);
        when(repository.findById(1L)).thenReturn(Optional.of(buildCustomer()));

        CustomerDto result = customerService.getCustomer(1L);

        assertNotNull(result);
        assertEquals("Perez", result.apellidos());
    }

    @Test
    void getCustomer_shouldThrowWhenNotFound() {
        customerService = new CustomerServiceImpl(repository);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.getCustomer(99L));
    }

    @Test
    void getAllCustomer_shouldReturnListOfDtos() {
        customerService = new CustomerServiceImpl(repository);
        when(repository.findAll()).thenReturn(List.of(buildCustomer()));

        List<CustomerDto> result = customerService.getAllCustomer();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("12345678-9", result.get(0).rut());
    }

    @Test
    void updateCustomer_shouldModifyAndReturnDto() {
        customerService = new CustomerServiceImpl(repository);
        Customer existing = buildCustomer();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        CustomerDto updatedDto = new CustomerDto(1L, "98765432-1", "Maria", "Gonzalez",
                912345678, "maria@correo.com", "LIC99999",
                LocalDate.now().plusYears(3), LocalDate.now());

        CustomerDto result = customerService.updateCustomer(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Maria", result.nombre());
        assertEquals("Gonzalez", result.apellidos());
        verify(repository).save(existing);
    }

    @Test
    void updateCustomer_shouldThrowWhenNotFound() {
        customerService = new CustomerServiceImpl(repository);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.updateCustomer(99L, buildDto()));
    }

    @Test
    void deleteCustomer_shouldReturnSuccessMessageWhenExists() {
        customerService = new CustomerServiceImpl(repository);
        when(repository.existsById(1L)).thenReturn(true);

        ResponseDto result = customerService.deleteCustomer(1L);

        assertEquals("Customer eliminado correctamente.", result.message());
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteCustomer_shouldReturnNotFoundMessageWhenMissing() {
        customerService = new CustomerServiceImpl(repository);
        when(repository.existsById(99L)).thenReturn(false);

        ResponseDto result = customerService.deleteCustomer(99L);

        assertEquals("Customer no encontrado.", result.message());
    }
}
