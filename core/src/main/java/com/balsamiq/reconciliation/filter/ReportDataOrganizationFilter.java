package com.balsamiq.reconciliation.filter;

import com.balsamiq.reconciliation.exception.FilterException;
import com.balsamiq.reconciliation.filter.model.FilterInput;
import com.balsamiq.reconciliation.filter.model.FilterOutput;
import com.balsamiq.reconciliation.matching.consumer.RecordConsumers;
import com.balsamiq.reconciliation.model.Record;
import com.balsamiq.reconciliation.model.RecordPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A specific {@link Filter} that will organize existing data for its final presentation to the user.
 */
public class ReportDataOrganizationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ReportDataOrganizationFilter.class);

    @Override
    public boolean doFilter(FilterInput input, FilterOutput output) throws FilterException {
        output.getFile1RecordStore().forEach(record1 -> RecordConsumers.unmatched().accept(record1, null, output));
        output.getFile2RecordStore().forEach(record2 -> RecordConsumers.unmatched().accept(null, record2, output));
        Collections.sort(output.getRecordPairs(), new RecordPairComparator());
        return true;
    }

    /**
     * Records will be ordered by row number.
     */
    private static class RecordPairComparator implements Comparator<RecordPair> {

        @Override
        public int compare(RecordPair o1, RecordPair o2) {
            if (o1.getRecord1() != null) {
                if (o2.getRecord1() != null) {
                    return doCompare(o1.getRecord1(), o2.getRecord1());
                } else {
                    return doCompare(o1.getRecord1(), o2.getRecord2());
                }
            } else {
                if (o2.getRecord1() != null) {
                    return doCompare(o1.getRecord2(), o2.getRecord1());
                } else {
                    return doCompare(o1.getRecord2(), o2.getRecord2());
                }
            }
        }

        private int doCompare(Record record1, Record record2) {
            Integer row1 = record1.getRow();
            Integer row2 = record2.getRow();
            return row1.compareTo(row2);
        }
    }

}
