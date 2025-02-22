package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;





public class FareCalculatorService {



    public void calculateFare(Ticket ticket, boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        // Declaration variable for 5% discount //
        double discountPercentage = 5;

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long outTime = ticket.getOutTime().getTime();
        long inTime = ticket.getInTime().getTime();
        float duration =  ((outTime - inTime) / (float)(60*60*1000));

        double timeSpent= (double) Math.round(duration * 1000)/1000;


        // Parking is free for parking times less than 30 minutes ;

        if(timeSpent <= 0.5){
            ticket.setPrice(0);
            return;
        }
                            // CALCULATOR //
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(timeSpent * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(timeSpent * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }

        if(discount) {
            ticket.setPrice( ticket.getPrice() * (1 - (discountPercentage/100)));
        }
    }

                            // NO DISCOUNT FOR CLIENT //
    public void calculateFare(Ticket ticket) {
     calculateFare(ticket, false);
    }
}
