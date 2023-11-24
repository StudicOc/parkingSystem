package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.util.Date;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    // CONNECTION WITH DATABASE //
    @Mock
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    // CREATE NEW TICKET //
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private Ticket ticket;
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
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date());
        ticket.setOutTime(null);
    }
    @Test
    public void SaveTicketDAOTest ()  {
        //WHEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);

        //THEN
        int resultTicketEntries = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);
        //int resultTicketsExiting = ticketDAO.getNbTicket("ABCDEF");
        //ticketDAO.saveTicket(ticket);

        //ASSERT
        Assertions.assertNotNull(ticket.getParkingSpot());
        Assertions.assertEquals(resultTicketEntries, 1);
    }
    @Test
    public void GetTicketDAOTest (){
        //WHEN
        Mockito.when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        //THEN
       Ticket ticket =  ticketDAO.getTicket("ABCDEF");
        //ASSERT
        assertNotNull(ticket);
    }
    @Test
    public void UpdateTicketTest () {

        ParkingSpot parkingSpot = ticket.getParkingSpot();
        //THEN
        parkingSpotDAO.updateParking(parkingSpot);
        //ASSERT
        assertFalse(ticket.getParkingSpot().isAvailable());
    }
}