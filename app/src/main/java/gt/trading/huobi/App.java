package gt.trading.huobi;

/**
 * The main class for the order book application.
 *
 * This class sets up the application and starts the user interface. It creates
 * an instance of the order book and registers it with the user interface.
 */
public final class App {
  private App() {
    return;
  }

  /**
   * The main method for running the order book.
   *
   * This method initializes an order book and starts processing order data. The
   * order book maintains a list of buy and sell orders for the BTC/USDT
   * instrument and matches them according to their prices and timestamps. It
   * also keeps track of the current bid and ask prices for the instrument.
   *
   * @param args an array of command line arguments (not used)
   */
  public static void main(final String[] args) {
    System.out.println("Hello World");
  }
}
