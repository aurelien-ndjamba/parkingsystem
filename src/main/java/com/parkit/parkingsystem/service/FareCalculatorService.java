package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Duration;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		/*
		 * TODO 1: Fix the code to make the unit tests pass
		 * 
		 * La méthode .getHours() du type DATE est obsolète, deplus elle renvoie un int
		 * compris entre 0 et 23 qui représente l'heure du jour. Donc le prix n'est pas 
		 * calculé sur le temps par minutes réellement stationné que ce soit la même 
		 * journée ou au delà.
		 * 
		 * La méthode .getHours() est remplacée par .getTime() pour en tenir compte.
		 * 
		 */

		long inHour = ticket.getInTime().getTime();
		long outHour = ticket.getOutTime().getTime();

		long duration = outHour - inHour;
		
		double durationHour = (double) duration / (Duration.hourInSecond * Duration.secondInMillisecond);

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(durationHour * Fare.CAR_RATE_PER_HOUR);
			break;
		}
		case BIKE: {
			ticket.setPrice(durationHour * Fare.BIKE_RATE_PER_HOUR);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
}