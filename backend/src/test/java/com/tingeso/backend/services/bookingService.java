package com.tingeso.backend.services;

import com.tingeso.backend.entities.bookingEntity;
import com.tingeso.backend.repositories.bookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class bookingServiceTest {

    @Mock
    private bookingRepository bookingRepo;

    @InjectMocks
    private bookingService bookingService;

    private bookingEntity createSampleBooking() {
        bookingEntity booking = new bookingEntity();
        booking.setId(1L);
        booking.setDate("2023-12-25");
        booking.setStatus("Confirmado");
        booking.setTime(LocalTime.of(14, 30));
        booking.setType(1);
        booking.setQuantity(5);
        booking.setRutWhoMadeBooking("12345678-9");
        booking.setParticipant1("Juan Pérez");
        booking.setParticipant2("María González");
        booking.setParticipant3("Carlos López");
        // Podrías establecer más participantes si es necesario para tus pruebas
        return booking;
    }

    @Test
    void getAllBookings_ShouldReturnAllBookings() {
        // Arrange
        bookingEntity booking1 = createSampleBooking();
        bookingEntity booking2 = createSampleBooking();
        booking2.setId(2L);
        booking2.setStatus("Pendiente");

        List<bookingEntity> expectedBookings = Arrays.asList(booking1, booking2);

        when(bookingRepo.findAll()).thenReturn(expectedBookings);

        // Act
        List<bookingEntity> actualBookings = bookingService.getAllBookings();

        // Assert
        assertEquals(2, actualBookings.size());
        assertEquals("Confirmado", actualBookings.get(0).getStatus());
        assertEquals("Pendiente", actualBookings.get(1).getStatus());
        verify(bookingRepo, times(1)).findAll();
    }

    @Test
    void saveBooking_ShouldSetDefaultStatusAndSaveWithAllAttributes() {
        // Arrange
        bookingEntity booking = createSampleBooking();
        booking.setStatus(null); // Simular status no definido

        when(bookingRepo.save(booking)).thenReturn(booking);

        // Act
        bookingEntity result = bookingService.saveBooking(booking);

        // Assert
        assertNotNull(result);
        assertEquals("Pendiente", result.getStatus());
        assertEquals("2023-12-25", result.getDate());
        assertEquals(LocalTime.of(14, 30), result.getTime());
        assertEquals(1, result.getType());
        assertEquals(5, result.getQuantity());
        assertEquals("12345678-9", result.getRutWhoMadeBooking());
        assertEquals("Juan Pérez", result.getParticipant1());
        verify(bookingRepo, times(1)).save(booking);
    }

    @Test
    void getBookingById_ShouldReturnBookingWithAllDetails() {
        // Arrange
        Long id = 1L;
        bookingEntity expectedBooking = createSampleBooking();

        when(bookingRepo.findById(id)).thenReturn(Optional.of(expectedBooking));

        // Act
        bookingEntity actualBooking = bookingService.getBookingById(id);

        // Assert
        assertNotNull(actualBooking);
        assertEquals(id, actualBooking.getId());
        assertEquals("Confirmado", actualBooking.getStatus());
        assertEquals("2023-12-25", actualBooking.getDate());
        assertEquals(5, actualBooking.getQuantity());
        assertEquals("Juan Pérez", actualBooking.getParticipant1());
        verify(bookingRepo, times(1)).findById(id);
    }

    @Test
    void getBookingsByRut_ShouldReturnBookingsWithParticipants() {
        // Arrange
        String rut = "12345678-9";
        bookingEntity booking1 = createSampleBooking();
        bookingEntity booking2 = createSampleBooking();
        booking2.setId(2L);
        booking2.setParticipant1("Ana Silva");

        List<bookingEntity> expectedBookings = Arrays.asList(booking1, booking2);

        when(bookingRepo.findByRutWhoMadeBooking(rut)).thenReturn(expectedBookings);

        // Act
        List<bookingEntity> actualBookings = bookingService.getBookingsByRut(rut);

        // Assert
        assertEquals(2, actualBookings.size());
        assertEquals("Juan Pérez", actualBookings.get(0).getParticipant1());
        assertEquals("Ana Silva", actualBookings.get(1).getParticipant1());
        verify(bookingRepo, times(1)).findByRutWhoMadeBooking(rut);
    }

    @Test
    void updateBooking_ShouldUpdateAllFields() {
        // Arrange
        bookingEntity originalBooking = createSampleBooking();
        bookingEntity updatedBooking = createSampleBooking();
        updatedBooking.setStatus("Cancelado");
        updatedBooking.setQuantity(3);
        updatedBooking.setParticipant1("Pedro Martínez");
        updatedBooking.setParticipant2(null); // Simular eliminación de participante

        when(bookingRepo.save(updatedBooking)).thenReturn(updatedBooking);

        // Act
        bookingEntity result = bookingService.updateBooking(updatedBooking);

        // Assert
        assertEquals("Cancelado", result.getStatus());
        assertEquals(3, result.getQuantity());
        assertEquals("Pedro Martínez", result.getParticipant1());
        assertNull(result.getParticipant2());
        verify(bookingRepo, times(1)).save(updatedBooking);
    }

    @Test
    void deleteBooking_ShouldDeleteBookingWithParticipants() throws Exception {
        // Arrange
        Long id = 1L;
        bookingEntity booking = createSampleBooking();

        when(bookingRepo.findById(id)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepo).deleteById(id);

        // Act
        boolean result = bookingService.deleteBooking(id);

        // Assert
        assertTrue(result);
        verify(bookingRepo, times(1)).deleteById(id);
        // Verificar que la reserva que se eliminó tenía los participantes esperados
        bookingEntity deletedBooking = bookingService.getBookingById(id);
        assertEquals("Juan Pérez", deletedBooking.getParticipant1());
    }

    @Test
    void getBookingsByStatus_ShouldFilterByStatus() {
        // Arrange
        String status = "Confirmado";
        bookingEntity booking1 = createSampleBooking();
        bookingEntity booking2 = createSampleBooking();
        booking2.setId(2L);
        booking2.setStatus("Pendiente");

        List<bookingEntity> allBookings = Arrays.asList(booking1, booking2);
        List<bookingEntity> expectedBookings = List.of(booking1);

        when(bookingRepo.findByStatus(status)).thenReturn(expectedBookings);

        // Act
        List<bookingEntity> actualBookings = bookingService.getBookingsByStatus(status);

        // Assert
        assertEquals(1, actualBookings.size());
        assertEquals("Confirmado", actualBookings.get(0).getStatus());
        assertEquals("Juan Pérez", actualBookings.get(0).getParticipant1());
        verify(bookingRepo, times(1)).findByStatus(status);
    }
}