package railway.simulator.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import railway.simulator.model.Passenger;
import railway.simulator.model.Station;
import railway.simulator.model.Train;

public class TrainUtility {

    public static void depart(Train train, Map<Integer, Station> stations, int currentTime, int timebetweenstations) {

        Station nexStation = nextStationForTrain(train, stations, currentTime);
        if (nexStation == null) { // reached end station.
            // Offload All Passengers from train
            train.arriveEndStationAndDisappear();
        } else {
            if (currentTime > train.getDepartureTime()) {
                // 1. OffloadPassengers At CurrentStation
                List<Passenger> psgrOffboardList = train.getPassengers().stream()
                        .filter(p -> p.getDestinationStation() == train.getCurrentStation()).toList();
                train.getPassengers().removeAll(psgrOffboardList);
                // System.out
                //         .println("Passengers Offboarded from Train # : " + train.getId() + " ---> " + psgrOffboardList);
            }

            // 2. Onboard Passengers at CurrentStation.
            boolean trainFwd = train.getCurrentStation().getId() - train.getEndStation().getId() < 0;
            // System.out.println("\t #### TRAIN # " + train.getId() + " IS MOVING FORWARD ??? " + trainFwd);
            List<Passenger> onboardPassengersList = new ArrayList<>();

            for (Passenger p : train.getCurrentStation().getStationPassengers()) {
                boolean psgrFwd = train.getCurrentStation().getId() - p.getDestinationStation().getId() < 0;
                // train and psgr in same direction
                
                if ((trainFwd == psgrFwd) && (p.getArrivalTime() <= currentTime)) {
                    // passengerBoardTheTrain(p);
                    onboardPassengersList.add(p);
                    // System.out.println("\tPASSENGER # : " + p.getId() + " BOARDING ONTO TRAIN # : " + train.getId());
                }
            }

            // If two or more passengers attempt to board a train at the same time,
            // if(!onboardPassengersList.isEmpty() && onboardPassengersList.size() > 1) {
            // the passenger whose destination is more distant boards first
            Comparator<Passenger> compareDistanceToDestinationStation = Comparator
                    .comparing(p1 -> Math.abs(train.getCurrentStation().getId() - p1.getDestinationStation().getId()));

            // If there is a tie, type A passengers board first
            Comparator<Passenger> compareByPassengerType = Comparator.comparing(Passenger::getType);

            Comparator<Passenger> cmpratr = compareDistanceToDestinationStation.thenComparing(compareByPassengerType);
            onboardPassengersList = onboardPassengersList.stream().sorted(cmpratr).toList();
            

            for (int i = 0; i < onboardPassengersList.size(); i++) {
                train.passengerBoardTheTrain(onboardPassengersList.get(i));
            }

        }

        // 3. currentStation = nextStation.
        train.setCurrentStation(nexStation);

    }

    public static Station nextStationForTrain(Train t, Map<Integer, Station> stations, int currentTime) {
        if (t.getEndStation().getId() == stations.size())
            return stations.get(t.getCurrentStation().getId() + 1);

        return stations.get(t.getCurrentStation().getId() - 1);
    }

    public void arrive() {

    }

}
