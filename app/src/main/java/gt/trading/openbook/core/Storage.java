package gt.trading.openbook.core;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import gt.trading.openbook.models.DepthData;
import gt.trading.openbook.models.TradeData;
import gt.trading.openbook.models.OrderBookData;

public class Storage {
  private LocalStorage<DepthData> depthLocalStorage;
  private LocalStorage<TradeData> tradeLocalStorage;
  private LocalStorage<OrderBookData> orderBookLocalStorage;

  private static final Logger LOGGER = Logger
      .getLogger(Storage.class.getName());

  private static final int INTERVAL_MS = 300000;

  /**
   * Constructor of Storage, creates 3 LocalStorage instances, and setup to save
   * to S3 every 5 minutes.
   */
  public Storage() {
    try {
      depthLocalStorage = new LocalStorage.Builder<DepthData>(
          "app/storageData/depthData").build();

      tradeLocalStorage = new LocalStorage.Builder<TradeData>(
          "app/storageData/tradeData").build();

      orderBookLocalStorage = new LocalStorage.Builder<OrderBookData>(
          "app/storageData/orderBookData").build();

      Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        public void run() {
          uploadData(depthLocalStorage.getFilePath());
          uploadData(tradeLocalStorage.getFilePath());
          uploadData(orderBookLocalStorage.getFilePath());
          LOGGER.info("S3 uploaded!");
        }
      };
      timer.scheduleAtFixedRate(task, 0, INTERVAL_MS);

    } catch (Exception e) {
      LOGGER.severe(e.getMessage());
    }

  }

  /**
   * Callback function passed to listener to be called whenever there is a depth
   * event.
   *
   * @param data
   */
  public void onDepthEvent(final DepthData data) {
    depthLocalStorage.onEvent(data);
  }

  /**
   * Callback function passed to listener to be called whenever there is a trade
   * event.
   *
   * @param data
   */
  public void onTradeEvent(final TradeData data) {
    tradeLocalStorage.onEvent(data);
  }

  /**
   * Callback function passed to listener to be called whenever there is an
   * orderBook event.
   *
   * @param data
   */
  public void onOrderBookEvent(final OrderBookData data) {
    orderBookLocalStorage.onEvent(data);
  }

  /**
   * Uploads data to S3. Local and remote filename are the same.
   *
   * @param filename
   */
  public void uploadData(final String filename) {
    String bucketname = "huobi-openbook";
    String filepath = filename;

    S3Client client = S3Client.builder().credentialsProvider(null)
        .region(Region.US_EAST_1).build();

    PutObjectRequest request = PutObjectRequest.builder().bucket(bucketname)
        .key(filename).build();

    client.putObject(request, RequestBody.fromFile(new File(filepath)));
  }
}