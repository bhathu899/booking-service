package com.booking.controller;

import com.booking.controller.dto.AvailableRoomsDto;
import com.booking.controller.dto.BookingRequestDto;
import com.booking.controller.dto.BookingResultDto;
import com.booking.errors.ErrorDto;
import com.booking.persistence.service.impl.Room;
import com.booking.persistence.service.impl.RoomSearchResult;
import com.booking.service.BookingManager;
import com.booking.service.exception.MaintenanceTimeOverlapException;
import com.booking.service.exception.NoRoomsAvailableException;
import com.booking.service.model.BookingRequest;
import com.booking.shared.model.Interval;
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
@RequestMapping("/v1/booking")
public class BookingController {
    private final BookingManager bookingManager;

    //Use mock mvc for controller tests
    @PostMapping("/reserve")
    public ResponseEntity<BookingResultDto> bookRoom(@RequestBody BookingRequestDto bookingRequestDto)
            throws MaintenanceTimeOverlapException, NoRoomsAvailableException {
        BookingRequest request = mapToRequest(bookingRequestDto);
        Room room = bookingManager.reserveRoom(request);
        return ResponseEntity.ok(mapToResponse(room));
    }

    private BookingRequest mapToRequest(BookingRequestDto bookingRequestDto) {
        return BookingRequest.builder()
                             .interval(Interval.builder()
                                               .startTimeInclusive(bookingRequestDto.startTime())
                                               .endTimeExclusive(bookingRequestDto.endTime())
                                               .build())
                             .noOfPersons(bookingRequestDto.noOfPersons())
                             .build();
    }

    private BookingResultDto mapToResponse(Room room) {
        return BookingResultDto.builder()
                               .size(room.size())
                               .name(room.name())
                               .build();
    }

    @GetMapping("/available")
    public ResponseEntity<?> fetchAvailableRooms(@RequestParam @FutureOrPresent LocalTime startTime,
                                                 @RequestParam @FutureOrPresent LocalTime endTime) throws NoRoomsAvailableException {

        Interval interval = Interval.builder()
                                    .startTimeInclusive(startTime)
                                    .endTimeExclusive(endTime)
                                    .build();
        RoomSearchResult roomSearchResult = bookingManager.fetchAvailableRooms(interval);

        if (roomSearchResult.reason() != null) {
            String message = switch (roomSearchResult.reason()) {
                case OVERLAP_WITH_MAINTENANCE_TIME -> "Maintenance timings are overlapped with Given Timings";
                case ALL_ROOMS_BOOKED -> "All rooms are booked for the Given timings";
            };
            return new ResponseEntity<>(new ErrorDto("BS-003", message), HttpStatus.BAD_REQUEST);
        }
        List<AvailableRoomsDto.Room> rooms = roomSearchResult.rooms()
                                                             .stream()
                                                             .map(room -> new AvailableRoomsDto.Room(room.name(), room.size()))
                                                             .toList();

        return ResponseEntity.ok(new AvailableRoomsDto(rooms));
    }


    @ExceptionHandler(value = NoRoomsAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorDto handleNoRoomsAvailableException(NoRoomsAvailableException ex) {
        log.error("Exception details are:", ex);
        return new ErrorDto("BKS-002", "No Rooms are Available for this no of persons");
    }


    @ExceptionHandler(value = MaintenanceTimeOverlapException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorDto handleMaintenanceTimingException(MaintenanceTimeOverlapException ex) {
        log.error("Exception details are:", ex);
        return new ErrorDto("BKS-001", "Maintenance Timings are Overlapped ");
    }

}
