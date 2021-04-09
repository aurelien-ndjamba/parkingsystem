package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import com.parkit.parkingsystem.constants.ParkingType;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        assertThat(ticketDAO.getTicket("ABCDEF").getId()).isEqualTo(1);      
		assertThat(ticketDAO.getTicket("ABCDEF").getParkingSpot().getId()).isEqualTo(1);  
		assertThat(ticketDAO.getTicket("ABCDEF").getPrice()).isEqualTo(0.0); 
		assertThat(ticketDAO.getTicket("ABCDEF").getInTime()).isNotNull(); 
		assertThat(ticketDAO.getTicket("ABCDEF").getOutTime()).isNull();
		assertThat(ticketDAO.getTicket("ABCDEF").isDiscount()).isEqualTo(false);   
		assertThat(ticketDAO.getTicket("ABCDEF").getParkingSpot().isAvailable()).isEqualTo(false);
		assertThat(ticketDAO.getTicket("ABCDEF").getParkingSpot().getParkingType().toString()).isEqualTo("CAR");
		
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);      
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).isEqualTo(4);
    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        
        //TODO: check that the fare generated and out time are populated correctly in the database
        assertThat(ticketDAO.getTicket("ABCDEF").getPrice()).isEqualTo(0.0); //because less half hour
		assertThat(ticketDAO.getTicket("ABCDEF").getInTime()).isBefore(ticketDAO.getTicket("ABCDEF").getOutTime()); 
		assertThat(ticketDAO.getTicket("ABCDEF").getOutTime()).isNotNull();
		assertThat(ticketDAO.getTicket("ABCDEF").isDiscount()).isEqualTo(false); 
		
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);      
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).isEqualTo(4);
    }

}