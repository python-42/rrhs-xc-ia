package rrhs.xc.ia.data.mem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import rrhs.xc.ia.data.Season;
import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.PDFExportable;
import rrhs.xc.ia.data.i.SQLSerializable;
import rrhs.xc.ia.util.GraphFactory;
import rrhs.xc.ia.util.PdfFooter;
import rrhs.xc.ia.util.PrettyPrinter;

public class Athlete implements PDFExportable, SQLSerializable {

    private String name;
    private boolean isBoysTeam;
    private int gradYear;
    private HashMap<Season, List<Race>> raceMap = new HashMap<Season, List<Race>>();

    private boolean canSqlWrite = true;
    private final String[] pdfColumnTitles = {"MEET NAME", "DATE", "TIME", "PLACE", "MILE ONE SPLIT", "MILE TWO SPLIT", "MILE THREE SPLIT","AVERAGE SPLIT"};

    public Athlete(List<Race> list) {
        if (list == null) {
            return;
        }
        for (Race r : list) {
            if (raceMap.get(r.getSeason()) == null) {
                raceMap.put(r.getSeason(), new ArrayList<Race>());
            }
            raceMap.get(r.getSeason()).add(r);
        }
    }

    /**
     * Get the race with the best time. If the list contains races with identical
     * times, one of them will be returned. There is no guarantee of which one.
     * 
     * If the list is null or empty, return null.
     * 
     * @param list a list of race objects.
     * @return the fastest race or null
     */
    private Race getFastest(List<Race> list) {
        if(list == null || list.size() == 0) {
            return null;
        }

        int index = 0;
        for (int i = 1; i < list.size(); i ++) {
            if(list.get(i).getTimeSeconds() < list.get(index).getTimeSeconds()) {
                index = i;
            }
        }
        
        return list.get(index);
    }

    /**
     * Get the race with the worst time. If the list contains race with identical
     * times, one of them will be returned. There is no guarnatee of which one.
     * 
     * If the list is null or empty, return null.
     * 
     * @param list a list of race objects.
     * @return the slowest race or null
     */
    private Race getSlowest(List<Race> list) {
        if(list == null || list.size() == 0) {
            return null;
        }
        int index = 0;
        for (int i = 1; i < list.size(); i ++) {
            if(list.get(i).getTimeSeconds() > list.get(index).getTimeSeconds()) {
                index = i;
            }
        }
        
        return list.get(index);
    }

    /**
     * Condense all of the races from the HashMap in to a single list.
     * 
     * @return All the races from the map in a single list
     */
    private List<Race> condense() {
        List<Race> races = new ArrayList<Race>();
        for (List<Race> r : raceMap.values()) {
            if (r == null) {
                continue;
            }
            races.addAll(r);
        }
        return races;
    }

    public String getName() {
        return name;
    }

    public boolean isBoysTeam() {
        return isBoysTeam;
    }

    public int getGradYear() {
        return gradYear;
    }

    public Race getWorstCareerRace() {
        return getSlowest(condense());
    }

    public Race getWorstSeasonRace(Season s) {
        return getSlowest(raceMap.get(s));

    }

    public Race getBestCareerRace() {
        return getFastest(condense());
    }

    public Race getBestSeasonRace(Season s) {
        return getFastest(raceMap.get(s));
    }

    public double getCareerTimeDropSeconds() {
        Race best = getBestCareerRace();
        Race worst = getWorstCareerRace();

        if(best != null && worst != null) {
            return worst.getTimeSeconds() - best.getTimeSeconds();
        }
        return -1;
    }

    public double getSeasonTimeDropSeconds(Season s) {
        Race best = getBestSeasonRace(s);
        Race worst = getWorstSeasonRace(s);

        if(best != null && worst != null) {
            return worst.getTimeSeconds() - best.getTimeSeconds();
        }
        return -1;
    }

    public double getCareerAverageTime() {
        double totalTime = 0;
        int count = 0;
        for (Race r : condense()) {
            totalTime += r.getTimeSeconds();
            count++;
        }

        if(count == 0) {
            return -1;
        }

        return totalTime / count;
    }

    public double getSeasonAverageTime(Season s) {
        if(raceMap.get(s) == null || raceMap.get(s).size() == 0) {
            return -1;
        }

        double totalTime = 0;
        for (Race r : raceMap.get(s)) {
            totalTime += r.getTimeSeconds();
        }

        return totalTime / raceMap.size();
    }

    public Season[] getSeasons() {
        Season[] s = {};
        s = raceMap.keySet().toArray(s);
        Arrays.sort(s);
        return s;
    }

    public double getAverageCareerSplitSeconds(int mile) {
        if (mile > 3 || mile < 1) {
            return -1;
        }

        double totalTime = 0;
        int count = 0;
        for (Race r : condense()) {
            count++;
            if(mile == 1) {
                totalTime += r.getMileOneSplitSeconds();
            }else if(mile == 2) {
                totalTime += r.getMileTwoSplitSeconds();
            }else {
                totalTime += r.getMileThreeSplitSeconds();
            }
        }

        if(count == 0) {
            return -1;
        }

        return totalTime / count;
    }

    public double getAverageSeasonSplitSeconds(Season s, int mile) {
        if (mile > 4 || mile < 1 || raceMap.get(s) == null || raceMap.get(s).size() == 0) {
            return -1;
        }

        double totalTime = 0;
        for (Race r : raceMap.get(s)) {
            if(mile == 1) {
                totalTime += r.getMileOneSplitSeconds();
            }else if(mile == 2) {
                totalTime += r.getMileTwoSplitSeconds();
            }else {
                totalTime += r.getMileThreeSplitSeconds();
            }
        }

        return totalTime / raceMap.get(s).size();
    }

    public JFreeChart getGraph() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
        for (Season s : getSeasons()) {
            TimeSeries series = new TimeSeries(s.name());
            List<Race> races = raceMap.get(s);

            Race r;
            for (int i = 0; i < races.size(); i++) {
                r = races.get(i);
                //Hardcode a year so that only day and month are displayed
                series.add(new Day(r.getMeetDate().getDayOfMonth(), r.getMeetDate().getMonthValue(), 1970), r.getTimeSeconds() / 60.0);
            }
            dataset.addSeries(series);
        }

        return GraphFactory.createTimeSeriesChart("", "Date", "Race Time", dataset);
    }

    private byte[] getGraphImageBytes(int height, int width) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, getGraph(), width, height);

        return out.toByteArray();
    }
    

    @Override
    public void loadFromSQL(SQLRow result) {
        if (canSqlWrite) {
            name = SQLTypeConversion.getString(result.get(SQLTableInformation.Athlete.NAME_STR));
            isBoysTeam = SQLTypeConversion.getBoolean(result.get(SQLTableInformation.Athlete.BOYS_TEAM_BOOL));
            gradYear = SQLTypeConversion.getInt(result.get(SQLTableInformation.Athlete.GRADUATION_YEAR_INT));

            canSqlWrite = false;
        }
    }

    @Override
    public SQLRow writeTOSQL() {
        SQLRow rtn = new SQLRow("Athlete", 0);

        rtn.putPair(SQLTableInformation.Athlete.NAME_STR, name);
        rtn.putPair(SQLTableInformation.Athlete.BOYS_TEAM_BOOL, isBoysTeam);
        rtn.putPair(SQLTableInformation.Athlete.GRADUATION_YEAR_INT, gradYear);

        return rtn;
    }

    @Override
    public void writeToPDF(File file) throws DocumentException, IOException{
        final int DOCUMENT_HEIGHT = 1000;
        final int DOCUMENT_WIDTH = 1000;

        final int DOCUMENT_HEIGHT_MARGIN = 50;
        final int DOCUMENT_WIDTH_MARGIN = 100;

        final int CHART_HEIGHT = 800 - DOCUMENT_HEIGHT_MARGIN;
        final int CHART_WIDTH = DOCUMENT_WIDTH - DOCUMENT_WIDTH_MARGIN;


        Document doc = new Document(new Rectangle(DOCUMENT_HEIGHT, DOCUMENT_WIDTH), DOCUMENT_WIDTH_MARGIN / 2, DOCUMENT_WIDTH_MARGIN / 2, DOCUMENT_HEIGHT_MARGIN / 2, DOCUMENT_HEIGHT_MARGIN / 2);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
        PdfFooter footer = new PdfFooter(DOCUMENT_WIDTH / 2, (int)(DOCUMENT_HEIGHT_MARGIN * 0.5));
        footer.setFooterText("Generated on " + LocalDate.now().toString() + " by JCrossCountry Tracker. Go Huskies!");
        
        writer.setPageEvent(footer);

        doc.addSubject(getName() + " Cross Country Summary");
        doc.addCreationDate();

        float spacing = 10;

        Font titleFont = FontFactory.getFont(FontFactory.COURIER, 40, Font.NORMAL, BaseColor.BLUE);
        Font subtitleFont = FontFactory.getFont(FontFactory.COURIER, 30, Font.ITALIC, BaseColor.GRAY);
        Font sectionHeaderFont = FontFactory.getFont(FontFactory.COURIER, 30, Font.NORMAL, BaseColor.BLUE);
        Font statsHeaderFont = FontFactory.getFont(FontFactory.COURIER, 35, Font.UNDERLINE, BaseColor.BLACK);
        Font tableHeaderFont = FontFactory.getFont(FontFactory.COURIER, 15, Font.BOLD, new BaseColor(0, 102, 255));
        Font textFont = FontFactory.getFont(FontFactory.COURIER, 15, Font.NORMAL, BaseColor.BLACK);

        Paragraph title = new Paragraph(getName(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(spacing);

        Paragraph subtitle = new Paragraph("Class of " + getGradYear() + "\n", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(spacing);

        Paragraph statsTitle = new Paragraph("Statistics", statsHeaderFont);
        statsTitle.setAlignment(Element.ALIGN_CENTER);
        statsTitle.setSpacingAfter(spacing);

        Paragraph generationInformation = new Paragraph("Generated on " + LocalDate.now().toString() + " by JCrossCountry Tracker. Go Huskies!", textFont);
        generationInformation.setAlignment(Element.ALIGN_RIGHT);

        Image img = Image.getInstance(getGraphImageBytes(CHART_HEIGHT, CHART_WIDTH));
        doc.open();
        doc.add(title);
        doc.add(subtitle);
        doc.add(new LineSeparator());
        doc.add(img);
        doc.newPage();
        
        PdfPTable table;
        Paragraph sectionHeader;
        for (Season season : getSeasons()) {
            sectionHeader = new Paragraph(season.name(), sectionHeaderFont);
            sectionHeader.setAlignment(Element.ALIGN_CENTER);
            sectionHeader.setSpacingAfter(spacing);

            table = new PdfPTable(pdfColumnTitles.length);
            table.setHeaderRows(1);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setPadding(spacing);

            for (String s : pdfColumnTitles) {
                PdfPCell cell = new PdfPCell(new Phrase(s, tableHeaderFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(spacing);

                table.addCell(cell);
            }
            
            table.completeRow();

            Collections.sort(raceMap.get(season));

            for (Race r : raceMap.get(season)) {
                table.addCell(r.getMeetName());
                table.addCell(r.getMeetDate().toString());
                table.addCell(PrettyPrinter.formatTime(r.getTimeSeconds()));
                table.addCell(r.getPlace() + "");
                table.addCell(PrettyPrinter.formatTime(r.getMileOneSplitSeconds()));
                table.addCell(PrettyPrinter.formatTime(r.getMileTwoSplitSeconds()));
                table.addCell(PrettyPrinter.formatTime(r.getMileThreeSplitSeconds()));
                table.addCell(PrettyPrinter.formatTime(r.getAverageSplitSeconds()));

                table.completeRow();
            }

            doc.add(sectionHeader);
            doc.add(table);
        }

        doc.newPage();
        doc.add(statsTitle);

        //TODO: statistics 
        doc.close();
    }

}
