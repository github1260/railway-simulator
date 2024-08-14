package railway.simulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PassengerTest {

     @Test
    public void testPassengerCreation() {
        Station arrivalStation = new Station(1);
        Station destinationStation = new Station(2);
        Integer arrivalTime = 10;
        Passenger passenger = new Passenger(1, 'A', arrivalStation, destinationStation, arrivalTime);

        assertEquals(1, passenger.getId());
        assertEquals(arrivalStation, passenger.getArrivalStation());
        assertEquals(destinationStation, passenger.getDestinationStation());
        assertEquals(arrivalTime, passenger.getArrivalTime());
    }

}
