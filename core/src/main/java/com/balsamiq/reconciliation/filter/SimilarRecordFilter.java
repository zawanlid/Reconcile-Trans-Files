package com.balsamiq.reconciliation.filter;

import com.balsamiq.reconciliation.filter.model.FilterOutput;
import com.balsamiq.reconciliation.matching.matcher.RecordMatcher;
import com.balsamiq.reconciliation.matching.strategy.RecordModifierMatchers;
import com.balsamiq.reconciliation.model.Record;
import com.balsamiq.reconciliation.matching.matcher.RecordMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.balsamiq.reconciliation.matching.consumer.RecordConsumers.matched;

/**
 * A specific {@link Filter} used to try to detect records that although not really identical they are so similar that can
 * safely be considered as equal. This implementation relies on {@link RecordMatcher} in order to use different strategies
 * to match records.
 * @see RecordMatchers
 * @see RecordModifierMatchers
 */
public class SimilarRecordFilter extends RecordMatcherConsumerFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(SimilarRecordFilter.class);

    private final RecordMatcher recordMatcher;

    public SimilarRecordFilter(RecordMatcher recordMatcher) {
        if (recordMatcher == null) {
            throw new IllegalArgumentException("recordMatcher cannot be null");
        }
        this.recordMatcher = recordMatcher;
    }

    @Override
    public void accept(Record record1, Record record2, FilterOutput output) {
        matched().accept(record1, record2, output);
    }

    @Override
    public boolean matches(Record recordOne, Record recordTwo) {
        return recordMatcher.matches(recordOne, recordTwo);
    }

}
