package railway.simulator.model;

public class Passenger {
    private int id;
    private char type;
    private Station arrivalStation;
    private Station destinationStation;
    private int arrivalTime;

    public Passenger(int id, char type, Station arrivalStation, Station destinationStation, int arrivalTime) {
        this.id = id;
        this.type = type;
        this.arrivalStation = arrivalStation;
        this.destinationStation = destinationStation;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Passenger [id=" + id + ", type=" + type + ", arrivalStation=" + arrivalStation.getId() + ", destinationStation="
                + destinationStation.getId() + ", arrivalTime=" + arrivalTime + "]";
    }
    

}
