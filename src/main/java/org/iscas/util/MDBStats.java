package org.iscas.util;

/**
 * Created by andyren on 2016/6/28.
 */

public class MDBStats extends java.util.HashMap {
    
    //Singleton class
    private static MDBStats mdbStats = null;
    private MDBStats()
    {
    }
    
    public static synchronized MDBStats getInstance()
    {
        if (mdbStats == null)
            mdbStats = new MDBStats();
        return mdbStats;
    }
    
    public TimerStat addTiming(String type, long sendTime, long recvTime)
    {
        TimerStat stats = null;
        synchronized (type)
        {

            stats = (TimerStat) get(type);
            if (stats == null) stats = new TimerStat();

            long time =  recvTime - sendTime;                                
            if ( time > stats.getMax() ) stats.setMax(time);
            if ( time < stats.getMin() ) stats.setMin(time);
            stats.setCount(stats.getCount()+1);
            stats.setTotalTime(stats.getTotalTime() + time);
            
            put(type, stats);
        }
        return stats;
    }

    public synchronized void reset()
    {
        clear();
    }    

}
