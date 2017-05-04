package org.iscas.util;

//import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Collection;
import java.util.Iterator;


public class Log {
   // private final static org.apache.commons.logging.Log log = LogFactory.getLog(Log.class);
    private final static Logger log = LogManager.getLogger("Log");

    public static void log(String message) {
        //log.debug("DayTrader Log:" + new java.util.Date() + "------\n\t ");
        //log.debug(message);
        System.out.println();
        log.info("ServiceInvoke-Tag————  "+ new java.util.Date() +"  μDaytrader  "+"  "+message+ "  account  ServiceInvoke-Tag————\n\t ");
      //  log.trace("DayTrader Log: " +message+"  "+ new java.util.Date() + "---------------\n\t ");
    }
//    public static void log(String ServiceName,String MethodName) {
//        //log.debug("DayTrader Log:" + new java.util.Date() + "------\n\t ");
//        //log.debug(message);
//        //System.out.println();
//        log.info("DayTrader Log: "
//                + new java.util.Date()
//                + ServiceName +"  "
//                + MethodName
//                + "---------------\n\t ");
//    }

    public static void log(String msg1, String msg2) {
        log(msg1 + msg2);
    }

    public static void log(String msg1, String msg2, String msg3) {
        log(msg1 + msg2 + msg3);
    }

    public static void error(String message) {
        message = "Error: " + message;
        log.error(message);
    }

    public static void error(String message, Throwable e) {
        error(message + "\n\t" + e.toString());
        e.printStackTrace(System.out);
    }

    public static void error(String msg1, String msg2, Throwable e) {
        error(msg1 + "\n" + msg2 + "\n\t", e);
    }

    public static void error(String msg1, String msg2, String msg3, Throwable e) {
        error(msg1 + "\n" + msg2 + "\n" + msg3 + "\n\t", e);
    }

    public static void error(Throwable e, String message) {
        error(message + "\n\t", e);
        e.printStackTrace(System.out);
    }

    public static void error(Throwable e, String msg1, String msg2) {
        error(msg1 + "\n" + msg2 + "\n\t", e);
    }

    public static void error(Throwable e, String msg1, String msg2, String msg3) {
        error(msg1 + "\n" + msg2 + "\n" + msg3 + "\n\t", e);
    }


    public static void trace(String message) {
        log.trace(message + " threadID=" + Thread.currentThread());
    }

    public static void trace(String message, Object parm1) {
        trace(message + "(" + parm1 + ")");
    }

    public static void trace(String message, Object parm1, Object parm2) {
        trace(message + "(" + parm1 + ", " + parm2 + ")");
    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3) {
        trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")");
    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4) {
        trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")" + ", " + parm4);
    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4, Object parm5) {
        trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")" + ", " + parm4 + ", " + parm5);
    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4,
                             Object parm5, Object parm6) {
        trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")" + ", " + parm4 + ", " + parm5 + ", " + parm6);
    }

    public static void trace(String message, Object parm1, Object parm2, Object parm3, Object parm4,
                             Object parm5, Object parm6, Object parm7) {
        trace(message + "(" + parm1 + ", " + parm2 + ", " + parm3 + ")" + ", " + parm4 + ", " + parm5 + ", " + parm6 + ", " + parm7);
    }

    public static void traceEnter(String message) {
        log.trace("Method enter --" + message);
    }

    public static void traceExit(String message) {
        log.trace("Method exit  --" + message);
    }


    public static void stat(String message) {
        log(message);
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void print(String message) {
        log(message);
    }

    public static void printObject(Object o) {
        log("\t" + o.toString());
    }

    public static void printCollection(Collection c) {
        log("\t---Log.printCollection -- collection size=" + c.size());
        Iterator it = c.iterator();
        while (it.hasNext()) {
            log("\t\t" + it.next().toString());
        }
        log("\t---Log.printCollection -- complete");
    }

    public static void printCollection(String message, Collection c) {
        log(message);
        printCollection(c);
    }

    public static boolean doActionTrace() {
        return getTrace() || getActionTrace();
    }

    public static boolean doTrace() {
        return getTrace();
    }

    public static boolean doDebug() {
        return true;
    }

    public static boolean doStat() {
        return true;
    }


    public static boolean getTrace() {
        return TradeConfig.getTrace();
    }


    public static boolean getActionTrace() {
        return TradeConfig.getActionTrace();
    }


    public static void setTrace(boolean traceValue) {
        TradeConfig.setTrace(traceValue);
    }


    public static void setActionTrace(boolean traceValue) {
        TradeConfig.setActionTrace(traceValue);
    }
}

