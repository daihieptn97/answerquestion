package com.k14bktpm.daihieptn97.answerquestions;

import java.io.IOException;


/**
 * Created by Hiep Tran on 10/15/2017.
 */

public class ConnectInternet {
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
