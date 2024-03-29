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
import java.util.Map;
import java.util.function.Function;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
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

public class Meet extends SQLDataObject implements PDFExportable  {

    private String name;
    private LocalDate date;
    private HashMap<Level, List<Race>> raceMap = new HashMap<Level, List<Race>>();

    private int id;

    private final String[] pdfColumnTitles = {"ATHLETE NAME", "TIME", "PLACE", "MILE ONE SPLIT", "MILE TWO SPLIT", "MILE THREE SPLIT", "AVERAGE SPLIT"};

    public Meet(List<Race> list, String name, LocalDate date, int id, boolean isNew) {
        super(isNew);
        this.name = name;
        this.date = date;

        this.id = id;

        if (list == null) {
            return;
        }
        for (Race r : list) {
            addRace(r);
        }
    }
    
    //Getters
    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<Level, List<Race>> getRaces() {
        return raceMap;
    }

    public int getId() {
        return id;
    }

    public List<Race> getRaceList() {
        List<Race> rtn = new ArrayList<Race>();
        for (List<Race> r : raceMap.values()) {
            if (r != null) {
                rtn.addAll(r);
            }
        }
        return Collections.unmodifiableList(rtn);
    }

    //Setters
    public void setName(String name) {
        this.setModified();
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.setModified();
        this.date = date;
    }

    public void addRace(Race race) {
        if (raceMap.get(race.getLevel()) == null) {
            raceMap.put(race.getLevel(), new ArrayList<Race>());
        }
        raceMap.get(race.getLevel()).add(race);
    }

    public double getAverageTimeSeconds(Level l) {
        return getAverage(raceMap.get(l), (Race r) -> {return r.getTimeSeconds();});
    }

    public double getAverageSplitSeconds(int mile, Level l) {
        if (mile == 1) {
            return getAverage(raceMap.get(l), (Race r) -> {return r.getMileOneSplitSeconds();});
        }

        if (mile == 2) {
            return getAverage(raceMap.get(l), (Race r) -> {return r.getMileTwoSplitSeconds();});
        }

        if (mile == 3) {
            return getAverage(raceMap.get(l), (Race r) -> {return r.getMileThreeSplitSeconds();});
        }

        return -1;        
    }

    public double getTimeSpreadSeconds(Level l) {
        return getSpread(raceMap.get(l), (Race r) -> {return r.getTimeSeconds();}).doubleValue();
    }

    public int getPlaceSpread(Level l ) {
        return getSpread(raceMap.get(l), (Race r) -> {return r.getPlace();}).intValue();
    }

    private double getAverage(List<Race> races, Function<Race, Double> getter) {
        if (races == null || races.size() == 0) {
            return -1;
        }

        double rtn = 0;
        for (Race r : races) {
            rtn += getter.apply(r);
        }

        return rtn / races.size();
    }

    private Number getSpread(List<Race> races, Function<Race, Number> getter) {
        if (races == null || races.size() == 0) {
            return -1;
        }

        Number max = getter.apply(races.get(0));
        for (Race r : races) {
            if (getter.apply(r).doubleValue() > max.doubleValue()) {
                max = getter.apply(r);
            }
        }

        Number min = getter.apply(races.get(0));
        for (Race r : races) {
            if (getter.apply(r).doubleValue() < min.doubleValue()) {
                min = getter.apply(r);
            }
        }

        return max.doubleValue() - min.doubleValue();
    }

    public Level[] getLevels() {
        Level[] l = {};
        l = raceMap.keySet().toArray(l);
        Arrays.sort(l);
        return l;
    }

    @Override
    public String toString() {
        return name + " (" + date.toString() + ")";
    }

    @Override
    public SQLRow writeToSQL() {
        SQLRow rtn = new SQLRow("Meet", id);
        
        rtn.putPair(SQLTableInformation.Meet.MEET_NAME_STR, name);
        rtn.putPair(SQLTableInformation.Meet.MEET_DATE_DATE, date);
        return rtn;
    }

    @Override
    public void writeToPDF(File file) throws IOException {
        Document doc = PdfUtils.getDefaultDimensionsDocument();
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file));

        writer.setPageEvent(new PdfFooter(PdfUtils.DEFAULT_DOCUMENT_WIDTH / 2, PdfUtils.DEFAULT_DOCUMENT_HEIGHT_MARGIN / 2));

        doc.addSubject(getName() + " Meet Summary");

        float spacing = 10;
        
        doc.open();

        //Page one: Title and stats
        doc.add(PdfUtils.getParagraph(getName(), PdfUtils.TITLE_FONT, spacing));
        doc.add(PdfUtils.getParagraph(getDate().toString(), PdfUtils.SUBTITLE_FONT, spacing));
        doc.add(new LineSeparator());
        
        doc.add(PdfUtils.getParagraph("Statistics", PdfUtils.STATS_TITLE_FONT));
        for (Level level : getLevels()) {
            doc.add(PdfUtils.getParagraph(level.name().replace('_', ' '), PdfUtils.SECTION_HEADER_FONT));

            doc.add(PdfUtils.getParagraph("Average Time: " + StringUtils.formatTime(getAverageTimeSeconds(level)), PdfUtils.STATS_FONT));
            doc.add(PdfUtils.getParagraph("Average Mile One Split: " + StringUtils.formatTime(getAverageSplitSeconds(1, level)), PdfUtils.STATS_FONT));
            doc.add(PdfUtils.getParagraph("Average Mile Two Split: " + StringUtils.formatTime(getAverageSplitSeconds(2, level)), PdfUtils.STATS_FONT));
            doc.add(PdfUtils.getParagraph("Average Mile Three Split: " + StringUtils.formatTime(getAverageSplitSeconds(3, level)), PdfUtils.STATS_FONT));
            doc.add(PdfUtils.getParagraph("Time Spread: " + StringUtils.formatTime(getTimeSpreadSeconds(level)), PdfUtils.STATS_FONT));
        }
        doc.newPage();

        //Page two: Performance tables
        PdfPTable table;
        for (Level level : getLevels()) {
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

            Collections.sort(raceMap.get(level));

            //Data rows
            for (Race race : raceMap.get(level)) {
                table.addCell(race.getAthleteName());
                table.addCell(StringUtils.formatTime(race.getTimeSeconds()));
                table.addCell(race.getPlace() + "");
                table.addCell(StringUtils.formatTime(race.getMileOneSplitSeconds()));
                table.addCell(StringUtils.formatTime(race.getMileTwoSplitSeconds()));
                table.addCell(StringUtils.formatTime(race.getMileThreeSplitSeconds()));
                table.addCell(StringUtils.formatTime(race.getAverageSplitSeconds()));
            }
            table.completeRow();

            doc.add(PdfUtils.getParagraph(level.name().replace('_', ' '), PdfUtils.SECTION_HEADER_FONT, spacing));
            doc.add(table);

        }

        doc.close();
    }
    
}
