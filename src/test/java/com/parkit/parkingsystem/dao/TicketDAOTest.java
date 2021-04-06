package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TicketDAOTest {

	Ticket ticket = new Ticket();

	@BeforeAll
	void setupPerTest() {
		new DataBasePrepareService().clearDataBaseEntries();
	}

	@Test
	@Tag("TicketDAO.saveTicket")
	@DisplayName("Controler que la méthode 'saveTicket' sauvegarde un ticket dans une base de donnée ticket")
	public void saveTicketTest() {

		// GIVEN
		ParkingSpot parkingSpot = new ParkingSpot(3, ParkingType.CAR, false);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("AZERTY");
		ticket.setPrice(0.0);
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		ticket.setDiscount(false);

		// WHEN
		TicketDAO ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = new DataBaseTestConfig();
		ticketDAO.saveTicket(ticket);

		// THEN
		assertThat(ticketDAO.getTicket("AZERTY").getParkingSpot().getId()).isEqualTo(3);
		assertThat(ticketDAO.getTicket("AZERTY").getPrice()).isEqualTo(0.0);
		assertThat(ticketDAO.getTicket("AZERTY").getOutTime()).isNull();
	}

	@Test
	@Tag("TicketDAO.updateTicket")
	@DisplayName("Controler que la méthode 'updateTicket' met à jour une base de donnée ticket")
	public void updateTicketTest() {

		// GIVEN
		saveTicketTest();
		TicketDAO ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = new DataBaseTestConfig();
		ticket.setPrice(12.5);
		Date outTime = new Date();
		ticket.setOutTime(outTime);
		System.out.println(ticketDAO.getTicket("AZERTY").getId());
		ticket.setId(ticketDAO.getTicket("AZERTY").getId());

		// WHEN
		ticketDAO.updateTicket(ticket);

		// THEN
		assertThat(ticketDAO.getTicket("AZERTY").getOutTime()).isAfter(ticketDAO.getTicket("AZERTY").getInTime());
		assertThat(ticketDAO.getTicket("AZERTY").getPrice()).isEqualTo(12.5);
	}

}