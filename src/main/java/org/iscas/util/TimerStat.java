package org.iscas.util;

/**
 * Created by andyren on 2016/6/28.
 */

public class TimerStat {

        private double min=1000000000.0, max=0.0, totalTime=0.0;
        private int count;

        public int getCount() {
            return count;
        }


        public double getMax() {
            return max;
        }


        public double getMin() {
            return min;
        }


        public void setCount(int count) {
            this.count = count;
        }


        public void setMax(double max) {
            this.max = max;
        }


        public void setMin(double min) {
            this.min = min;
        }


        public double getTotalTime() {
            return totalTime;
        }


        public void setTotalTime(double totalTime) {
            this.totalTime = totalTime;
        }


        public double getMaxSecs() {
            return max/1000.0;
        }


        public double getMinSecs() {
            return min/1000.0;
        }

        public double getAvgSecs() {
            
            double avg =  (double)getTotalTime() / (double)getCount();
            return avg / 1000.0;
        }
        

}
