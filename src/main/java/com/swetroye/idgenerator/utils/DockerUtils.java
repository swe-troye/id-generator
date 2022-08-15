package com.swetroye.idgenerator.utils;

public class DockerUtils {

    private static String podUid = "";
    private static boolean isDocker;

    static {
        initInfo();
    }

    /**
     * Get pod UID
     * 
     * @return empty string if it's no a docker
     */
    public static String getPodUid() {
        return podUid;
    }

    /**
     * If it's a docker or not.
     */
    public static boolean isDocker() {
        return isDocker;
    }

    private static void initInfo() {
        // Get pod Uid from enviroment
        String podUid = System.getenv("POD_UID");

        if (podUid != null && !podUid.isBlank()) {
            isDocker = true;
        } else {
            isDocker = false;
        }

    }
}