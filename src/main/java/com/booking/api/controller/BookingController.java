package com.booking.api.controller;

import com.booking.api.controller.dto.BookingRequestDto;
import com.booking.api.controller.dto.BookingResultDto;
import com.booking.api.controller.dto.AvailableRoomsDto;
import com.booking.api.errors.ErrorDto;
import com.booking.integrations.booking.service.model.Room;
import com.booking.integrations.booking.service.model.RoomSearchResult;
import com.booking.service.BookingManager;
import com.booking.service.exception.MaintenanceTimeOverlapException;
import com.booking.service.exception.NoSuitableRoomsException;
import com.booking.service.model.BookingRequest;
import com.booking.shared.Interval;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BookingController {
    public static final String ERROR_MAINTENANCE_TIMINGS_OVERLAPPED = "BKS-001";
    public static final String ERROR_NO_ROOMS_AVAILABLE = "BKS-002";
    private final BookingManager bookingManager;

    //Use mock mvc for controller tests
    @PostMapping("/bookings")
    public ResponseEntity<BookingResultDto> bookRoom(@RequestBody BookingRequestDto bookingRequestDto)
            throws MaintenanceTimeOverlapException, NoSuitableRoomsException {
        Interval interval = new Interval(bookingRequestDto.startTime(), bookingRequestDto.endTime());
        BookingRequest request = new BookingRequest(interval, bookingRequestDto.noOfPersons());

        Room room = bookingManager.reserveRoom(request);

        BookingResultDto bookingResultDto = new BookingResultDto(room.name(), room.size());
        return ResponseEntity.ok(bookingResultDto);
    }


    @GetMapping("/rooms/available")
    public ResponseEntity<?> fetchAvailableRooms(@RequestParam @FutureOrPresent LocalTime startTime,
                                                 @RequestParam @FutureOrPresent LocalTime endTime) throws NoSuitableRoomsException {

        Interval interval = new Interval(startTime,endTime);

        RoomSearchResult roomSearchResult = bookingManager.fetchAvailableRooms(interval);

        if (roomSearchResult.reason() != null) {
            String message = switch (roomSearchResult.reason()) {
                case OVERLAP_WITH_MAINTENANCE_TIME -> "Maintenance timings are overlapped with Given Timings";
                case ALL_ROOMS_BOOKED -> "All rooms are booked for the Given timings";
            };
            ErrorDto errorDto = new ErrorDto(ERROR_NO_ROOMS_AVAILABLE, message);
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }

        AvailableRoomsDto availableRoomsDto = mapToDto(roomSearchResult);
        return ResponseEntity.ok(availableRoomsDto);
    }

    private AvailableRoomsDto mapToDto(RoomSearchResult roomSearchResult) {
        List<AvailableRoomsDto.Room> rooms = roomSearchResult.rooms()
                                                             .stream()
                                                             .map(BookingController::mapToDto)
                                                             .toList();

        return new AvailableRoomsDto(rooms);
    }

    private static AvailableRoomsDto.Room mapToDto(Room room) {
        return new AvailableRoomsDto.Room(room.name(), room.size());
    }


    @ExceptionHandler(value = NoSuitableRoomsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorDto handleNoRoomsAvailableException(NoSuitableRoomsException ex) {
        log.error("Exception details are:", ex);
        return new ErrorDto(ERROR_NO_ROOMS_AVAILABLE, "No Rooms are Available for this no of persons");
    }


    @ExceptionHandler(value = MaintenanceTimeOverlapException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorDto handleMaintenanceTimingException(MaintenanceTimeOverlapException ex) {
        log.error("Exception details are:", ex);
        return new ErrorDto(ERROR_MAINTENANCE_TIMINGS_OVERLAPPED, "Maintenance Timings are Overlapped ");
    }

}
