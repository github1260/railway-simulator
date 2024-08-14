package railway.simulator.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/*  */
public class RailwaySystemTest {

      private RailwaySystem system;
    private File tempFile;

    /**
     * @throws IOException
     */
    @BeforeEach
    public void setUp() throws IOException {
        system = new RailwaySystem();
        // Create a temporary file for testing
        tempFile = Files.createTempFile("railway_test_input", ".txt").toFile();
        // System.out.println()
        // tempFile.deleteOnExit(); // Ensure the file is deleted after the test
    }
    
    @Test
    public void testReadInputFile() throws IOException {
        RailwaySystem system = new RailwaySystem();
        system.readInputFile("src/test/resources/Example1.txt");
        
         // Write sample data to the temporary file
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("2 2 2 2\n");  // Number of stations, travel time, train frequency, train capacity
            writer.write("B 1 1 2\n");  // Passenger type A, arrival time 1, from station 1, to station 2
        }


        // Read the input file
        system.readInputFile(tempFile.getAbsolutePath());

        // System.out.println(system.getTrainById(1));
        // System.out.println(system.getStation(1));
        // Add assertions to check that the system has been initialized correctly

        // Verify two Trains created.
        // assertNotNull(system.getTrainById(1));
        // assertNotNull(system.getTrainById(2));

        // Verify stations are created
        assertNotNull(system.getStation(1));
        assertNotNull(system.getStation(2));

        // Verify passengers created.
        List<Passenger> allPassengers = new ArrayList<>();
        for (Station stn: system.getStations().values()) {
            allPassengers.addAll(stn.getStationPassengers());
        }
        // System.out.println("passengers :: " + allPassengers.size());
        assertFalse(allPassengers.isEmpty());

    }

}

