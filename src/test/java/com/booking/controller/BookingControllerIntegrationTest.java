package com.booking.controller;

import com.booking.api.controller.dto.BookingRequestDto;
import com.booking.integrations.booking.impl.persistence.repository.BookingRepository;
import com.booking.integrations.booking.impl.persistence.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    private LocalTime bookingStartTime;
    private LocalTime bookingEndTime;

    @BeforeEach
     void setup() {
        bookingStartTime = LocalTime.of(10,30);
        bookingEndTime = LocalTime.of(11,0);
    }
        @Test
         void createBooking_ValidTimes_ShouldReturnBookingResponse() throws Exception {
            BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                    .startTime(bookingStartTime)
                    .endTime(bookingEndTime)
                    .noOfPersons(5).build();

            mockMvc.perform(post("/v1/bookings")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(bookingRequestDto)))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.roomName", is("Inspire")))
                   .andExpect(jsonPath("$.roomSize", is(12)));
        }

        @Test
         void getAvailableRooms_ValidTimes_ShouldReturnBookingResponse() throws Exception {

            mockMvc.perform(get("/v1/rooms/available")
                                    .param("startTime", "10:30:00")
                                    .param("endTime", "11:00:00"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.rooms.length()", is(2)))
                   .andExpect(jsonPath("$.rooms.[0].name", is("Amaze")))
                   .andExpect(jsonPath("$.rooms.[0].size", is(3)));
        }


    @Test
    void createBooking_ValidTimesNextAvailableRoom_ShouldReturnBookingResponse() throws Exception {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                                                               .startTime(bookingStartTime)
                                                               .endTime(bookingEndTime)
                                                               .noOfPersons(5).build();

        mockMvc.perform(post("/v1/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookingRequestDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.roomName", is("Beauty")))
               .andExpect(jsonPath("$.roomSize", is(7)));
    }
    @Test
     void createBooking_InValidMaximumPersons_ShouldReturnBadRequest() throws Exception {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                                                               .startTime(bookingStartTime)
                                                               .endTime(bookingEndTime)
                                                               .noOfPersons(21).build();

        mockMvc.perform(post("/v1/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookingRequestDto)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message", is("Max persons must be less than or equal to 20")));
    }

    @Test
     void createBooking_InValidMinimumPersons_ShouldReturnBadRequest() throws Exception {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                                                               .startTime(bookingStartTime)
                                                               .endTime(bookingEndTime)
                                                               .noOfPersons(1).build();

        mockMvc.perform(post("/v1/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookingRequestDto)))
               .andExpect(status().isBadRequest())
             /*  .andDo(result -> {
                   System.out.println("Response: " + result.getResponse().getContentAsString());
               })*/
               .andExpect(jsonPath("$.message", is("Min persons must be greater than or equal to 2")));
    }
        @Test
         void createBooking_InvalidTimes_ShouldReturnBadRequest() throws Exception {
            bookingStartTime = LocalTime.of(11,0);
            bookingEndTime = LocalTime.of(10,30);
            BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                                                                   .startTime(bookingStartTime)
                                                                   .endTime(bookingEndTime)
                                                                   .noOfPersons(5).build();

            mockMvc.perform(post("/v1/bookings")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(bookingRequestDto)))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.message", is("Booking start time should be before booking end time")));
        }

        @Test
         void createBooking_InvalidInterval_ShouldReturnBadRequest() throws Exception {

            bookingStartTime = LocalTime.of(10,30);
            bookingEndTime = LocalTime.of(10,44);
            BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                                                                   .startTime(bookingStartTime)
                                                                   .endTime(bookingEndTime)
                                                                   .noOfPersons(5).build();
            mockMvc.perform(post("/v1/bookings")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(bookingRequestDto)))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.message", is("Booking interval should be in terms of 15 minutes")));
        }


        @Test
         void getAvailableRooms_InvalidTimes_ShouldReturnBadRequest() throws Exception {
            mockMvc.perform(get("/v1/rooms/available")
                                    .param("startTime", "11:00:00")
                                    .param("endTime", "10:30:00"))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.message", is("Booking start time should be before booking end time")));
        }

        @Test
         void getAvailableRooms_InvalidInterval_ShouldReturnBadRequest() throws Exception {
           mockMvc.perform(get("/v1/rooms/available")
                                    .param("startTime", "10:30:00")
                                    .param("endTime", "10:44:00"))
                  .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.message", is("Booking interval should be in terms of 15 minutes")));
        }


    @Test
    void createBooking_InvalidIntervalDuringMaintenanceTiming_ShouldReturnBadRequest() throws Exception {

        bookingStartTime = LocalTime.of(9,0);
        bookingEndTime = LocalTime.of(10,0);
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                                                               .startTime(bookingStartTime)
                                                               .endTime(bookingEndTime)
                                                               .noOfPersons(5).build();
        mockMvc.perform(post("/v1/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookingRequestDto)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message", is("Maintenance Timings are Overlapped ")));
    }


    @Test
    void getAvailableRooms_InvalidTimesDuringMaintenanceTiming_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/v1/rooms/available")
                                .param("startTime", "09:00:00")
                                .param("endTime", "10:30:00"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message", is("Maintenance timings are overlapped with Given Timings")));
    }



}
