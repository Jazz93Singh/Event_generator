package org.carleton.generator.sensors;

public class start_sensors {

    public static void main(String[] args) throws Exception {

        sensor1 sensor1 = null;
        sensor2 sensor2 = null;
        sensor3 sensor3 = null;
        sensor4 sensor4 = null;


        Thread thread1 = new Thread() {

            @Override
            public void run() {
                try {

                    sensor1.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        Thread thread2 = new Thread() {

            @Override
            public void run() {
                try {
                    sensor2.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        Thread thread3 = new Thread() {

            @Override
            public void run() {
                try {
                    sensor3.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        Thread thread4 = new Thread() {

            @Override
            public void run() {
                try {
                    sensor4.main(null);
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
