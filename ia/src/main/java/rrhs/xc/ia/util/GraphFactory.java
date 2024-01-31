package rrhs.xc.ia.util;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;

public class GraphFactory {
    
    private static ChartTheme defaultTheme = StandardChartTheme.createJFreeTheme();

    public static JFreeChart createTimeSeriesChart(String title, String timeAxisLabel, String valueAxisLabel, TimeSeriesCollection dataset) {
        ValueAxis timeAxis = new DateAxis(timeAxisLabel);
        timeAxis.setLowerMargin(0.02);
        timeAxis.setUpperMargin(0.02);
        NumberAxis valueAxis = new NumberAxis(valueAxisLabel);
        valueAxis.setAutoRangeIncludesZero(false);
        XYPlot plot = new XYPlot(dataset, timeAxis, valueAxis, null);


        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        plot.setRenderer(renderer);

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        defaultTheme.apply(chart);        

        return chart;
    }


}
