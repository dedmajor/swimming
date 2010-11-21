package ru.swimmasters;

import org.joda.time.LocalDate;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.swimmasters.domain.*;
import ru.swimmasters.parser.DisciplineParser;
import ru.swimmasters.parser.MastersDisciplineParser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: dedmajor
 * Date: Nov 18, 2010
 */
public class ParserApp {
    public final DisciplineParser disciplineParser = new MastersDisciplineParser();

    private Event currentEvent;
    private Meet meet;
    private Pool pool;
    private MeetRegister meetRegister;
    private TotalRanking currentTotalRanking;

    public static void main(String[] args) throws XPathExpressionException {
        OdfFileDom odfContent;
        OdfDocument odfDoc;
        try {
            odfDoc = OdfDocument.loadDocument(System.in);
            odfContent = odfDoc.getContentDom();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        XPath xpath = odfDoc.getXPath();
        NodeList nodeList = (NodeList) xpath.evaluate("//table:table", odfContent, XPathConstants.NODESET);
        int numtables = nodeList.getLength();

        for (int nc = 0; nc < numtables; nc++) {
            System.out.println("========== START SHEET: " + (nc + 1) + " ==========");
            String str = "//table:table[" + nc + 1 + "]/table:table-row";
            NodeList nlst = (NodeList) xpath.evaluate(str, odfContent, XPathConstants.NODESET);
            if (nc == 0) {
                // only first sheet
                new ParserApp().parseMeet(nlst);
            }
            System.out.println("========== END SHEET: " + (nc + 1) + " ==========");
        }
    }

    private void parseMeet(NodeList nlst) {
        // TODO: FIXME: get from nodes
        meet = new Meet("V международный турнир по плаванию в категории «Мастерс» «St.Petersburg OPEN 2010»");
        meet.setStartDate(new LocalDate("2010-06-05")); // TODO: sessions for several days
        pool = new Pool("Центр Плавания", "С.-Петербург");
        // pool.setLength(50); // TODO: FIXME: really need this?
        meet.setPool(pool);
        meetRegister = new MeetRegister(meet);

        ParserState state = ParserState.INITIAL;
        for (int k = 0; k < nlst.getLength(); k++) {
            Node row = nlst.item(k);
            for (int cn = 0; cn < row.getChildNodes().getLength(); cn++) {
                System.out.print(row.getChildNodes().item(cn).getTextContent() + "\t");
            }
            System.out.println();
            if (disciplineParser.isDiscipline(row.getTextContent())) {
                state = ParserState.DISCIPLINE;
                parseDiscipline(row);
                System.out.println("EVENT: " + currentEvent);
            } else if (state == ParserState.DISCIPLINE) {
                if (isEmpty(row)) {
                    System.out.println("STATE: INITIAL");
                    state = ParserState.INITIAL;
                } else {
                    // parseAthleteResult(row);
                }
            }
        }
    }

    private boolean isEmpty(Node row) {
        return row.getTextContent().trim().isEmpty();
    }

    private void parseAthleteResult(Node row) {
        Athlete athlete = new Athlete(); // TODO: FIXME: either new or search by name
        Application application = new Application(currentEvent, athlete);
        Result currentResult = new Result(application);
        currentTotalRanking.addResult(currentResult);
    }

    private void parseDiscipline(Node row) {
        Discipline currentDiscipline = disciplineParser.parseDiscipline(row.getTextContent());
        currentEvent = new Event(currentDiscipline);
        meet.addEvent(currentEvent);
        currentTotalRanking = new TotalRanking(currentEvent);
        meetRegister.addTotalRanking(currentTotalRanking);
    }

    private enum ParserState {
        INITIAL,
        DISCIPLINE
    }
}
