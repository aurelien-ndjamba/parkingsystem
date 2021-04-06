package com.parkit.parkingsystem.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketTest {

	@Test
	@Tag("TICKET CHECKING")
	@DisplayName("CHECKING setId(id)")
	public void setIdTest() {
		// GIVEN
		Ticket ticket = new Ticket();
		int id = 160524;
		// WHEN
		ticket.setId(id);
		// THEN
		assertThat(ticket.getId()).isEqualTo(160524);
	}

}