package pkg;

import org.apache.logging.log4j.core.util.SystemClock;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        long nano_startTime = System.nanoTime(); // for elapsed time or performance measurement
        long millis_startTime = System.currentTimeMillis(); // real time in milliseconds since epoch

        System.out.println("Nano Time: " + nano_startTime);  // Just for performance measurement

        // Formatting the millis to a readable date and time
        Instant instant = Instant.ofEpochMilli(millis_startTime);  // Convert to Instant
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault()); // Formatting with time zone
        String formattedTime = formatter.format(instant);  // Format the Instant to a readable string

        System.out.println("Millis Time (in epoch): " + millis_startTime);
        System.out.println("Formatted Date/Time: " + formattedTime);  // Human-readable date and time
    }
}
