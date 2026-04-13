package org.example.sgc.strategy;

import java.util.List;

public class SimpleAverageStrategy implements AverageStrategy {
    @Override
    public double calculate(List<Double> grades) {
        if (grades == null || grades.isEmpty()) return 0.0;
        double sum = 0;
        for (double g : grades) sum += g;
        return sum / grades.size();
    }
}
