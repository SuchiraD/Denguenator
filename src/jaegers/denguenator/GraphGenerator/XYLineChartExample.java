package jaegers.denguenator.GraphGenerator;



import org.jfree.data.xy.XYDataset;
//import org.jcommon;


import javax.swing.JFrame;

import java.awt.Color;
import java.awt.BasicStroke;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
//import jfreechart;

/**
 * Created by Janitha on 9/12/2016.
 */
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

public class XYLineChartExample extends JFrame {

    public XYLineChartExample(int[] s1, int[] s2) {
        super("XY Line Chart Example with JFreechart");

        JPanel chartPanel = createChartPanel(s1, s2);
        add(chartPanel, BorderLayout.CENTER);

        setSize (1500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public XYLineChartExample() {
        super("XY Line Chart Example with JFreechart");

//        JPanel chartPanel = createChartPanel();
//        add(chartPanel, BorderLayout.CENTER);

        setSize (1500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createChartPanel(int[] s1, int[] s2) {
        // creates a line chart object
        // returns the chart panel
        String chartTitle = "MC-Colombo";
        String xAxisLabel = "Weeks";
        String yAxisLabel = "Cases";
        boolean showLegend = false;
        boolean createURL = false;
        boolean createTooltip = false;

        XYDataset dataset;

        if (s1.length > 0) {
            XYSeriesCollection tmpdataset = new XYSeriesCollection();
            XYSeries series1 = new XYSeries("Actual");
            XYSeries series2 = new XYSeries("Predicted");

            for(int i = 0 ; i < s1.length; i++){
                series1.add(i, s1[i]);
                series2.add(i, s2[i]);
            }

            tmpdataset.addSeries(series1);
            tmpdataset.addSeries(series2);

            dataset = tmpdataset;
        } else {
            dataset = createDataset();
        }

//        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset);

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                xAxisLabel, yAxisLabel, dataset,
                PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();


// sets paint color for each series
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.RED);

// sets thickness for series (using strokes)



//        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinesVisible(false);
//        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(false);
//        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRenderer(renderer);



        return new ChartPanel(chart);
    }

    private XYDataset createDataset() {
        // creates an XY dataset...
        // returns the dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Object 1");
        XYSeries series2 = new XYSeries("Object 2");
        XYSeries series3 = new XYSeries("Object 3");

        series1.add(1.0, 2.0);
        series1.add(2.0, 3.0);
        series1.add(3.0, 2.5);
        series1.add(3.5, 2.8);
        series1.add(4.2, 6.0);
//        series1.add();

        series2.add(2.0, 1.0);
        series2.add(2.5, 2.4);
        series2.add(3.2, 1.2);
        series2.add(3.9, 2.8);
        series2.add(4.6, 3.0);

        series3.add(1.2, 4.0);
        series3.add(2.5, 4.4);
        series3.add(3.8, 4.2);
        series3.add(4.3, 3.8);
        series3.add(4.5, 4.0);

        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                new XYLineChartExample().setVisible(true);
            }
        });
    }
}
