package org.example.sgc.strategy;

import java.util.List;

public class WeightedAverageStrategy implements AverageStrategy {
    @Override
    public double calculate(List<Double> grades) {
        if (grades == null || grades.isEmpty()) return 0.0;
        // Simulación: los últimos exámenes valen más (ej. 60% el último, 40% el resto)
        if (grades.size() == 1) return grades.get(0);
        
        double last = grades.get(grades.size() - 1);
        double sumOthers = 0;
        for (int i = 0; i < grades.size() - 1; i++) {
            sumOthers += grades.get(i);
        }
        double avgOthers = sumOthers / (grades.size() - 1);
        
        return (avgOthers * 0.4) + (last * 0.6);
    }
}
