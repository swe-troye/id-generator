package com.swetroye.idgenerator.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetUtils {
    private static InetAddress intentAddress;

    static {
        try {
            intentAddress = getLocalInetAddress();
        } catch (SocketException e) {
            throw new RuntimeException("Cannot get local IP.");
        }
    }

    private static InetAddress getLocalInetAddress() throws SocketException {

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (networkInterface.isLoopback()) {
                continue;
            }

            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress address = inetAddresses.nextElement();

                // ignores all invalidated addresses
                if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                    continue;
                }

                return address;
            }
        }

        throw new RuntimeException("No validated local address!");
    }

    /**
     * Retrieve local address
     * 
     * @return local address
     */
    public static String getLocalAddress() {
        return intentAddress.getHostAddress();
    }
}
