package com.balsamiq.reconciliation.web.mapper;

import com.balsamiq.reconciliation.filter.model.FilterOutput;
import com.balsamiq.reconciliation.web.model.ReconciliationResult;
/**
 * Build response object.
 */
public class FilterOutputMapper {

    public static ReconciliationResult toReconciliationResult(FilterOutput filterOutput) {
        ReconciliationResult.Builder builder = new ReconciliationResult.Builder();

        builder.setMatchingRecords(filterOutput.getMatchedRecords().get())
                .setTotalRecordsFile1(filterOutput.getTotalRecordsFile1().get())
                .setTotalRecordsFile2(filterOutput.getTotalRecordsFile2().get())
                .setUnmatchedRecordsFile1(filterOutput.getUnmatchedRecordsFile1().get())
                .setUnmatchedRecordsFile2(filterOutput.getUnmatchedRecordsFile2().get())
                .setRecordPairs(filterOutput.getRecordPairs())
                .setUnmatchedListFile1(filterOutput.getUnmatchedListFile1())
                .setUnmatchedListFile2(filterOutput.getUnmatchedListFile2());

        return builder.build();
    }

}
