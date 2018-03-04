package org.carleton.generator.sensors;

public class start_sensors {

    public static void main(String[] args) throws Exception {

        ecg_sensor ecg_sensor = null;
        bp_sensor bp_sensor = null;
        respiration_sensor respiration_sensor = null;
        eeg_sensor eeg_sensor = null;


        Thread thread1 = new Thread() {

            @Override
            public void run() {
                try {

                    ecg_sensor.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        Thread thread2 = new Thread() {

            @Override
            public void run() {
                try {
                    bp_sensor.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        Thread thread3 = new Thread() {

            @Override
            public void run() {
                try {
                    respiration_sensor.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        Thread thread4 = new Thread() {

            @Override
            public void run() {
                try {
                    eeg_sensor.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };





        thread1.start();
        thread2.start();
        thread3.start();
//        thread4.start();


    } //main
} //class
