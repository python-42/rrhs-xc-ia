package rrhs.xc.ia.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;

public class PdfUtils {
    
    public static final int DEFAULT_DOCUMENT_HEIGHT = 1000;
    public static final int DEFAULT_DOCUMENT_WIDTH = 1000;
    public static final int DEFAULT_DOCUMENT_HEIGHT_MARGIN = 50;
    public static final int DEFAULT_DOCUMENT_WIDTH_MARGIN = 100;

    public static final Font TITLE_FONT = FontFactory.getFont(FontFactory.COURIER, 40, Font.NORMAL, BaseColor.BLUE);
    public static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.COURIER, 30, Font.ITALIC, BaseColor.GRAY);
    public static final Font SECTION_HEADER_FONT = FontFactory.getFont(FontFactory.COURIER, 30, Font.NORMAL, BaseColor.BLUE);
    public static final Font TABLE_HEADER_FONT = FontFactory.getFont(FontFactory.COURIER, 15, Font.BOLD, new BaseColor(0, 102, 255));
    public static final Font STATS_TITLE_FONT = FontFactory.getFont(FontFactory.COURIER, 35, Font.UNDERLINE, BaseColor.BLACK);
    public static final Font STATS_FONT = FontFactory.getFont(FontFactory.COURIER, 15, Font.NORMAL, BaseColor.BLACK);

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

    public static byte[] getChartImageBytes(JFreeChart chart, int height, int width) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, width, height);

        return out.toByteArray();
    }

    /**
     * Utility method to get a new document with some default information added.
     * This document has: 
     * <ol>
     * <li> Default Dimensions </li>
     * <li> Creation date metadata added </li>
     * </ol>
     * @return an <code>Document</code> object with defaults added.
     */
    public static Document getDefaultDimensionsDocument() {
        Document doc =  new Document(
            new Rectangle(DEFAULT_DOCUMENT_HEIGHT, DEFAULT_DOCUMENT_WIDTH),
            DEFAULT_DOCUMENT_WIDTH_MARGIN  / 2,
            DEFAULT_DOCUMENT_WIDTH_MARGIN  / 2,
            DEFAULT_DOCUMENT_HEIGHT_MARGIN / 2,
            DEFAULT_DOCUMENT_HEIGHT_MARGIN / 2
        );

        doc.addCreationDate();

        return doc;
    }


    /**
     * Return a new paragraph with a center alignment.
     * @param content content of this paragraph
     * @param font font of this paragraph
     * @return a new paragraph 
     */
    public static Paragraph getParagraph(String content, Font font) {
        Paragraph rtn = new Paragraph(content, font);

        rtn.setAlignment(Element.ALIGN_CENTER);

        return rtn;
    }


    /**
     * Return a new paragraph with a center alignment.
     * @param content content of this paragraph
     * @param font font of this paragraph
     * @param spacing spacing after this paragraph
     * @return a new paragraph
     */
    public static Paragraph getParagraph(String content, Font font, float spacing) {
        Paragraph rtn = new Paragraph(content, font);

        rtn.setAlignment(Element.ALIGN_CENTER);
        rtn.setSpacingAfter(spacing);

        return rtn;
    }

}
