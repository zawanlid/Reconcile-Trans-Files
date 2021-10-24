package com.balsamiq.reconciliation.matching.consumer;

/**
 * Utility factory class to construct {@link RecordConsumer MatchingConsumers}
 */
public class RecordConsumers {

    /**
     * This {@link RecordConsumer} is used when the matching functionality has managed to find 2 records which were considered as <b>similar</b>.
     * This means that we need to sum 1 to the matching count.
     * @return a {@link RecordConsumer} that will manipulate the output to reflect that a matching operation has succeeded.
     */
    public static RecordConsumer matched() {
        return (record1, record2, output) -> {
            output.getMatchedRecords().incrementAndGet();
        };
    }

    /**
     * This {@link RecordConsumer} is used when the matching functionality could not find 2 records which were considered as <b>similar</b>.
     * This means that we need to sum 1 to the unmatched count for both file #1 and file #2.
     * @return a {@link RecordConsumer} that will manipulate the output to reflect that a matching operation has succeeded.
     */
    public static RecordConsumer unmatched() {
        return (record1, record2, output) -> {
            if (record1 != null) {
                output.getUnmatchedRecordsFile1().incrementAndGet();
                output.getUnmatchedListFile1().add(record1);
            } else if (record2 != null) {
                output.getUnmatchedRecordsFile2().incrementAndGet();
                output.getUnmatchedListFile2().add(record2);
            }

        };
    }

    /**
     * This {@link RecordConsumer} indicates that the matching functionality has managed to find 2 records which were considered as <b>resembling</b>.
     * This means that these 2 records need to be linked so they can be manually reconciled later on.
     * @return a {@link RecordConsumer} that will link 2 records in order to denote that they resemble and therefore they should be manually
     * compared.
     */
    public static RecordConsumer resembling() {
        return (record1, record2, output) -> {
            output.addRecordPair(record1, record2);
            output.getUnmatchedRecordsFile1().incrementAndGet();
            output.getUnmatchedRecordsFile2().incrementAndGet();
        };
    }

}
