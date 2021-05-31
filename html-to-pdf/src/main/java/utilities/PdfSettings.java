package utilities;

public class PdfSettings {
  /**
   * Meta document title (like <title></title> in HTML
   */
  private final String documentTitle;

  /**
   * It tells us whether to save the document or just return its data
   */
  private final boolean saveContent;

  /**
   * If saveContent == true, you have to select the save path
   */
  private String contentPath;

  /**
   * If saveContent == true, you have to select the file name
   */
  private String fileName;

  public PdfSettings(String documentTitle) {
    this.documentTitle = documentTitle;
    this.saveContent = false;
  }

  public PdfSettings(String documentTitle, String contentPath, String fileName) {
    this.documentTitle = documentTitle;
    this.saveContent = true;
    this.contentPath = contentPath;
    this.fileName = fileName;
  }

  public String getDocumentTitle() {
    return documentTitle;
  }

  public boolean isSaveContent() {
    return saveContent;
  }

  public String getFileName() {
    return fileName;
  }

  public String getContentPath() {
    return contentPath;
  }
}
