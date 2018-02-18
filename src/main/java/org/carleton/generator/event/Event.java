
package org.carleton.generator.event;


public class Event {

    public Integer patinet_id;
    public Integer sensor_id;
    public Integer uid;
    public Long time;
    public Integer value;

    public Event(Integer patinet_id, Integer sensor_id, Integer uid, Long time, Integer value) {
        this.patinet_id = patinet_id;
        this.sensor_id = sensor_id;
        this.uid = uid;
        this.time = time;
        this.value = value;
    }


    public Integer getPatinet_id() {
        return patinet_id;
    }

    public void setPatinet_id(Integer patinet_id) {
        this.patinet_id = patinet_id;
    }

    public Integer getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(Integer sensor_id) {
        this.sensor_id = sensor_id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (getPatinet_id() != null ? !getPatinet_id().equals(event.getPatinet_id()) : event.getPatinet_id() != null)
            return false;
        if (getSensor_id() != null ? !getSensor_id().equals(event.getSensor_id()) : event.getSensor_id() != null)
            return false;
        if (getUid() != null ? !getUid().equals(event.getUid()) : event.getUid() != null) return false;
        if (getTime() != null ? !getTime().equals(event.getTime()) : event.getTime() != null) return false;
        return getValue() != null ? getValue().equals(event.getValue()) : event.getValue() == null;
    }

    @Override
    public int hashCode() {
        int result = getPatinet_id() != null ? getPatinet_id().hashCode() : 0;
        result = 31 * result + (getSensor_id() != null ? getSensor_id().hashCode() : 0);
        result = 31 * result + (getUid() != null ? getUid().hashCode() : 0);
        result = 31 * result + (getTime() != null ? getTime().hashCode() : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "patinet_id=" + patinet_id +
                ", sensor_id=" + sensor_id +
                ", uid=" + uid +
                ", time=" + time +
                ", value=" + value;
    }

    //from string method
    public static Event fromstring(String line) {

        String[] token = line.split(",");


        Integer val1 = Integer.valueOf(token[0]);
        Integer val2 = Integer.valueOf(token[1]);
        Integer val3 = Integer.valueOf(token[2]);
        Long val4 = Long.valueOf(token[3]);
        Integer val5 = Integer.valueOf(token[4]);


        return new Event(val1, val2, val3, val4, val5);
    }


}

