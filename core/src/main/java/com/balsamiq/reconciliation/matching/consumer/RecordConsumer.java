package com.balsamiq.reconciliation.matching.consumer;

import com.balsamiq.reconciliation.filter.model.FilterOutput;
import com.balsamiq.reconciliation.model.Record;

/**
 * Matching records will be pass through a {@link RecordConsumer} instance in order to decide what should be done with them.
 *
 * @see RecordConsumers
 */
@FunctionalInterface
public interface RecordConsumer {

    void accept(Record record1, Record record2, FilterOutput output);

}