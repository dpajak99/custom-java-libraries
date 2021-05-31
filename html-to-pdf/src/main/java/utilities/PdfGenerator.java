package utilities;

import com.lowagie.text.pdf.BaseFont;
import exceptions.FileNotFoundException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class PdfGenerator {
  private static final String MAIN_PATH = "/templates/pdf/";
  private static final String TEMPLATE_PATH = MAIN_PATH + "templates/";
  private static final String GENERATED_PATH = "/generated";
  private static final String FONTS_PATH = MAIN_PATH + "fonts/";

  private final TemplateEngine templateEngine = new TemplateEngine();

  public PdfGenerator() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setSuffix(".html");
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setTemplateMode(TemplateMode.HTML);

    templateEngine.setTemplateResolver(templateResolver);
  }

  /**
   * Generates PDF from HTML
   * Can save it loccaly on server
   * @param pages - list of single PdfPages
   * @param settings - global PDF settings
   * @return PDF bute array
   * @throws IOException - when something gone wrong
   */
  public ByteArrayOutputStream generateTemplate(List<PdfPage> pages, PdfSettings settings) throws IOException {
    List<String> parsedPages = parsePages(pages);
    try {
      generateSingleFileIfRequired(parsedPages, pages);
    } catch (Exception e) {
      e.printStackTrace();
    }
    ByteArrayOutputStream os = generatePdf(parsedPages, settings.getDocumentTitle());
    if (settings.isSaveContent()) {
      File file = new File(settings.getContentPath());
      file.mkdirs();
      file = new File(settings.getContentPath() + settings.getFileName() + ".pdf");
      writeFile(file, os);

    }
    return os;
  }

  /**
   * Generates a single file if so set in PdfPage parameters
   * @param parsedPages - List of PdfPages parsed to HTML
   * @param pages - list of single PdfPages
   * @throws IOException - when something gone wrong
   */
  public void generateSingleFileIfRequired(List<String> parsedPages, List<PdfPage> pages) throws IOException {
    for (int i = 0; i < parsedPages.size(); i++) {
      PdfPage page = pages.get(i);
      if (page.isSaveSingleFile()) {
        ByteArrayOutputStream singleFileData = generatePdf(parsedPages.get(i), page.getName());
        File file = new File(page.getSingleFilePath());
        file.mkdirs();
        file = new File(page.getSingleFilePath() + page.getSingleFileName() + ".pdf");
        writeFile(file, singleFileData);
      }
    }
  }

  /**
   * Saves file
   * @param file - file to save
   * @param data - file content
   */
  private void writeFile(File file, ByteArrayOutputStream data) {
    System.out.println(file.toString());
    try {
      OutputStream os = new FileOutputStream(file);
      if (data != null) {
        os.write(data.toByteArray());
      }
      os.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates final version of pdf (single page)
   * When you want to save only one page
   * @param parsedPage - Single page of pdf
   * @param documentTitle - document title
   * @return - pdf in byte array
   * @throws IOException - when something gone wrong
   */
  private ByteArrayOutputStream generatePdf(String parsedPage, String documentTitle) throws IOException {
    List<String> parsedPages = new ArrayList<>();
    parsedPages.add(parsedPage);
    return generatePdf(parsedPages, documentTitle);
  }

  /**
   * Generates final version of pdf
   * @param parsedPages - List of PdfPages parsed to HTML
   * @param documentTitle - document title
   * @return - pdf in byte array
   * @throws IOException - when something gone wrong
   */
  private ByteArrayOutputStream generatePdf(List<String> parsedPages, String documentTitle) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    ITextRenderer iTextRenderer = new ITextRenderer(300, 230);

    File f = new File("src/main/resources/"+FONTS_PATH);
    if (f.isDirectory()) {
      File[] files = f.listFiles((dir, name) -> {
        String lower = name.toLowerCase();
        return lower.endsWith(".otf") || lower.endsWith(".ttf");
      });

      assert files != null;
      for (File file : files) {
        iTextRenderer.getFontResolver().addFont(file.getAbsolutePath(), BaseFont.IDENTITY_H,
          BaseFont.NOT_EMBEDDED);
      }
    }

    iTextRenderer.setDocumentFromString(parsedPages.get(0));
    iTextRenderer.layout();
    iTextRenderer.createPDF(os, false);
    boolean isFirstPage = true;
    for (String parsedPage : parsedPages) {
      if( isFirstPage ) {
        isFirstPage = false;
        continue;
      }
      iTextRenderer.setDocumentFromString(parsedPage);
      iTextRenderer.layout();
      iTextRenderer.writeNextDocument();
    }
    iTextRenderer.getOutputDevice().setMetadata("title", documentTitle);
    iTextRenderer.finishPDF();
    os.close();
    return os;
  }

  /**
   * Builds HTML from PdfPage data
   * @param pages - list of PdfPages
   * @return HTML List
   */
  private List<String> parsePages(List<PdfPage> pages) {
    List<String> parsedPages = new ArrayList<>();
    for (PdfPage page : pages) {
      String templatePath = TEMPLATE_PATH + page.getDirectory() + "/";
      String cssPath = templatePath + "css/" + page.getTemplateName() + ".css";
      String htmlPath = templatePath + page.getTemplateName() + ".html";

      Map<String, Object> variables = page.getVariables();
      variables.put("style", getFile(cssPath));

      parsedPages.add(getHtmlString(variables, htmlPath));
    }
    return parsedPages;
  }

  /**
   * Load html file and replace variables
   * @param variables - variables to replace
   * @param templatePath - path to template
   * @return Parsed HTML
   */
  private String getHtmlString(Map<String, Object> variables, String templatePath) {
    try {
      final Context ctx = new Context();
      ctx.setVariables(variables);
      return templateEngine.process(templatePath, ctx);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Load file by path
   * @param path
   * @return file content
   */
  private String getFile(String path) {
    try {
      URL url = PdfGenerator.class.getResource(path);
      if (url == null) {
        throw new FileNotFoundException("File not found");
      }
      return new String(Files.readAllBytes(Paths.get(url.toURI())));
    } catch (IOException | URISyntaxException | FileNotFoundException e) {
      e.printStackTrace();
      return "";
    }
  }
}
