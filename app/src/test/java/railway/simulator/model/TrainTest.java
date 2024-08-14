package railway.simulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
/*  */
public class TrainTest {

    @Test
    public void testLoadPassenger() {
        Station station = new Station(1);
        Station destinationStation = new Station(2);
        Train train = new Train(1, 2, station, 9, destinationStation);
        Passenger passenger = new Passenger(1, 'A', station, destinationStation, 10);

        train.passengerBoardTheTrain(passenger);
        assertEquals(1, train.getPassengers().size());
        assertTrue(train.getPassengers().contains(passenger));
    }

}
