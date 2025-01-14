This is the Booking - service Application , where users can book conference room for a given time period , on that
particular day .

There are two API's available in this service

1. /v1/conferenceroom/bookingEntity/available -- This is GET API which takes input as startTime and endTime in 24 Hour
   Format , the Response will be available Rooms
2. /v1/conferenceroom/bookingEntity/reserve -- This is a POST API which can be used to reserve a room based on startTime
   ,endTime and No of persons , the Suitable room will be booked based on availability

NOTE: The DB used is h2 in memory DB , we can access it through below URL in browser post starting the application
http://localhost:8080/h2-ui/

The Conference Room Data is loaded on application start up and new Rooms can be added to the table

Maintenance timings are configured using comma(,) seperator in application.yaml , new timings can be added.

Business Exceptions like Booking during Maintenance Times, Invalid Booking time and If no rooms are available during
given time/no of people, appropriate error responses are Given .