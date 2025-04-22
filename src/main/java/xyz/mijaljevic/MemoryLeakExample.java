/**
 * Copyright (C) 2025 Karlo Mijaljević
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package xyz.mijaljevic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Memory leak example created to test out VisualVM and practice memory leak
 * detection.
 */
public final class MemoryLeakExample {
    /**
     * The main leak perpetrator. A static list that will get a new element
     * on a steady interval.
     */
    private static final List<String> LEAK = new ArrayList<>();

    /**
     * Format for simple logging timestamps. Only hours, minutes and seconds.
     */
    private static final DateTimeFormatter LOG_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Main method. Ignores command line arguments.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {
        info("Starting MemoryLeakExample");

        // Calculate List (object) size
        long size = InstrumentationAgent.calculateObjectSize(LEAK);

        info("Size of leak without elements (only list): " + size);

        // Define a human readable value
        String value;

        while (true) {
            String newElement = "VERY STRING. MUCH INFORMATION.";

            LEAK.add(newElement);

            // Add new String (object) size to total calculation
            size += InstrumentationAgent.calculateObjectSize(newElement);

            // Format human readable value
            if (size > 1000000) {
                value = (size / 1000000) + "MB" + " and " + (size % 1000000) + "B";
            } else {
                value = size + "B";
            }

            // Keep the console cleaner
            if (LEAK.size() % 1000 == 0) {
                info("Size of leak is " + value + " with " + LEAK.size() + " elements");
            }

            // This would be a little bit over 2GB of size
            if (size > Integer.MAX_VALUE) {
                info("Size of leak is over " + Integer.MAX_VALUE + " which is the Integer MAX_VALUE!");
                break;
            }

            try {
                Thread.sleep(Duration.ofMillis(10));
            } catch (InterruptedException e) {
                error("MemoryLeakExample interrupted! Error: " + e.getMessage());
            }
        }

        info("Ending MemoryLeakExample");
    }

    /**
     * Logs info message using <code>System.out</code>.
     *
     * @param message {@link String} message to log.
     */
    private static void info(String message) {
        System.out.println("[" + timestamp() + "] " + message);
    }

    /**
     * Logs error message using <code>System.err</code>.
     *
     * @param message {@link String} message to log.
     */
    private static void error(String message) {
        System.err.println("[" + timestamp() + "] " + message);
    }

    /**
     * @return A {@link String} timestamp using {@link LocalDateTime} and the
     * <i>LOG_FORMAT</i>
     */
    private static String timestamp() {
        return LocalDateTime.now().format(LOG_FORMAT);
    }
}
