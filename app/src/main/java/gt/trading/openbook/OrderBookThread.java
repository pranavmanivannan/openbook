package gt.trading.openbook;

import java.util.concurrent.CountDownLatch;

import gt.trading.openbook.core.OrderBook;
import gt.trading.openbook.listeners.OrderBookListener;

public class OrderBookThread implements Runnable {
  private static final OrderBookListener orderBookListener = new OrderBookListener();
  public OrderBookThread () {

  }

  public void run() {
    new OrderBook(orderBookListener);
    CountDownLatch latch = new CountDownLatch(1);
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
