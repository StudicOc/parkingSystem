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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {
    @Mock
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static DataBasePrepareService dataBasePrepareService;
    @Mock
    private com.parkit.parkingsystem.model.ParkingSpot ParkingSpot;



    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();

    }


    @Test
    public void updateParking(){
        Mockito.when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotDAO.updateParking(parkingSpot);

        assertEquals(1,parkingSpot.getId());
        assertEquals(true,parkingSpot.isAvailable());
    }
    @Test
    public void NotUpdateParking(){
        Mockito.when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);

        ParkingSpot parkingSpot = new ParkingSpot(0, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);

        assertEquals(0,parkingSpot.getId());
        assertEquals(false,parkingSpot.isAvailable());
    }
}
