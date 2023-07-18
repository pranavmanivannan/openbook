package gt.trading.openbook.core;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

import gt.trading.openbook.MapperSingleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public final class LocalStorage<T> {
  private static final int DEFAULT_MAX_ROWS = 100;

  private final int csvMaxRows;
  private final String saveFolder;
  private final String fileName;

  private final ObjectMapper objectMapper = MapperSingleton.getInstance();
  private BufferedWriter bw;
  private int csvRowCount = 0;

  private static final Logger LOGGER = Logger
      .getLogger(LocalStorage.class.getName());

  public static class Builder<T> {
    private final String saveFolder;

    private String fileName = "";
    private int csvMaxRows = DEFAULT_MAX_ROWS;

    /**
     * Builder Constructor.
     *
     * @param newSaveFolder the folder to save files in
     */
    public Builder(final String newSaveFolder) {
      this.saveFolder = newSaveFolder;
    }

    /**
     * Set fileName.
     *
     * @param val the file name
     * @return builder
     */
    public Builder<T> fileName(final String val) {
      this.fileName = val;
      return this;
    }

    /**
     * Set csvMaxRows.
     *
     * @param val max number of rows in csv files
     * @return builder
     */
    public Builder<T> csvMaxRows(final int val) {
      this.csvMaxRows = val;
      return this;
    }

    /**
     * Build LocalStorage instance.
     *
     * @return instance
     */
    public LocalStorage<T> build() throws IOException {
      return new LocalStorage<T>(this);
    }
  }

  /**
   * LocalStorage constructor. If not given a filename to resume, would generate
   * a filename by current time.
   *
   * @param builder custom builder used to instantiate class
   */
  private LocalStorage(final Builder<T> builder) throws IOException {

    this.csvMaxRows = builder.csvMaxRows;
    this.saveFolder = builder.saveFolder;

    if (builder.fileName.equals("")) {
      this.fileName = getTimeFileName();
    } else {
      this.fileName = builder.fileName;
    }

    this.bw = new BufferedWriter(new FileWriter(getFilePath(), true));
  }

  /**
   * Callback function to be called when an event occurs.
   *
   * @param data
   */
  public void onEvent(final T data) {
    try {
      bw.append(objectMapper.writeValueAsString(data));
      bw.newLine();
      csvRowCount += 1;
      if (csvRowCount >= csvMaxRows) {
        flushToFile();
      }
    } catch (Exception e) {
      LOGGER.warning(e.getMessage());
    }
  }

  /**
   * GET method for filepath. The value is final after construction.
   *
   * @return file path
   */
  public String getFilePath() {
    return this.saveFolder + "/" + this.fileName;
  }

  /**
   * Flushes the data in bufferWriter to the file, and resets counter.
   *
   * @apiNote ! prob should handle exceptions differently
   *
   * @throws IOException
   */
  private void flushToFile() throws IOException {
    bw.flush();
    LOGGER.info("Flushed " + this.saveFolder);
    csvRowCount = 0;
  }

  /**
   * Gets the current time and formats it to be used as a file name.
   *
   * @return file name
   */
  private String getTimeFileName() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = currentDateTime.format(formatter);
    return formattedDateTime + ".json";
  }

}
