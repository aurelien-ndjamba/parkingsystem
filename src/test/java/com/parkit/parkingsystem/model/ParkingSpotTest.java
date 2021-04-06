package com.parkit.parkingsystem.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;

class ParkingSpotTest {

	@Test
	void testEqualsIsTrueWithSameObjet() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		assertThat(parkingSpot.equals(parkingSpot)).isTrue();
	}

	@Test
	void testEqualsIsTrueWithOtherObjet() {
		ParkingSpot parkingSpot1 = new ParkingSpot(1, ParkingType.CAR, true);
		ParkingSpot parkingSpot2 = new ParkingSpot(1, ParkingType.BIKE, false);
		assertThat(parkingSpot1.equals(parkingSpot2)).isTrue();
	}

	@Test
	void testEqualsIsFalseWithOtherObjet() {
		ParkingSpot parkingSpot1 = new ParkingSpot(1, ParkingType.CAR, true);
		ParkingSpot parkingSpot2 = new ParkingSpot(3, ParkingType.CAR, true);
		assertThat(parkingSpot1.equals(parkingSpot2)).isFalse();
	}
	
	@Test
	void getParkingTypeTest() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		parkingSpot.setParkingType(ParkingType.BIKE);
		assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
	}
	
	@Test
	void setIdTest() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		parkingSpot.setId(3);
		assertThat(parkingSpot.getId()).isEqualTo(3);
	}
	
	@Test
	void hashCodeTest() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		assertThat(parkingSpot.hashCode()).isEqualTo(1);
	}
}
