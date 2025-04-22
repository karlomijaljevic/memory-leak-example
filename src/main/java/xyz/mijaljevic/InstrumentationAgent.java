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

import java.lang.instrument.Instrumentation;

/**
 * Initializes a Java {@link Instrumentation} instance to calculate object
 * sizes.
 */
public final class InstrumentationAgent {
    /**
     * Single {@link Instrumentation} instance.
     */
    private static volatile Instrumentation instance;

    /**
     * Agents main method as required by the specification.
     *
     * @param agentArgs {@link Instrumentation} arguments.
     * @param inst      {@link Instrumentation} instance
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        instance = inst;
    }

    /**
     * Calculates the size (in bytes) of the provided object.
     *
     * @param object Object whose size needs to be calculated
     * @return The size of the provided object in bytes
     */
    public static long calculateObjectSize(final Object object) {
        if (instance == null) {
            throw new IllegalStateException("Agent not initialized.");
        }

        return instance.getObjectSize(object);
    }
}