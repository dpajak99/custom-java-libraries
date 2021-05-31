package utilities;

import java.util.Map;

public class PdfPage {
  /**
   * Meta document title (like <title></title> in HTML
   */
  private String name;

  /**
   * Name of single file without extension (eg. mi001)
   * (html and css files must have the same name)
   */
  private String templateName;

  /**
   * Type of generated layout
   * Eg. schedule, invoice
   */
  private String directory;

  /**
   * Whether to save each subpage of a document separately, apart from the document itself
   */
  private boolean saveSingleFile;

  /**
   * If saveSingleFile == true, you have to select the save path
   */
  private String singleFilePath;

  /**
   * If saveSingleFile == true, you have to select the file name
   */
  private String singleFileName;

  /**
   * Map of variables to replace
   */
  private Map<String, Object> variables;

  public PdfPage(String templateName, String directory, Map<String, Object> variables) {
    this.templateName = templateName;
    this.directory = directory;
    this.variables = variables;
  }

  public String getTemplateName() {
    return templateName;
  }

  public String getDirectory() {
    return directory;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getName() {
    return name;
  }

  public String getSingleFileName() {
    return singleFileName;
  }

  public void setSingleFileName(String singleFileName) {
    this.singleFileName = singleFileName;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDirectory(String directory) {
    this.directory = directory;
  }

  public boolean isSaveSingleFile() {
    return saveSingleFile;
  }

  public void setSaveSingleFile(boolean saveSingleFile) {
    this.saveSingleFile = saveSingleFile;
  }

  public String getSingleFilePath() {
    return singleFilePath;
  }

  public void setSingleFilePath(String singleFilePath) {
    this.saveSingleFile = true;
    this.singleFilePath = singleFilePath;
  }

  public void setVariables(Map<String, Object> variables) {
    this.variables = variables;
  }
}
