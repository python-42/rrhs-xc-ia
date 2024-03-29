package rrhs.xc.ia.data.mem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.PDFExportable;
import rrhs.xc.ia.data.i.SQLDataObject;
import rrhs.xc.ia.util.PdfFooter;
import rrhs.xc.ia.util.PdfUtils;
import rrhs.xc.ia.util.StringUtils;

public class Athlete extends SQLDataObject implements PDFExportable {

    private String name;
    private int gradYear;
    private HashMap<Season, List<Race>> raceMap = new HashMap<Season, List<Race>>();

    private int id;

    private final String[] pdfColumnTitles = {"MEET NAME", "DATE", "TIME", "PLACE", "MILE ONE SPLIT", "MILE TWO SPLIT", "MILE THREE SPLIT","AVERAGE SPLIT"};

    public Athlete(List<Race> list, String name, int gradYear, int id, boolean isNew) {
        super(isNew);
        this.name = name;
        this.gradYear = gradYear;

        this.id = id;

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
    
    //Standard getters
    public String getName() {
        return name;
    }

    public int getGradYear() {
        return gradYear;
    }
    
    public int getId() {
        return id;
    }

    public List<Race> getRaces() {
        return Collections.unmodifiableList(condense());
    }

    //Setters
    public void setName(String name) {
        this.setModified();
        this.name = name;
    }

    public void setGradYear(int gradYear) {
        this.setModified();
        this.gradYear = gradYear;
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
     * times, one of them will be returned. There is no guarantee of which one.
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

    public Season determineSeason(LocalDate date) {
        int year = date.getYear();
        int distance = gradYear - year;

        if (! (date.getMonthValue() < 11 && date.getMonthValue() > 6) ) {
            distance++;
        }

        switch (distance) {
            case 1:
                return Season.SENIOR;
            case 2:
                return Season.JUNIOR;
            case 3:
                return Season.SOPHOMORE;
            case 4:
                return Season.FRESHMAN;
            default:
                return null;
        }

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

        return PdfUtils.createTimeSeriesChart("", "Date", "Race Time", dataset);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public SQLRow writeToSQL() {
        SQLRow rtn = new SQLRow("Athlete", id);

        rtn.putPair(SQLTableInformation.Athlete.NAME_STR, name);
        rtn.putPair(SQLTableInformation.Athlete.GRADUATION_YEAR_INT, gradYear);

        return rtn;
    }

    @Override
    public void writeToPDF(File file) throws IOException{
        Document doc = PdfUtils.getDefaultDimensionsDocument();

        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
        writer.setPageEvent(new PdfFooter(PdfUtils.DEFAULT_DOCUMENT_WIDTH / 2, PdfUtils.DEFAULT_DOCUMENT_HEIGHT_MARGIN / 2));

        doc.addSubject(getName() + " Cross Country Summary");

        float spacing = 10;
        
        //Page one: Title and graph
        doc.open();
        doc.add(PdfUtils.getParagraph(getName(), PdfUtils.TITLE_FONT, spacing));
        doc.add(PdfUtils.getParagraph("Class of " + getGradYear(), PdfUtils.SUBTITLE_FONT, spacing));
        doc.add(new LineSeparator());
        doc.add(Image.getInstance(PdfUtils.getChartImageBytes(this.getGraph(), 800 - PdfUtils.DEFAULT_DOCUMENT_HEIGHT_MARGIN, PdfUtils.DEFAULT_DOCUMENT_WIDTH - PdfUtils.DEFAULT_DOCUMENT_WIDTH_MARGIN)));
        doc.newPage();
        
        //Page two: Performance tables for each season
        PdfPTable table;
        for (Season season : getSeasons()) {
            table = new PdfPTable(pdfColumnTitles.length);
            table.setHeaderRows(1);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setPadding(spacing);

            //Header row
            for (String title : pdfColumnTitles) {
                PdfPCell cell = new PdfPCell(new Paragraph(title, PdfUtils.TABLE_HEADER_FONT));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(spacing);

                table.addCell(cell);
            }
            table.completeRow();

            Collections.sort(raceMap.get(season));

            //Data rows
            for (Race race : raceMap.get(season)) {
                table.addCell(race.getMeetName());
                table.addCell(race.getMeetDate().toString());
                table.addCell(StringUtils.formatTime(race.getTimeSeconds()));
                table.addCell(race.getPlace() + "");
                table.addCell(StringUtils.formatTime(race.getMileOneSplitSeconds()));
                table.addCell(StringUtils.formatTime(race.getMileTwoSplitSeconds()));
                table.addCell(StringUtils.formatTime(race.getMileThreeSplitSeconds()));
                table.addCell(StringUtils.formatTime(race.getAverageSplitSeconds()));

                table.completeRow();
            }

            doc.add(PdfUtils.getParagraph(season.name(), PdfUtils.SECTION_HEADER_FONT, spacing));
            doc.add(table);
        }

        doc.newPage();

        //Page three: Statistics
        doc.add(PdfUtils.getParagraph("Statistics", PdfUtils.STATS_TITLE_FONT));

        for (Season season : getSeasons()) {
            doc.add(PdfUtils.getParagraph(season.name(), PdfUtils.SECTION_HEADER_FONT));

            doc.add(PdfUtils.getParagraph("Time Drop: " + StringUtils.formatTime(getSeasonTimeDropSeconds(season)), PdfUtils.STATS_FONT));
            doc.add(PdfUtils.getParagraph("Average Time: " + StringUtils.formatTime(getSeasonAverageTime(season)), PdfUtils.STATS_FONT));
            doc.add(PdfUtils.getParagraph("Average Mile 1 Split: " + StringUtils.formatTime(getAverageSeasonSplitSeconds(season, 1)), PdfUtils.STATS_FONT));
            doc.add(PdfUtils.getParagraph("Average Mile 2 Split: " + StringUtils.formatTime(getAverageSeasonSplitSeconds(season, 2)), PdfUtils.STATS_FONT));
            doc.add(PdfUtils.getParagraph("Average Mile 3 Split: " + StringUtils.formatTime(getAverageSeasonSplitSeconds(season, 3)), PdfUtils.STATS_FONT));

            Race best = getBestSeasonRace(season);
            doc.add(PdfUtils.getParagraph("Best Race: " + best.getMeetName() + " (" + StringUtils.formatTime(best.getTimeSeconds()) + ")", PdfUtils.STATS_FONT));
        }

        doc.add(PdfUtils.getParagraph("CAREER", PdfUtils.SECTION_HEADER_FONT));

        doc.add(PdfUtils.getParagraph("Time Drop: " + StringUtils.formatTime(getCareerTimeDropSeconds()), PdfUtils.STATS_FONT));
        doc.add(PdfUtils.getParagraph("Average Time: " + StringUtils.formatTime(getCareerAverageTime()), PdfUtils.STATS_FONT));
        doc.add(PdfUtils.getParagraph("Average Mile 1 Split: " + StringUtils.formatTime(getAverageCareerSplitSeconds(1)), PdfUtils.STATS_FONT));
        doc.add(PdfUtils.getParagraph("Average Mile 2 Split: " + StringUtils.formatTime(getAverageCareerSplitSeconds(2)), PdfUtils.STATS_FONT));
        doc.add(PdfUtils.getParagraph("Average Mile 3 Split: " + StringUtils.formatTime(getAverageCareerSplitSeconds(3)), PdfUtils.STATS_FONT));

        Race best = getBestCareerRace();
        doc.add(PdfUtils.getParagraph("Best Race: " + best.getMeetName() + " (" + StringUtils.formatTime(best.getTimeSeconds()) + ")", PdfUtils.STATS_FONT));

        doc.close();
    }

}