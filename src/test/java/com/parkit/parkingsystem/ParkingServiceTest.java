package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.service.FareCalculatorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLTimeoutException;
import java.sql.SQLException;


@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @Mock
    private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static Ticket ticket;
	@Mock
    private static FareCalculatorService fareCalculatorService;

//	Ticket ticket = new Ticket();

	@BeforeEach
	void setupPerTest() {
		new DataBasePrepareService().clearDataBaseEntries();
	}

	@Test
	@DisplayName("processIncomingVehicle")
	public void processIncomingVehicleTest() throws Exception, SQLTimeoutException, SQLException {

		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("AZERTY");
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(2);

		TicketDAO ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = new DataBaseTestConfig();

		// WHEN
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		// THEN
		assertThat(ticketDAO.getTicket("AZERTY").getPrice()).isEqualTo(0.0);
		assertThat(ticketDAO.getTicket("AZERTY").getOutTime()).isNull();
	}

    @Test
    @DisplayName("processExitingVehicle")
    public void processExitingVehicleTtest() throws Exception, SQLTimeoutException, SQLException {
    	
    	// GIVEN
    	when(inputReaderUtil.readSelection()).thenReturn(1);
   		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("AZERTY");
  		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(2);
  		
  		TicketDAO ticketDAO = new TicketDAO();
   		ticketDAO.dataBaseConfig = new DataBaseTestConfig();

  		// WHEN
   		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	parkingService.processIncomingVehicle();
    	Thread.currentThread();
		Thread.sleep(1000);
    	parkingService.processExitingVehicle();
        
		// THEN
		assertThat(ticketDAO.getTicket("AZERTY").getPrice()).isEqualTo(0.0);
		assertThat(ticketDAO.getTicket("AZERTY").getOutTime()).isNotNull();
		assertThat(ticketDAO.getTicket("AZERTY").getOutTime()).isAfter(ticketDAO.getTicket("AZERTY").getInTime());
    }


}
