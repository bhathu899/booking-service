package com.booking.persistence.service.impl;

import com.booking.persistence.BookingCommand;
import com.booking.persistence.FindBookingCommand;
import com.booking.persistence.entity.BookingEntity;
import com.booking.persistence.repository.BookingRepository;
import com.booking.persistence.service.BookingService;
import com.booking.service.exception.NoRoomsAvailableException;
import com.booking.service.model.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.sql.Time;
import java.util.List;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@RequiredArgsConstructor

public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> findBookings(FindBookingCommand findBookingCommand) throws NoRoomsAvailableException {
        List<BookingEntity> bookingEntityList = bookingRepository.findBookings(Time.valueOf(findBookingCommand.startTime()),
                                                                               Time.valueOf(findBookingCommand.endTime()));

        if (CollectionUtils.isEmpty(bookingEntityList)) {
            throw new NoRoomsAvailableException("No rooms available for given Start and end time ");
        }
        return mapEntityToDto(bookingEntityList);
    }

    private List<Booking> mapEntityToDto(List<BookingEntity> bookingEntityList) {
        return bookingEntityList.stream()
                                .map(bookingEntity -> new com.booking.service.model.Booking(bookingEntity.getRoomId()))
                                .toList();
    }

    @Override
    public void saveBooking(BookingCommand bookingCommand) {
        BookingEntity bookingEntity = mapToEntity(bookingCommand);
        bookingRepository.save(bookingEntity);
    }

    private BookingEntity mapToEntity(BookingCommand bookingCommand) {

        return new BookingEntity(bookingCommand.interval()
                                               .startTimeInclusive(), bookingCommand.interval()
                                                                                    .endTimeExclusive(), bookingCommand.roomId(),
                                 bookingCommand.noOfPersons());
    }
}
