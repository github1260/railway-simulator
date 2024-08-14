package railway.simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Station {

    private int id;
    private List<Passenger> stationPassengers;
    private List<Train> trains;

    public Station(int id) {
        this.id = id;
        this.stationPassengers = new ArrayList<>();
        this.trains = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void addStationPassenger(Passenger passenger) {
        stationPassengers.add(passenger);
    }

    public void addStationPassengers(List<Passenger> passengers) {
        this.stationPassengers.addAll(passengers);
    }

    public List<Passenger> getStationPassengers() {
        return stationPassengers;
    }

    public void removeStationPassenger(Passenger passenger) {
        stationPassengers.remove(passenger);
    }

    public void addTrain(Train train) {
        trains.add(train);
    }

    public Train getNextTrain(int currentTime) {
        

        // Filter trains to find those that are scheduled to depart at or after the current time
        List<Train> upcomingTrains = trains.stream()
                .filter(train -> train.getDepartureTime() > currentTime)
                .sorted((t1, t2) -> Integer.compare(t1.getDepartureTime(), t2.getDepartureTime()))
                .collect(Collectors.toList());

        // Return the first train in the sorted list (earliest departure time)
        return upcomingTrains.isEmpty() ? null : upcomingTrains.get(0);
    }

    public void removeTrain(Train train) {
        trains.remove(train);
    }

    @Override
    public String toString() {
        return "Station [id=" + id + ", stationPassengers=" + stationPassengers.size() + ", trains=" + trains.size() + "]";
    }
    


    
}
