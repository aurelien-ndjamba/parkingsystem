package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

    @Tag("ParkingACar")
	@DisplayName("Controler que pour l'entrée d'un véhicule, le ticket est bien sauvegardé et la table de parking mis à jour des disponibilités dans la database")
    @Test
    public void testParkingACar(){
    	
    	//WHEN
    	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        
    	//THEN
        
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        
        //ticket is actualy saved in DB
        assertThat(ticketDAO.getTicket("ABCDEF").getId()).isEqualTo(1);      
		assertThat(ticketDAO.getTicket("ABCDEF").getParkingSpot().getId()).isEqualTo(1);  
		assertThat(ticketDAO.getTicket("ABCDEF").getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(ticketDAO.getTicket("ABCDEF").getPrice()).isEqualTo(0.0); 
		assertThat(ticketDAO.getTicket("ABCDEF").getInTime()).isNotNull(); 
		assertThat(ticketDAO.getTicket("ABCDEF").getOutTime()).isNull();
		
		//Parking table is updated with availability
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);      
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).isEqualTo(4);
    }
    @Tag("ParkingLotExit")
	@DisplayName("Controler que pour la sortie d'un véhicule, le tarif et l'horaire de départ sont correctement sauvegardé dans la database")
    @Test
    public void testParkingLotExit() throws InterruptedException{

    	//WHEN
        testParkingACar();
        java.lang.Thread.sleep(3000);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        
        //THEN
        
        //TODO: check that the fare generated and out time are populated correctly in the database
        
        //fare generated in database
		assertThat(ticketDAO.getTicket("ABCDEF").getPrice()).isEqualTo(0.0); 
		
		//out time in database
		assertThat(ticketDAO.getTicket("ABCDEF").getInTime()).isBefore(ticketDAO.getTicket("ABCDEF").getOutTime()); 
		assertThat(ticketDAO.getTicket("ABCDEF").getOutTime()).isNotNull();
    }

}
