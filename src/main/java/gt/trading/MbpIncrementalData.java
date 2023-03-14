package gt.trading;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MbpIncrementalData {

  private Long seqNum;

  private Long prevSeqNum;

  private List<PriceLevel> bids;

  private List<PriceLevel> asks;

}
