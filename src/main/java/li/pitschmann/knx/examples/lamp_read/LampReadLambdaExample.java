/*
 * Copyright (C) 2022 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package li.pitschmann.knx.examples.lamp_read;

import li.pitschmann.knx.core.address.GroupAddress;
import li.pitschmann.knx.core.communication.DefaultKnxClient;
import li.pitschmann.knx.core.datapoint.DPT1;
import li.pitschmann.knx.core.exceptions.KnxException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Demo class how to send a read request to a KNX group address.
 *
 * @author PITSCHR
 */
public final class LampReadLambdaExample {
    public static void main(final String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        // this is the group address where the KNX actuator read the status of lamp
        final var readGroupAddress = GroupAddress.of(1, 2, 113);

        // create KNX client and connect to KNX Net/IP device using auto-discovery
        try (final var client = DefaultKnxClient.createStarted()) {
            // send a 'read' request to KNX
            client.readRequest(readGroupAddress)
                    // KNX actuator will send a response to the KNX client with actual lamp status
                    .thenApply(ok -> {
                        if (ok) {
                            return client.getStatusPool().getValue(readGroupAddress, DPT1.SWITCH).getValue();
                        } else {
                            throw new KnxException("Could not read status from lamp");
                        }
                    })
                    // lamp status will be inverted (on -> off / off -> on)
                    .thenAccept(isLampOn -> {
                                if (isLampOn) {
                                    System.out.println("Yes, lamp is on!");
                                } else {
                                    System.out.println("No, lamp is off!");
                                }
                            }
                    )
                    // wait max. 10 sec for an acknowledgment
                    .get(10, TimeUnit.SECONDS);
        }

        // auto-closed and disconnected by KNX client
    }
}
