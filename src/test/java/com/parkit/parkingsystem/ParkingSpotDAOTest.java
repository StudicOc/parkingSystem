package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
public class ParkingSpotDAOTest {
    @Mock
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();

    }
    @BeforeEach
    private void setUpPerTest () {
        ParkingSpot initializeParkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotDAO.updateParking(initializeParkingSpot);
    }

    @Test
    public void getNextAvailableSlot () {
        // parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        int NextAvailableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals (1, NextAvailableSlot);
    }
    @Test
    public void updateParking(){
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        //ASSERT
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }
    @Test
    public void NotUpdateParking(){
        ParkingSpot parkingSpot = new ParkingSpot(0, ParkingType.CAR, false);
        //ASSERT
        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }
}
