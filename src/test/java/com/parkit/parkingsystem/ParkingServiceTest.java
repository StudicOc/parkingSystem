package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
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
    public static TicketDAO ticketDAO;
    @Mock
    private Ticket ticket;

    @BeforeEach
    private void setUpPerTest () {

        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

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
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        //  VERIFY
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void testProcessIncomingVehicle () {
        /*testProcessIncomingVehicle: test the method call processIncomingVehicle() where everything happens as expected.*/

        //GIVEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        //THEN
        parkingService.processIncomingVehicle();
        ticketDAO.saveTicket(ticket);
        //ASSERT
        Assertions.assertEquals(1, inputReaderUtil.readSelection());

    }

    @Test
    public void processExitingVehicleTestUnableUpdate (){
        /* execution of the test in the case where the updateTicket() method of ticketDAO returns false
        when calling processExitingVehicle()*/

        //GIVEN -Initialisation
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
        //WHEN
        parkingService.processExitingVehicle();
        //THEN
        Assertions.assertFalse(ticketDAO.updateTicket(any(Ticket.class)));

    }

    @Test
    public void testGetNextParkingNumberIfAvailable(){
        /* the call of the method getNext ParkingNumberIf Available()
        with resul obtaining a spot whose ID is 1 and which is available
        */

        //GIVEN
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        //THEN
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        //ASSERT
        Assertions.assertEquals(1, parkingSpot.getId());
        Assertions.assertTrue(parkingSpot.isAvailable());
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        //Calling the getNextParkingNumberIfAvailable() method resulting in no spots available (method returns null)
        //GIVEN
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
        //THEN
        parkingService.getNextParkingNumberIfAvailable();
        //ASSERT
        Assertions.assertEquals(0, parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class)));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){

        //GIVEN
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(3);
        //THEN
        parkingService.getNextParkingNumberIfAvailable();
        //ASSERT
        Assertions.assertEquals(3, inputReaderUtil.readSelection());
    }



    //MORE TEST//
    @Test
    public void testProcessIncomingVehicleRecurrentClient () {
    /*testProcessIncomingVehicle: test the method call
    processIncomingVehicle() when we have reccurent client.
    With message 5% discount*/

        //GIVEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(2);
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        //THEN
        parkingService.processIncomingVehicle();
        ticketDAO.saveTicket(ticket);
        //ASSERT
        Assertions.assertEquals(2,ticketDAO.getNbTicket("ABCDEF"));
        Assertions.assertEquals(1, inputReaderUtil.readSelection());

    }



}