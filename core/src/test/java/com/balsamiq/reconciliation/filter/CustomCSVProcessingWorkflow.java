package com.balsamiq.reconciliation.filter;

import com.balsamiq.reconciliation.config.ConfigProperties;
import com.balsamiq.reconciliation.matching.matcher.RecordMatchers;
import com.balsamiq.reconciliation.workflow.CSVProcessingWorkflow;
import static com.balsamiq.reconciliation.matching.strategy.RecordModifierMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Custom workflow to test different configurations other than the default one.
 */
public class CustomCSVProcessingWorkflow extends CSVProcessingWorkflow {


    public CustomCSVProcessingWorkflow(List<Filter> filters) {
        super(filters);
    }

    public static CustomCSVProcessingWorkflow omitResembling() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new CSVLoadingFilter());
        filters.add(new IdenticalRecordRemoverFilter());
        filters.add(new SimilarRecordFilter(RecordMatchers.anyOf(Arrays.asList(ignoreSpaces(), ignoreCase()))));
        filters.add(new ReportDataOrganizationFilter());
        return new CustomCSVProcessingWorkflow(filters);
    }
}