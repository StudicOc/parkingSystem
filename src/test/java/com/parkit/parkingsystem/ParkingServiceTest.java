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
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }


                    // MOCKITO TEST //
    @Test
    public void processExitingVehicleTest(){

        /*  Please type the vehicle registration number and press enter key
            Please pay the parking fare:1.5
            Recorded out-time for vehicle number:ABCDEF is:Thu Oct 26 15:36:21 CEST 2023*/

        //  WHEN
        Mockito.when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        //  VERIFY
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void testProcessIncomingVehicle(){}


        //execution of the test in the case where the updateTicket() method of ticketDAO returns false
        //when calling processExitingVehicle()
        /* we need to use :
        *  processExitingVehicle();
        *  updateTicket();
        *  class ticketDAO return false
        * */
    @Test
    public void processExitingVehicleTestUnableUpdate(){}

    @Test
    public void testGetNextParkingNumberIfAvailable(){}

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound(){}

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){}


}
