package org.carleton.generator.event;


import java.util.Random;


public class EventGenerator_simple {

    Integer InputRate;  // events/second
    Integer Sleeptime;
    Integer NumberOfEvents;
    Integer upper_bound;
    Integer lower_bound;
    Integer patient_id;
    Integer sensor_id;
    Integer uid;


    public EventGenerator_simple(Integer inputRate, Integer time_in_sec, Integer patient_id, Integer sensor_id, Integer lower_bound, Integer upper_bound) throws InterruptedException {
        this.InputRate = inputRate;
        Sleeptime = 1000 / InputRate;
        NumberOfEvents = inputRate * time_in_sec;

        this.patient_id = patient_id;
        this.sensor_id = sensor_id;
        this.upper_bound = upper_bound;
        this.lower_bound = lower_bound;
    }



    Long currentTime;
    Integer Value;
    Integer Sensor_id;
    Integer Uid;
    Integer Patient_id;



     void run() throws InterruptedException {


        for (Integer i = 1; i <= NumberOfEvents; i++)

        {

            Random random = new Random(System.currentTimeMillis());

            Patient_id = patient_id;
            Sensor_id = sensor_id;
            Uid = i;

            currentTime = System.currentTimeMillis();

            // int randomNum = rand.nextInt((max - min) + 1) + min;
            Value = lower_bound + random.nextInt((upper_bound - lower_bound) + 1);


            // Event(Integer patinet_id, Integer sensor_id, Integer uid, Long time, Integer value)

            Event event = new Event(Patient_id, Sensor_id, Uid, currentTime, Value);



            Thread.sleep(Sleeptime);


        }//for

    } //    public Event run() throws InterruptedException {



}
