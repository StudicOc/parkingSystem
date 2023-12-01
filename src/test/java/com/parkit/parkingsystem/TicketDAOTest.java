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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    @Mock
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private Ticket ticket;
    @Mock
    public static DataBasePrepareService dataBasePrepareService;


    @BeforeAll
    public static void setUp(){
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();

    }

    @BeforeEach
    public void setUpPerTest () {
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
    public void saveTicketDAOTest ()  {
        //WHEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);

        //THEN
        int resultTicketEntries = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);

        //ASSERT
        Assertions.assertNotNull(ticket.getParkingSpot());
        Assertions.assertEquals(resultTicketEntries, 1);
    }


    @Test
    public void saveFailTicketDAOTest ()  {
        //WHEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(0);

        //THEN
        int resultTicketEntries = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);

        //ASSERT
        Assertions.assertNotNull(ticket.getParkingSpot());
        Assertions.assertEquals(resultTicketEntries, 0);
    }

    @Test
    public void getTicketDAOTest (){
        //WHEN
        Mockito.when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        //THEN
        Ticket ticket =  ticketDAO.getTicket("ABCDEF");
        //ASSERT
        Assertions.assertNotNull(ticket);
    }


}