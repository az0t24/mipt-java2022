package ru.khairullin.ar.data;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DrawDiagram extends JFrame {

    public DrawDiagram(Map<String, Integer> data) {
        initUI(data);
    }

    private void initUI(Map<String, Integer> data) {
        DefaultPieDataset dataset = createDataset(data);

        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Pie chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private DefaultPieDataset createDataset(Map<String, Integer> data) {
        var dataset = new DefaultPieDataset();

        for (Object key : data.keySet()) {
            dataset.setValue((Comparable) key, data.get(key));
        }

        return dataset;
    }

    private JFreeChart createChart(DefaultPieDataset dataset) {
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Web servers market share",
                dataset,
                false, true, false);

        return pieChart;
    }

    public static void draw(Map<String, Integer> data) {
        EventQueue.invokeLater(() -> {
            var ex = new DrawDiagram(data);
            ex.setVisible(true);
        });
    }
}