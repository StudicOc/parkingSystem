package com.parkit.parkingsystem;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.util.Date;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    // CONNECTION WITH DATABASE //
    @Mock
    private final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    // CREATE NEW TICKET //

    @Mock
    private final TicketDAO ticketDAO = new TicketDAO();

    @Mock
    private Ticket ticket;

    @Mock
    private static ParkingService parkingService;
    @Mock
    private  static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    // SetUp Object TICKET //
    @BeforeEach
    public void setUp(){
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date());
        ticket.setOutTime(null);
    }

                // TEST MOCKITO - UNIT TEST //
    @Test
    public void SaveTicketDAOTest ()  {
        //First method = TicketDAO = Use in ParkingService
        // For save ticket = we need occurrence and VehicleRegNumber
        //WHEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);

        //THEN
        int resultTicketEntries = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);
        int resultTicketsExiting = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);

        //ASSERT - ARRAYList
        assertNotNull(ticket.getParkingSpot());
        assertEquals(resultTicketEntries + 1, resultTicketsExiting, 1);
    }

}
