/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package gt.trading.huobi;

import gt.trading.huobi.featuregraph.DefaultFeatureGraph;

public class App {
  public static void main(String[] args) {
    // new OrderBook();
    try {
      FeatureGraphRunner
          .run("gt/trading/huobi/featuregraph/config/example.json");

    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
