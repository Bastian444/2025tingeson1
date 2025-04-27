package com.tingeso.backend.services;

import com.tingeso.backend.entities.staffEntity;
import com.tingeso.backend.repositories.staffRepository;
import org.junit.jupiter.api.BeforeEach;
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
class staffServiceTest {

    @Mock
    private staffRepository staffRepo;

    @InjectMocks
    private staffService staffService;

    private staffEntity testStaff;

    @BeforeEach
    void setUp() {
        testStaff = new staffEntity();
        testStaff.setId(1L);
        testStaff.setStaffname("Juan");
        testStaff.setStafflastName("Pérez");
        testStaff.setStaffemail("juan.perez@example.com");
        testStaff.setStaffrut("12345678-9");
        testStaff.setStaffpassword("securePassword123");
    }

    @Test
    void getAllStaff_ShouldReturnAllStaff() {
        // Arrange
        staffEntity staff2 = new staffEntity();
        staff2.setId(2L);
        staff2.setStaffname("María");

        List<staffEntity> staffList = Arrays.asList(testStaff, staff2);
        when(staffRepo.findAll()).thenReturn(staffList);

        // Act
        List<staffEntity> result = staffService.getAllStaff();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getStaffname());
        assertEquals("María", result.get(1).getStaffname());
        verify(staffRepo, times(1)).findAll();
    }

    @Test
    void saveStaff_ShouldSaveStaffWithAllAttributes() {
        // Arrange
        when(staffRepo.save(testStaff)).thenReturn(testStaff);

        // Act
        staffEntity result = staffService.saveStaff(testStaff);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getStaffname());
        assertEquals("Pérez", result.getStafflastName());
        assertEquals("juan.perez@example.com", result.getStaffemail());
        assertEquals("12345678-9", result.getStaffrut());
        verify(staffRepo, times(1)).save(testStaff);
    }

    @Test
    void getStaffById_ShouldReturnStaffWhenExists() {
        // Arrange
        when(staffRepo.findById(1L)).thenReturn(Optional.of(testStaff));

        // Act
        staffEntity result = staffService.getStaffById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getStaffname());
        verify(staffRepo, times(1)).findById(1L);
    }

    @Test
    void getStaffById_ShouldReturnNullWhenNotExists() {
        // Arrange
        when(staffRepo.findById(99L)).thenReturn(Optional.empty());

        // Act
        staffEntity result = staffService.getStaffById(99L);

        // Assert
        assertNull(result);
        verify(staffRepo, times(1)).findById(99L);
    }

    @Test
    void getStaffByStaffrut_ShouldReturnStaff() {
        // Arrange
        String rut = "12345678-9";
        when(staffRepo.findByStaffrut(rut)).thenReturn(testStaff);

        // Act
        staffEntity result = staffService.getStaffByStaffrut(rut);

        // Assert
        assertNotNull(result);
        assertEquals(rut, result.getStaffrut());
        assertEquals("Juan", result.getStaffname());
        verify(staffRepo, times(1)).findByStaffrut(rut);
    }

    @Test
    void updateStaff_ShouldUpdateAllFields() {
        // Arrange
        testStaff.setStaffname("Juan Carlos");
        testStaff.setStaffemail("nuevo.email@example.com");

        when(staffRepo.save(testStaff)).thenReturn(testStaff);

        // Act
        staffEntity result = staffService.updateStaff(testStaff);

        // Assert
        assertEquals("Juan Carlos", result.getStaffname());
        assertEquals("nuevo.email@example.com", result.getStaffemail());
        verify(staffRepo, times(1)).save(testStaff);
    }

    @Test
    void deleteStaff_ShouldReturnTrueWhenDeleted() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(staffRepo).deleteById(id);

        // Act
        boolean result = staffService.deleteStaff(id);

        // Assert
        assertTrue(result);
        verify(staffRepo, times(1)).deleteById(id);
    }

    @Test
    void deleteStaff_ShouldThrowExceptionWhenErrorOccurs() {
        // Arrange
        Long id = 1L;
        doThrow(new RuntimeException("Database error")).when(staffRepo).deleteById(id);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            staffService.deleteStaff(id);
        });

        assertEquals("Database error", exception.getMessage());
        verify(staffRepo, times(1)).deleteById(id);
    }
}