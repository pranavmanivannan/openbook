package gt.trading.openbook.featuregraph.features;

import gt.trading.openbook.featuregraph.Feature;
import gt.trading.openbook.featuregraph.FeatureGraph;
import gt.trading.openbook.models.DepthData;

public final class BestAskFeature implements Feature {
  private Double value = Double.NaN;
  private final String name = "BestAskFeature";

  /**
   * Constructs a new bestAskFeature, registers the feature into the graph, and
   * invokes the callback function to ensure it is registered within the graph.
   *
   * @param featureGraph the featureGraph to register the BestAskFeature to
   */
  public BestAskFeature(final FeatureGraph featureGraph) {
    featureGraph.registerFeature(this, true);
    featureGraph.registerDepthEventCallback(this, this::onDepthEvent);
  }

  /**
   * Callback function that is invoked when a depthEvent occurs. Compares the
   * new ask value with the current value and updates the value if they are not
   * equal.
   *
   * @param depthData the depthData used to get the latest best ask
   */
  @Override
  public boolean onDepthEvent(final DepthData depthData) {
    if (Double.compare(depthData.getAsk(), value) != 0) {
      value = depthData.getAsk();
      return true;
    }

    return false;
  }

  /**
   * Returns the value of the feature.
   */
  @Override
  public Double getValue() {
    return value;
  }

  /**
   * Returns the name of the feature.
   */
  @Override
  public String toString() {
    return name;
  }
}
