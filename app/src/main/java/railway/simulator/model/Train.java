package railway.simulator.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Train {
    private final int id;
    private final int capacity;
    private Station currentStation;
    private final Station endStation;
    private List<Passenger> passengers;
    private final int departureTime; // Begin time for this train.

    public Train(int id, int capacity, Station startStation, int departureTime, Station destionation) {
        this.id = id;
        this.capacity = capacity;
        this.currentStation = startStation; // train starts from the startStation which is current station
                                            // This is either 1 or numStations when the train is created first.
                                            // This will increment later, for each t=t+timebetweenstations.
        this.departureTime = departureTime; // Departure time from last station. Changes every station before
                                            // destination reached.
        this.endStation = destionation; // Final destination station for the train, either 1 or numStation, depending on
                                        // where it started.
        this.passengers = new ArrayList<>(); // Passengers currently onboard the train.
        boolean trainFwdFlag = currentStation.getId() - getEndStation().getId() < 0;

    }

    public void passengerBoardTheTrain(Passenger passenger) {

        // checks if there's capacity before loading
        if (passengers.size() < capacity / 2) {
            passengers.add(passenger);
            currentStation.getStationPassengers().remove(passenger);
        } else if (passengers.size() < capacity && passenger.getType() == 'A') {
            passengers.add(passenger);
            currentStation.getStationPassengers().remove(passenger);
        } else {
            // System.out.println("No space for additional passenger. Wait for next train.");
        }
    }

    public Station getEndStation() {
        return endStation;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    // public Station getNexStation() {
    // return nexStation;
    // }

    public void depart(Station nextStation, int timebetweenstations, Map stations) {
        // train departs from current station.
        // 1. Remove Onboarded Passengers from Train with current Station as
        // Destination.
        /*
         * List<Passenger> psgrOffboardList = passengers.stream().filter(p ->
         * p.getDestinationStation() == currentStation).toList();
         * passengers.removeAll(psgrOffboardList);
         * System.out.println("Passengers Offboarded from Train # : " + getId() +
         * " ---> " + psgrOffboardList);
         */
        // 2. BEGIN Onboarding Passengers.
        boolean trainFwd = currentStation.getId() - getEndStation().getId() < 0;
        List<Passenger> onboardPassengersList = new ArrayList<>();

        for (Passenger p : currentStation.getStationPassengers()) {
            boolean psgrFwd = currentStation.getId() - p.getDestinationStation().getId() < 0;
            // train and psgr in same direction
            if (trainFwd == psgrFwd && p.getArrivalTime() < departureTime + timebetweenstations) {
                // passengerBoardTheTrain(p);
                onboardPassengersList.add(p);
            }
        }

        // If two or more passengers attempt to board a train at the same time,

        // a. the passenger whose destination is more distant boards first
        Comparator<Passenger> compareDistanceToDestinationStation = Comparator
                .comparing(p1 -> Math.abs(currentStation.getId() - p1.getDestinationStation().getId()));

        // b. If there is a tie, type A passengers board first
        Comparator<Passenger> compareByPassengerType = Comparator.comparing(Passenger::getType);

        Comparator<Passenger> cmpratr = compareDistanceToDestinationStation.thenComparing(compareByPassengerType);
        onboardPassengersList = onboardPassengersList.stream().sorted(cmpratr).toList();

        for (int i = 0; i < onboardPassengersList.size(); i++) {
            passengerBoardTheTrain(onboardPassengersList.get(i));
        }

        // 2. END PASSENGER ONBOARDING

        // 3. Notify the current station that the train is departing
        currentStation.removeTrain(this);

    }

    // If end of line is reached for the train (train's endStation),
    public void arriveEndStationAndDisappear() {

        // passengers.offboard
        // Assume we unload all passengers at the current station
        // currentStation.getStationPassengers().addAll(passengers);
        // System.out.println("REACHED END STATION, DEPARTING ALL PASSENGERS --- " +
        // passengers.size());
        // System.out.println("\t" + passengers);
        passengers.clear();

    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void arriveAtAStation(Map<Integer, Station> stations) {
        if (currentStation == null) { // not departure station
            // System.out.println("\tTrain# : " + getId() + ", with Departure Time = " + departureTime
                    // + ", has arrived at station# : " + TrainUtility.nextStationForTrain(this, stations, departureTime));
            // currentStation = TrainUtility.nextStationForTrain(this, stations,
            // departureTime);
        } else { // First station the train departs from.
            // System.out.println("\tTrain# : " + getId() + ", with Departure Time = " + departureTime
                    // + ", arrived for starting at station# : " + currentStation);
        }

    }

    @Override
    public String toString() {
        return "Train [id=" + id + ", capacity=" + capacity + ", currentStation=" + currentStation + ", endStation="
                + endStation + ", passengers=" + passengers + ", departureTime=" + departureTime + "]";
    }

    public void setCurrentStation(Station nexStation) {
        currentStation = nexStation;
    }

}
