package railway.simulator.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/*  */
public class StationTest {

    @Test
    public void testAddAndGetPassenger() {
        Station station = new Station(1);
        Passenger passenger = new Passenger(1, 'A', station, new Station(2), 10);
        station.addStationPassenger(passenger);

        List<Passenger> passengers = station.getStationPassengers();
        assertTrue(passengers.contains(passenger));
    }

}
