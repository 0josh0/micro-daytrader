package org.iscas.util;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Summer on 2016/11/16.
 */
public class TradeConfig {

    private static int MAX_QUOTES = 1000;//最大股票数
   // private static Random r0 = new Random(System.currentTimeMillis());
    private static Random randomNumberGenerator = new Random(System.currentTimeMillis());//生成随机数double

    //生成double随机数
    public static double random() {
        return randomNumberGenerator.nextDouble();
    }
    //生成int随机数
    public static int rndInt(int i) {
        return (new Float(random() * i)).intValue();
    }
    //生成Float随机数
    public static float rndFloat(int i) {
        return (new Float(random() * i)).floatValue();
    }
    //生成BigDecimal随机数
    public static BigDecimal rndBigDecimal(float f) {
        return (new BigDecimal(random() * f)).setScale(
                2,
                BigDecimal.ROUND_HALF_UP);
    }
    //生成随机股票代码，s:10
    public static String rndSymbol() {
        return "s:" + rndInt(MAX_QUOTES - 1);
    }
    //生成随机数量
    public static float rndQuantity() {
        return ((new Integer(rndInt(200))).floatValue()) + 1.0f;
    }
}
