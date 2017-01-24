package com.home.nam.iotcontrol;

/**
 * Created by Nam on 1/16/2017.
 */

import java.io.Serializable;

public class Bean implements Serializable
{
    private static final long serialVersionUID = -5435670920302756945L;
    private String cmdOn;
    private String cmdOff;
    private String name;

    public String getCmdON() {
        return cmdOn;
    }

    public void setCmdON(String data_on) {
        this.cmdOn = data_on;
    }

    public String getCmdOFF() {
        return cmdOff;
    }

    public void setCmdOFF(String data_off) {
        this.cmdOff = data_off;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
