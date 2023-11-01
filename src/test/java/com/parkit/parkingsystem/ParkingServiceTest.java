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

import java.util.Date;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest () {

            // lenient() => execute when Unnecessary stubbing detected
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }


                                        // MOCKITO TEST //
    @Test
    public void processExitingVehicleTest () {
        //  WHEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        //  VERIFY
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void testProcessIncomingVehicle () {

            //WHEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        parkingService.processIncomingVehicle();
            //VERIFY
        verify(ticketDAO,Mockito.times(1)).saveTicket(any(Ticket.class));

    }

    @Test
    public void processExitingVehicleTestUnableUpdate (){

        //WHEN
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
        //VERIFY
        parkingService.processExitingVehicle();
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));

    }

    @Test
    public void testGetNextParkingNumberIfAvailable(){}

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {}

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){}

}

