package org.agmip.translators.annotated.parsers;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import org.agmip.translators.annotated.data.DataContextKey;
import org.agmip.translators.annotated.data.DataFileKey;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

public class RawDataHandler implements ContentHandler {
  private enum RDHContext {
    IGNORE,
    SHEET,
    CELL
  }

  private final DataFileKey dfk;
  private final Set<String> targetSheets;
  private RDHContext context = RDHContext.IGNORE;
  private String currentSheet = "_";
  private Seq<String> currentRow = List.empty();
  private Seq<Seq<String>> sheetRows = List.empty();
  private Map<DataContextKey, Seq<Seq<String>>> results;
  private boolean parseThisSheet = false;
  private boolean hasSheetName = false;

  public RawDataHandler(
      DataFileKey dfk, Set<String> sheets, Map<DataContextKey, Seq<Seq<String>>> parsed) {
    super();
    this.dfk = dfk;
    this.targetSheets = sheets;
    this.results = parsed;
  }

  public RawDataHandler(DataFileKey dfk) {
    this(dfk, HashSet.empty(), HashMap.empty());
  }

  public RawDataHandler(DataFileKey dfk, Set<String> sheets) {
    this(dfk, sheets, HashMap.empty());
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) {
    switch (qName) {
      case "h1":
        hasSheetName = true;
        parseThisSheet = false;
        context = RDHContext.SHEET;
        if (!sheetRows.isEmpty()) {
          results = results.put(new DataContextKey(dfk, currentSheet), sheetRows);
          sheetRows = List.empty();
        }
        break;
      case "td":
        context = RDHContext.CELL;
        break;
      default:
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("tr")) {
      // Ending a row
      sheetRows = sheetRows.append(currentRow);
      currentRow = List.empty();
    }
    context = RDHContext.IGNORE;
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    String value = new String(ch);
    switch (context) {
      case SHEET:
        currentSheet = value;
        if (targetSheets.contains(value)) {
          parseThisSheet = true;
        }
        break;
      case CELL:
        if (!hasSheetName) {
          parseThisSheet = true;
        }
        if (parseThisSheet) {
          currentRow = currentRow.append(value);
        }
        break;
      default:
        break;
    }
  }

  @Override
  public void setDocumentLocator(Locator locator) {}

  @Override
  public void startDocument() {}

  @Override
  public void endDocument() {
    if (!sheetRows.isEmpty()) {
      results = results.put(new DataContextKey(dfk, currentSheet), sheetRows);
    }
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) {}

  @Override
  public void endPrefixMapping(String prefix) {}

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) {}

  @Override
  public void processingInstruction(String target, String data) {}

  @Override
  public void skippedEntity(String name) {}

  public Map<DataContextKey, Seq<Seq<String>>> getResults() {
    return results;
  }
}
