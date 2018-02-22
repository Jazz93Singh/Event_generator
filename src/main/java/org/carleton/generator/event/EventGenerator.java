package org.carleton.generator.event;


import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.time.ZonedDateTime;
import java.util.Random;
import java.sql.Timestamp;


public class EventGenerator extends RichParallelSourceFunction<Event> {

    Integer InputRate;  // events/second
    Integer Sleeptime;
    Integer NumberOfEvents;
    Integer upper_bound;
    Integer lower_bound;
    Long Delay = 3000L;

    Integer patient_id;
    Integer sensor_id;
    Integer time_in_sec;


    public EventGenerator(Integer inputRate, Integer time_in_sec, Integer patient_id, Integer sensor_id, Integer lower_bound, Integer upper_bound) {
        this.InputRate = inputRate;
        Sleeptime = 1000 / InputRate;
        NumberOfEvents = inputRate * time_in_sec;

        this.patient_id = patient_id;
        this.sensor_id = sensor_id;
        this.upper_bound = upper_bound;
        this.lower_bound = lower_bound;
        this.time_in_sec = time_in_sec;
    }

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {

//        Thread.sleep(Delay);

        Long currentTime;
        Integer Value;
        Integer Sensor_id;
        Integer Patient_id;
        Integer uid = 0;



        Long start_time =  System.currentTimeMillis();
        Long end_time = start_time + (1000 * time_in_sec);


        while (System.currentTimeMillis() < end_time ){

            uid = uid + 1;

            Random random = new Random(System.currentTimeMillis());

            Patient_id = patient_id;
            Sensor_id = sensor_id;


            currentTime = System.currentTimeMillis();

            // int randomNum = rand.nextInt((max - min) + 1) + min;
            Value = lower_bound + random.nextInt((upper_bound - lower_bound) + 1);


            // Event(Integer patinet_id, Integer sensor_id, Integer uid, Long time, Integer value)

            Event event = new Event(Patient_id, Sensor_id, uid, currentTime, Value);


            synchronized (sourceContext.getCheckpointLock()) {
                sourceContext.collect(event);

            }

            Thread.sleep(Sleeptime);



        }


    }

    @Override
    public void cancel() {

    }
}
