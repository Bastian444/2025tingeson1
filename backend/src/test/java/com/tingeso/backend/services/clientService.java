package com.tingeso.backend.services;

import com.tingeso.backend.entities.clientEntity;
import com.tingeso.backend.repositories.clientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class clientServiceTest {

    @Mock
    private clientRepository clientRepo;

    @InjectMocks
    private clientService clientService;

    private clientEntity createSampleClient() {
        clientEntity client = new clientEntity();
        client.setId(1L);
        client.setClientname("Juan");
        client.setClientlastname("Pérez");
        client.setClientemail("juan.perez@example.com");
        client.setClientrut("12345678-9");
        client.setClientpassword("securepassword123");
        client.setVisits(5);
        client.setBirthdate("1990-05-15");
        return client;
    }

    @Test
    void getAllClients_ShouldReturnAllClients() {
        // Arrange
        clientEntity client1 = createSampleClient();
        clientEntity client2 = createSampleClient();
        client2.setId(2L);
        client2.setClientname("María");

        List<clientEntity> expectedClients = Arrays.asList(client1, client2);
        when(clientRepo.findAll()).thenReturn(expectedClients);

        // Act
        List<clientEntity> actualClients = clientService.getAllClients();

        // Assert
        assertEquals(2, actualClients.size());
        assertEquals("Juan", actualClients.get(0).getClientname());
        assertEquals("María", actualClients.get(1).getClientname());
        verify(clientRepo, times(1)).findAll();
    }

    @Test
    void saveClient_ShouldSaveClientWithAllAttributes() {
        // Arrange
        clientEntity client = createSampleClient();
        when(clientRepo.save(client)).thenReturn(client);

        // Act
        clientEntity result = clientService.saveClient(client);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getClientname());
        assertEquals("Pérez", result.getClientlastname());
        assertEquals("juan.perez@example.com", result.getClientemail());
        assertEquals("12345678-9", result.getClientrut());
        assertEquals(5, result.getVisits());
        assertEquals("1990-05-15", result.getBirthdate());
        verify(clientRepo, times(1)).save(client);
    }

    @Test
    void getClientById_ShouldReturnClientWhenExists() {
        // Arrange
        Long id = 1L;
        clientEntity expectedClient = createSampleClient();
        when(clientRepo.findById(id)).thenReturn(Optional.of(expectedClient));

        // Act
        clientEntity actualClient = clientService.getClientById(id);

        // Assert
        assertNotNull(actualClient);
        assertEquals(id, actualClient.getId());
        assertEquals("Juan", actualClient.getClientname());
        verify(clientRepo, times(1)).findById(id);
    }

    @Test
    void getClientById_ShouldReturnNullWhenNotExists() {
        // Arrange
        Long id = 1L;
        when(clientRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        clientEntity actualClient = clientService.getClientById(id);

        // Assert
        assertNull(actualClient);
        verify(clientRepo, times(1)).findById(id);
    }

    @Test
    void getClientByClientrut_ShouldReturnClient() {
        // Arrange
        String rut = "12345678-9";
        clientEntity expectedClient = createSampleClient();
        when(clientRepo.findByClientrut(rut)).thenReturn(expectedClient);

        // Act
        clientEntity actualClient = clientService.getClientByClientrut(rut);

        // Assert
        assertNotNull(actualClient);
        assertEquals(rut, actualClient.getClientrut());
        assertEquals("Juan", actualClient.getClientname());
        verify(clientRepo, times(1)).findByClientrut(rut);
    }

    @Test
    void updateClient_ShouldUpdateAllFields() {
        // Arrange
        clientEntity originalClient = createSampleClient();
        clientEntity updatedClient = createSampleClient();
        updatedClient.setClientname("Juan Carlos");
        updatedClient.setVisits(10);
        updatedClient.setClientemail("nuevo.email@example.com");

        when(clientRepo.save(updatedClient)).thenReturn(updatedClient);

        // Act
        clientEntity result = clientService.updateClient(updatedClient);

        // Assert
        assertEquals("Juan Carlos", result.getClientname());
        assertEquals(10, result.getVisits());
        assertEquals("nuevo.email@example.com", result.getClientemail());
        verify(clientRepo, times(1)).save(updatedClient);
    }

    @Test
    void deleteClientById_ShouldReturnTrueWhenDeleted() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(clientRepo).deleteById(id);

        // Act
        boolean result = clientService.deleteClientById(id);

        // Assert
        assertTrue(result);
        verify(clientRepo, times(1)).deleteById(id);
    }

    @Test
    void deleteClientById_ShouldThrowExceptionWhenErrorOccurs() {
        // Arrange
        Long id = 1L;
        doThrow(new RuntimeException("Database error")).when(clientRepo).deleteById(id);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            clientService.deleteClientById(id);
        });

        assertEquals("Database error", exception.getMessage());
        verify(clientRepo, times(1)).deleteById(id);
    }

    @Test
    void getClientsWithMoreThanXVisits_ShouldReturnFilteredClients() {
        // Arrange
        int minVisits = 3;
        clientEntity client1 = createSampleClient(); // visits = 5
        clientEntity client2 = createSampleClient();
        client2.setId(2L);
        client2.setVisits(2); // No debería aparecer

        List<clientEntity> expectedClients = List.of(client1);
        when(clientRepo.findByVisitsGreaterThan(minVisits)).thenReturn(expectedClients);

        // Act
        List<clientEntity> actualClients = clientService.getClientsWithMoreThanXVisits(minVisits);

        // Assert
        assertEquals(1, actualClients.size());
        assertEquals(5, actualClients.get(0).getVisits());
        verify(clientRepo, times(1)).findByVisitsGreaterThan(minVisits);
    }

    @Test
    void isBirthday_ShouldReturnTrueWhenBirthdayMatches() {
        // Arrange
        String rut = "12345678-9";
        String bookingDate = "2023-05-15T14:30:00"; // Mismo día que el cumpleaños (15) y mes (05)

        when(clientRepo.isClientBirthday(rut, "2023-05-15")).thenReturn(true);

        // Act
        boolean result = clientService.isBirthday(rut, bookingDate);

        // Assert
        assertTrue(result);
        verify(clientRepo, times(1)).isClientBirthday(rut, "2023-05-15");
    }

    @Test
    void isBirthday_ShouldReturnFalseWhenNotBirthday() {
        // Arrange
        String rut = "12345678-9";
        String bookingDate = "2023-06-20T14:30:00"; // Diferente al cumpleaños

        when(clientRepo.isClientBirthday(rut, "2023-06-20")).thenReturn(false);

        // Act
        boolean result = clientService.isBirthday(rut, bookingDate);

        // Assert
        assertFalse(result);
        verify(clientRepo, times(1)).isClientBirthday(rut, "2023-06-20");
    }
}