package gt.trading;

import java.util.ArrayList;

import gt.trading.Buckets.TradeDetailData;

import gt.trading.Listener.MarketListener;

public class TradeEvent {
  /**
   * Trade detail constructor.
   * 
   * @param listener  takes in a MarketListener object.
   */
  public TradeEvent(MarketListener listener) {
    ArrayList<TradeDetailData> list = new ArrayList<>();
    listener.subscribeTradeDetail(data -> {
      if (list.size() <= 1000) {
        list.add(data);
      }
      System.out.println(data);
    });
  }
}