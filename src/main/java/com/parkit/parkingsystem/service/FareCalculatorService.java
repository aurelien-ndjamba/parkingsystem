package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Duration;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		//TODO 1: Fix the code to make the unit tests pass

		long inHour = ticket.getInTime().getTime();
		long outHour = ticket.getOutTime().getTime();

		long duration = outHour - inHour;

		double durationHour = (double) duration / (Duration.hourInSecond * Duration.secondInMillisecond);

		if (durationHour > Duration.halfHourByHour) {

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

		else
			ticket.setPrice(Fare.RATE_FOR_LESS_THAN_HALF_HOUR); // STORY1: Free 30-min parking
		
		if (ticket.isDiscount())  //STORY2 : 5%-discount for recurring users
			ticket.setPrice(ticket.getPrice() * Fare.DISCOUNT);
	}
}