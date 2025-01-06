package com.booking.integrations.booking.impl;

import com.booking.integrations.booking.service.BookingService;
import com.booking.integrations.booking.service.model.FindBookingCommand;
import com.booking.integrations.booking.service.model.BookingCommand;
import com.booking.integrations.booking.impl.persistence.entity.BookingEntity;
import com.booking.integrations.booking.impl.persistence.repository.BookingRepository;
import com.booking.integrations.booking.service.exception.AllRoomsAreBookedException;
import com.booking.integrations.booking.service.model.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@RequiredArgsConstructor

public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> findBookings(FindBookingCommand findBookingCommand) throws AllRoomsAreBookedException {
        List<BookingEntity> bookingEntityList = bookingRepository.findBookings(findBookingCommand.startTime(),
                                                                               findBookingCommand.endTime());

        if (CollectionUtils.isEmpty(bookingEntityList)) {
            throw new AllRoomsAreBookedException("No rooms available for given Start and end time ");
        }
        return mapEntityToDto(bookingEntityList);
    }

    private List<Booking> mapEntityToDto(List<BookingEntity> bookingEntityList) {
        return bookingEntityList.stream()
                                .map(bookingEntity -> new Booking(bookingEntity.getRoomId()))
                                .toList();
    }

    @Override
    public void saveBooking(BookingCommand bookingCommand) {
        BookingEntity bookingEntity = mapToEntity(bookingCommand);
        bookingRepository.save(bookingEntity);
    }

    private BookingEntity mapToEntity(BookingCommand bookingCommand) {
        return new BookingEntity(bookingCommand.roomId(),
                                 bookingCommand.interval().startTimeInclusive(),
                                 bookingCommand.interval().endTimeExclusive(),
                                 bookingCommand.noOfPersons());
    }
}
