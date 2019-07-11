package otus.homework;

import com.google.common.collect.Lists;

import java.util.List;


public class HelloOtus {
    public static void main(String[] args) {
        List<Double> temperatureList = Lists.newArrayList(36.5, 36.7, 35.9, 38.4, 39.0);
        for (Double temperature: temperatureList) {
            System.out.println(temperature);
        }
    }
}
