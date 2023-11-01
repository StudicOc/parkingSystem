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
    public void testGetNextParkingNumberIfAvailable(){
        /* the call of the method getNext ParkingNumberIf Available()
        with resul obtaining a spot whose ID is 1 and which is available
        */
            /* public int getId() {return number;}
             public ParkingType getParkingType() {return parkingType;}
             public boolean isAvailable() {return isAvailable;}
             */

        //WHEN
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        //VERIFY
        parkingService.getNextParkingNumberIfAvailable();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        //Calling the getNextParkingNumberIfAvailable() method resulting in no spots available (method returns null)

        //WHEN
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
        //VERIFY
        parkingService.getNextParkingNumberIfAvailable();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){

        /*test method call getNextParkingNumberIfAvailable() results in no spots
        (the method returns null) because the argument entered by the user
        regarding the vehicle type is incorrect*/

        //WHEN
        Mockito.when(inputReaderUtil.readSelection()).thenReturn(3);
        parkingService.getNextParkingNumberIfAvailable();
        //VERIFY
        verify(inputReaderUtil, Mockito.times(1)).readSelection();


    }

}

