package gt.trading.huobi.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * OrderBookData class represents a snapshot of the order book for an
 * instrument. It includes information such as the action, sequence numbers, and
 * lists of bids and asks.
 */
public final class OrderBookData {
  @JsonIgnore
  private String action;
  private long seqNum;
  private long prevSeqNum;
  private List<PriceLevel> bids;
  private List<PriceLevel> asks;

  /**
   * Constructs an empty OrderBookData for use in serialization.
   */
  public OrderBookData() {
    return;
  }

  /**
   * Constructs an OrderBookData using the builder.
   *
   * @param builder the builder object
   */
  private OrderBookData(final Builder builder) {
    action = builder.action;
    seqNum = builder.seqNum;
    prevSeqNum = builder.prevSeqNum;
    bids = builder.bids;
    asks = builder.asks;
  }

  /**
   * Returns the action of the event.
   *
   * @return the action string
   */
  public String getAction() {
    return action;
  }

  /**
   * Sets the action of the event when handling the event in the listener.
   *
   * @param newAction the new action string
   */
  public void setAction(final String newAction) {
    action = newAction;
  }

  /**
   * Returns the sequence number of the order book snapshot.
   *
   * @return the sequence number
   */
  public long getSeqNum() {
    return seqNum;
  }

  /**
   * Returns the previous sequence number of the order book snapshot.
   *
   * @return the previous sequence number
   */
  public long getPrevSeqNum() {
    return prevSeqNum;
  }

  /**
   * Returns the list of bids in the order book snapshot.
   *
   * @return the list of bids
   */
  public List<PriceLevel> getBids() {
    return bids;
  }

  /**
   * Returns the list of asks in the order book snapshot.
   *
   * @return list of asks
   */
  public List<PriceLevel> getAsks() {
    return asks;
  }

  /**
   * Creates a new builder for OrderBookData.
   *
   * @return Builder object
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for OrderBookData.
   *
   * Provides a way to create an OrderBookData object using the builder pattern.
   */
  public static final class Builder {
    private String action;
    private long seqNum;
    private long prevSeqNum;
    private List<PriceLevel> bids;
    private List<PriceLevel> asks;

    /**
     * Constructs an empty Builder for use in serialization.
     */
    private Builder() {
      return;
    }

    /**
     * Sets the action of the order book snapshot for this builder.
     *
     * @param newAction action string
     * @return the current Builder instance
     */
    public Builder action(final String newAction) {
      action = newAction;
      return this;
    }

    /**
     * Sets the sequence number of the order book snapshot for this builder.
     *
     * @param newSeqNum sequence number
     * @return the current Builder instance
     */
    public Builder seqNum(final long newSeqNum) {
      seqNum = newSeqNum;
      return this;
    }

    /**
     * Sets the previous sequence number of the order book snapshot for this
     * builder.
     *
     * @param newPrevSeqNum previous sequence number
     * @return the current Builder instance
     */
    public Builder prevSeqNum(final long newPrevSeqNum) {
      prevSeqNum = newPrevSeqNum;
      return this;
    }

    /**
     * Sets the list of bids in the order book snapshot for this builder.
     *
     * @param newBids list of bids
     * @return the current Builder instance
     */
    public Builder bids(final List<PriceLevel> newBids) {
      bids = newBids;
      return this;
    }

    /**
     * Sets the list of asks in the order book snapshot for this builder.
     *
     * @param newAsks list of asks
     * @return the current Builder instance
     */
    public Builder asks(final List<PriceLevel> newAsks) {
      asks = newAsks;
      return this;
    }

    /**
     * Constructs an OrderBookData instance using the values set in this
     * builder.
     *
     * @return a new OrderBookData instance
     */
    public OrderBookData build() {
      return new OrderBookData(this);
    }
  }
}
