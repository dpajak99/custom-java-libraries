import utilities.PdfGenerator;
import utilities.PdfPage;
import utilities.PdfSettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Application {
  /**
   * SAMPLE
   */
  public static void main (String[] args) throws IOException {
    List<PdfPage> pages = new ArrayList<>();

    PdfPage page = new PdfPage("mi001", "schedules", new HashMap<>());
    page.setSingleFilePath("src/main/resources/generated/singleFiles/");
    page.setSingleFileName("file1");
    pages.add(page);

    PdfPage page2 = new PdfPage("mi001", "schedules", new HashMap<>());
    page2.setSingleFilePath("src/main/resources/generated/singleFiles/");
    page2.setSingleFileName("file2");
    pages.add(page2);

    PdfPage page3 = new PdfPage("mi001", "schedules", new HashMap<>());
    page3.setSingleFilePath("src/main/resources/generated/singleFiles/");
    page3.setSingleFileName("file3");
    pages.add(page3);

    PdfSettings pdfSettings = new PdfSettings("test", "src/main/resources/generated/allFiles/", "document");
    PdfGenerator pdfGenerator = new PdfGenerator();
    pdfGenerator.generateTemplate(pages, pdfSettings);
  }
}
