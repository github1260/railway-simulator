package railway.simulator.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import railway.simulator.util.TrainUtility;

public class RailwaySystem {

    private Map<Integer, Station> stations; //1
    private List<Train> trains;
    private int travelTimeToNextStation; //2
    private int trainFrequency; //3
    private int trainCapacity; //4
    private int simulationStartTime; // Begin time, t=0.
    private int currentTime; // starts at t=0. increment in while loop, by travelTimeToNextStation;

    public RailwaySystem() {
        this.stations = new HashMap<>();
        this.trains = new ArrayList<>();
    }

    private void addStation(int id) {
        stations.put(id, new Station(id));
    }

    public Station getStation(int id) {
        return stations.get(id);
    }

    private void addTrain(Train train) {
        trains.add(train);
        train.getCurrentStation().addTrain(train); // Register train at its starting station
    }

    private boolean allPassengersEmpty() {
        // Implement logic to check if all passengers are processed

        long i = stations.values().stream().flatMap(station -> station.getStationPassengers().stream()).count();
        // System.out.println("PASSENGERS WAITING IN STATIONS ::: " + i);
        i += trains.stream().flatMap(train -> train.getPassengers().stream()).count();
        // System.out.println("PASSENGERS WAITING IN STATIONS + IN TRANSIT ON TRAINS ::: " + i);
        return i==0;
        // return stations.values().stream().allMatch(station -> station.getStationPassengers().isEmpty()) &&
        // trains.stream().allMatch(train -> train.getPassengers().isEmpty());
    }

    private void unloadPassengers(Train train) {
        Station destination = train.getCurrentStation();
        // Implement logic to unload passengers
        for (Passenger passenger : train.getPassengers()) {
            if (passenger.getDestinationStation().equals(destination)) {
                destination.addStationPassenger(passenger);
            }
        }
        // train.passengersDepartTheTrainIfDestinationReached();
    }

    public void readInputFile(String filePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            // System.out.println("INPUT FILE PATH :: " + filePath);

            String line = reader.readLine(); // First line 
            if (line != null) {
                String[] parts = line.split(" "); // The first line of input specifies the form of the railway in four integers.
                int numStations = Integer.parseInt(parts[0]); // The first is the number of stations,
                travelTimeToNextStation = Integer.parseInt(parts[1]); // the second is the time taken to travel from one station to the next
                trainFrequency = Integer.parseInt(parts[2]); // the third is the frequency that trains depart from the termini
                trainCapacity = Integer.parseInt(parts[3]); // the fourth is the capacity of trains

                // Number of stations created from reading the input.
                for (int i = 1; i <= numStations; i++) {
                    addStation(i);
                }

                // Subsequent lines define passengers, in the form of a letter that gives the type of the passenger followed by three integers.
                int passengerId = 0;
                while ((line = reader.readLine()) != null) {
                    String[] passengerData = line.split(" ");
                    char type = passengerData[0].charAt(0); // Passenger Type 'A' or 'B'
                    // The first is the time the passenger arrived.
                    int arrivalTime = Integer.parseInt(passengerData[1]); 
                     // the second is their destination station
                    int destinationIndex = Integer.parseInt(passengerData[2]); 
                    // the third is the station they appear at.
                    int arrivalIndex = Integer.parseInt(passengerData[3]); 

                    Station destination = getStation(destinationIndex);
                    Station arrivalStation = getStation(arrivalIndex);
                    int arrivalDateTime = arrivalTime;

                    Passenger passenger = new Passenger(++passengerId, type, arrivalStation, destination, arrivalDateTime);
                    arrivalStation.addStationPassenger(passenger);
                    // System.out.println("***************PASSENGER CREATED :::: " + passenger);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        } catch(Error er) {
            er.printStackTrace();
        }
    }



    // Trigger simulation
    public void simulateTravel() {
        currentTime = simulationStartTime; // t=0
        boolean simulationActive = true;
        // int totalTrains = 2;

        while (simulationActive) {
            simulationActive = false;

            // System.out.println("************* CURRENT TIME :: " + currentTime);
            // 1. Add new trains if frequency reached.
            if(currentTime % trainFrequency == 0) {
                trains.add(new Train(trains.size() + 1, trainCapacity, getStation(1), currentTime, getStation(stations.size())));
                trains.add(new Train(trains.size() + 1, trainCapacity, getStation(stations.size()), currentTime, getStation(1)));
                // System.out.println("\tTwo Trains added at both ends of the line :: " + trains);
            }

            // List All Passengers ready to board.
            List<Passenger> readyToBoardPassengers = stations.values().stream().flatMap(s -> s.getStationPassengers().stream()).filter(p -> p.getArrivalTime() <= currentTime).toList();
            // System.out.println("\t# of PASSENGERS ARRIVED AT STATIONS WAITING TO BOARD :: " + readyToBoardPassengers.size());
            // System.out.println("\tThey are :: " + readyToBoardPassengers);

            // Remove Trains that already reached destination.
            // removeTrainsAtDestination();
            // List<Train> trainsAtDestination = new ArrayList<>();

            /* for (Train train : trains) {
                
                if (((currentTime - train.getDepartureTime()) % travelTimeToNextStation == 0) 
                        // && (((currentTime - train.getDepartureTime()) / travelTimeToNextStation) > 0)
                        ) {
                    train.arriveAtAStation(stations); // 
                    if(train.getCurrentStation().getId() == train.getEndStation().getId()) {
                        // trains.remove(train);
                        System.out.println("***REMOVE TRAIN ID :: " + train.getId());
                        trainsAtDestination.add(train);
                        train.arriveEndStationAndDisappear();
                    }
                }
            }

            trains.removeAll(trainsAtDestination);
            System.out.println("***" + trainsAtDestination.size() + " trains have reached end Stations and terminated.");
             */
            for (Train train: trains) {
                // System.out.println("DEPARTING TRAIN # : " + train.getId() + "\n\tDepartureTime : " + train.getDepartureTime() + "\n\tEndStation : " + train.getEndStation().getId());
                if (((currentTime - train.getDepartureTime()) % travelTimeToNextStation == 0)
                    // && (((currentTime - train.getDepartureTime()) / travelTimeToNextStation) > 0)
                    ) {
                    // System.out.println("\tCurrentStation : " + train.getCurrentStation().getId() );
                    // int nextStationId = train.getCurrentStation().getId() < train.getEndStation().getId() 
                    //                     ? train.getCurrentStation().getId() + 1
                    //                     : train.getCurrentStation().getId() - 1;
                    
                    // Station nextStation = TrainUtility.nextStationForTrain(train, stations, currentTime);

                    // train.depart(getStation(nextStationId), travelTimeToNextStation, stations); // Depending on UP or Down.
                    TrainUtility.depart(train, stations, currentTime, travelTimeToNextStation);
                }
                
            }

            // Cleanup trains that arrived at destination.
            Iterator<Train> trainIter = trains.iterator();
            while(trainIter.hasNext()) {
                Train t = trainIter.next();
                if(t.getCurrentStation() == null) {
                    // System.out.println("\tTrain# : " + t.getId() + " has reached its destination station# : " 
                                            // + t.getEndStation().getId() + " at time t=" + currentTime);
                    trainIter.remove();
                }
            }

            if (!allPassengersEmpty()) {
                simulationActive = true;
                currentTime++;
                // System.out.println("PASSENGERS NOT REACHED DESTINATION YET!!!");
            }
            
        }

        // --currentTime;
        System.out.println("Finished at: t=" + currentTime + " minutes");
    }

    private void removeTrain(Train train) {

        trains.remove(train);
    }

    private void removeTrainsAtDestination() {
        Iterator<Train> trainIter = trains.iterator();
            while(trainIter.hasNext()) {
                Train t = trainIter.next();
                if(t.getCurrentStation()!=null) {
                    if(t.getCurrentStation().getId() == t.getEndStation().getId()) {
                        trainIter.remove();
                    }
                }
            }
    }

    public Map<Integer, Station> getStations() {
        return stations;
    }

    public List<Train> getTrains() {
        return trains;
    }

    public Train getTrainById(int id) {
        return trains.stream().filter(t -> t.getId() == id).findAny().get();
    }

}
