package gt.trading.openbook.featuregraph;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import gt.trading.openbook.MapperSingleton;
import gt.trading.openbook.featuregraph.config.Config;
import gt.trading.openbook.listeners.MarketListener;

/**
 * Runs a feature graph and adds the features to a CSV file which is then added
 * to a specified folder.
 */
public final class GraphRunner {
  private final String csvFolderName = "app/src/resources/featuregraph/reports";
  private static final Logger LOGGER = Logger
      .getLogger(GraphRunner.class.getName());

  /**
   * Runs a feature graph and adds the features to a CSV file/folder whose path
   * is specified in the constructor.
   *
   * @param sharedListener reference to a MarketListener.
   * @param fileName       the file to write CSV data to
   * @throws IOException an exception thrown if the data cannot be written
   */
  public GraphRunner(final String fileName, final MarketListener sharedListener)
      throws IOException {
    ObjectMapper mapper = MapperSingleton.getInstance();
    File jsonFile = new File(fileName);
    Config config = mapper.readValue(jsonFile, Config.class);
    String path = config.getBuilderPath();
    createReports();

    try {
      Class<?> customBuilderClass = Class.forName(path);
      LOGGER.info("Class loaded: " + customBuilderClass.getName());

      DefaultGraph graph = new DefaultGraph();
      Object builderObject = customBuilderClass.getDeclaredConstructor()
          .newInstance();
      if (builderObject instanceof GraphBuilder) {
        GraphBuilder builder = (GraphBuilder) builderObject;
        builder.build(graph);

        sharedListener.connect("wss://api.huobi.pro/ws");
        sharedListener.subscribeDepth(data -> {
          graph.onDepthEvent(data);
        });
      }
    } catch (ClassNotFoundException | NoSuchMethodException
        | IllegalAccessException | InvocationTargetException
        | InstantiationException error) {
      LOGGER.severe("Error running graph: " + error.getMessage());
    }
  }

  /**
   * Creates the "reports" folder in resources/featuregraph if it's not already
   * there.
   */
  private void createReports() {
    File reports = new File(csvFolderName);
    if (!reports.exists()) {
      reports.mkdirs();
      LOGGER.info("Reports folder created.");
    }
  }
}
