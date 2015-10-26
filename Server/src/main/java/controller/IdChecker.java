package controller;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Oleg on 11.09.2015.
 */
public class IdChecker implements Runnable {
    private static final Logger log = Logger.getLogger(IdChecker.class);
    private HashMap<Integer, Date> ids;

    public IdChecker(HashMap<Integer, Date> ids) {
        this.ids = ids;
    }

    @Override
    public void run() {
        for (int i = 0; i >= 0 ; i++) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.error("Threw a InterruptedException in MainClass::MyMethod:", e);
            }
            Date currentTime = new Date();
            for(HashMap.Entry<Integer, Date> entry: ids.entrySet()) {
                if (currentTime.getTime() - entry.getValue().getTime() >= 10000) {
                    ids.remove(entry.getKey());
                }
            }
        }
    }
}
