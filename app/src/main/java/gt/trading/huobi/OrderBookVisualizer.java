package gt.trading.huobi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gt.trading.huobi.models.OrderBookData;
import gt.trading.huobi.models.PriceLevel;

/**
 * A visualizer for the order book of a cryptocurrency exchange, which displays
 * the top 10 bids and asks in a graphical format.
 */
public class OrderBookVisualizer extends JFrame {
  private static final int TEN = 10;
  private static final int HUNDRED = 100;

  private static final int VISUALIZER_WIDTH = 400;
  private static final int VISUALIZER_HEIGHT = 800;

  private static final int BOX_PANEL_HEIGHT = 20;

  private static final Dimension PREF_DIMENSION = new Dimension(150, 20);
  private static final Color GREEN = new Color(0, 153, 51);
  private static final Color RED = new Color(204, 0, 0);

  private JPanel bidsPanel;
  private JPanel asksPanel;

  private List<PriceLevel> askData = null;
  private List<PriceLevel> bidData = null;

  /**
   * Constructs a new instance of the OrderBookVisualizer.
   */
  public OrderBookVisualizer() {
    // Initialize the frame
    super("Orderbook Visualizer");
    setSize(VISUALIZER_WIDTH, VISUALIZER_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create the bids panel
    bidsPanel = new JPanel();
    bidsPanel.setLayout(new BoxLayout(bidsPanel, BoxLayout.Y_AXIS));
    bidsPanel.setBorder(BorderFactory.createTitledBorder("Bids"));

    JScrollPane bidsScrollPane = new JScrollPane(bidsPanel);
    bidsScrollPane
        .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    // Create the asks panel
    asksPanel = new JPanel();
    asksPanel.setLayout(new BoxLayout(asksPanel, BoxLayout.Y_AXIS));
    asksPanel.setBorder(BorderFactory.createTitledBorder("Asks"));

    JScrollPane asksScrollPane = new JScrollPane(asksPanel);
    asksScrollPane
        .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    // Add the panels to the frame
    getContentPane()
        .setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    getContentPane().add(asksScrollPane);
    getContentPane().add(bidsScrollPane);
  }

  /**
   * Creates a panel that represents an order.
   *
   * @param totalAmount the total amount of the order
   * @param amount      the amount of the order at the given price level
   * @param price       the price of the order
   * @param isBid       a boolean indicating whether the order is a bid (true)
   *                    or an ask (false)
   * @return a new JPanel representing the order
   */
  private JPanel createOrderPanel(final double totalAmount, final double amount,
      final double price, final boolean isBid) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel priceLabel = new JLabel(String.format("%.2f", price));
    priceLabel.setHorizontalAlignment(JLabel.LEFT);
    panel.add(priceLabel, BorderLayout.WEST);

    JPanel box = new JPanel() {
      @Override
      public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        int barWidth = (int) Math.max(1, HUNDRED * Math.log10(totalAmount));
        int boxHeight = BOX_PANEL_HEIGHT;
        int x = 0;
        int y = 0;
        int width = barWidth;
        int height = boxHeight;

        g.setColor(isBid ? GREEN : RED);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
      }
    };
    box.setPreferredSize(PREF_DIMENSION);

    JLabel sizeLabel = new JLabel(
        String.format("%.4f (%.2f)", amount, totalAmount));
    sizeLabel.setHorizontalAlignment(JLabel.RIGHT);
    panel.add(sizeLabel, BorderLayout.EAST);

    panel.add(box, BorderLayout.CENTER);

    return panel;
  }

  /**
   * Updates the order book with the given order.
   *
   * @param newData the OrderBookData to update the order book with.
   */
  public void updateOrderBook(final OrderBookData newData) {

    if (newData.getAsks().size() >= TEN) {
      askData = newData.getAsks().subList(0, TEN);
    } else {
      if (askData != null) {
        askData.addAll(newData.getAsks());
        if (askData.size() > TEN) {
          askData = askData.subList(0, TEN);
        }
      } else {
        askData = newData.getAsks();
      }
    }

    if (newData.getBids().size() >= TEN) {
      bidData = newData.getBids().subList(0, TEN);
    } else {
      if (bidData != null) {
        bidData.addAll(newData.getBids());
        if (bidData.size() > TEN) {
          bidData = bidData.subList(0, TEN);
        }
      } else {
        bidData = newData.getBids();
      }
    }

    List<PriceLevel> askLevels = askData;
    List<PriceLevel> bidLevels = bidData;

    // Sort the ask levels by price in ascending order
    askLevels.sort(Comparator.comparing(PriceLevel::getPrice));

    // Get the top 10 ask levels
    askLevels = askLevels.subList(0, Math.min(TEN, askLevels.size()));

    // Sort the bid levels by price in descending order
    bidLevels.sort(Comparator.comparing(PriceLevel::getPrice));
    // Get the top 10 bid levels
    bidLevels = bidLevels.subList(0, Math.min(TEN, bidLevels.size()));

    // Clear the existing panels
    asksPanel.removeAll();
    bidsPanel.removeAll();

    // Add the ask panels
    double cumulativeAskAmount = 0;
    for (PriceLevel ask : askLevels) {
      double price = ask.getPrice();
      double amount = ask.getAmount();
      cumulativeAskAmount += amount;
      JPanel askPanel = createOrderPanel(cumulativeAskAmount, amount, price,
          false);
      asksPanel.add(askPanel, 0);
    }

    // Add the bid panels
    double cumulativeBidAmount = 0;
    for (int i = bidLevels.size() - 1; i >= 0; i--) {
      PriceLevel bid = bidLevels.get(i);
      double price = bid.getPrice();
      double amount = bid.getAmount();
      cumulativeBidAmount += amount;
      JPanel bidPanel = createOrderPanel(cumulativeBidAmount, amount, price,
          true);
      bidsPanel.add(bidPanel);
    }

    bidData.removeIf(level -> level.getAmount() < 0.0001);
    askData.removeIf(level -> level.getAmount() < 0.0001);
  }
}
