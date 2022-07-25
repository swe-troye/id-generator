package com.swetroye.idgenerator.utils;

public class DockerUtils {

    private static String hostname = "";
    private static String port = "";
    private static boolean isDocker;

    static {
        initInfo();
    }

    /**
     * Get docker hostname
     * 
     * @return empty string if it's no a docker
     */
    public static String getHostname() {
        return hostname;
    }

    /**
     * Get docker port
     * 
     * @return empty string if it's no a docker
     */
    public static String getPort() {
        return port;
    }

    /**
     * If it's a docker or not.
     */
    public static boolean isDocker() {
        return isDocker;
    }

    private static void initInfo() {
        // Get hostname & port from enviroment
        // **********Tmp fake data - Start**********
        hostname = "127.0.0.1";
        port = "1234";
        // **********Tmp fake data - End**********

        boolean hasHostname = !hostname.isBlank();
        boolean hasPort = !port.isBlank();

        if (hasHostname && hasPort) {
            isDocker = true;
        } else if (!hasHostname && !hasPort) {
            isDocker = false;
        } else {
            throw new RuntimeException(
                    "Missing host or port from env for Docker. host:" + hostname + ", port:" + port);
        }

    }
}