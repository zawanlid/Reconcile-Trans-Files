package com.balsamiq.reconciliation.workflow;

import com.balsamiq.reconciliation.filter.*;
import com.balsamiq.reconciliation.config.ConfigProperties;
import com.balsamiq.reconciliation.constant.SystemConstants;
import com.balsamiq.reconciliation.exception.FilterException;
import com.balsamiq.reconciliation.exception.ReconciliationException;
import com.balsamiq.reconciliation.filter.model.FilterInput;
import com.balsamiq.reconciliation.filter.model.FilterOutput;
import com.balsamiq.reconciliation.matching.matcher.RecordMatchers;
import com.balsamiq.reconciliation.matching.strategy.RecordModifierMatcher;
import com.balsamiq.reconciliation.matching.strategy.RecordModifierMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This is the entry point of core module and control workflow. It has a filter list to apply to the reconciliation files to match records
 * according to provided strategies.
 */
public class CSVProcessingWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(CSVProcessingWorkflow.class);

    private final List<Filter> filters;

    private final static String keyColumn = ConfigProperties.getStringProperty(SystemConstants.KEY_COLUMN);
    private final static List<String> secondaryColumns = ConfigProperties.getListProperty("secondary.columns");

    public CSVProcessingWorkflow(List<Filter> filters) {
        this.filters = Collections.unmodifiableList(filters);
        logger.info("CSV processing workflow is configured to run with the following filters {}.",
                filters.stream()
                .map( f -> f.getClass().getSimpleName() )
                .collect( Collectors.joining( ", " ) ));
    }

    public static CSVProcessingWorkflow instantiate() {
        List<Filter> filters = new ArrayList<>();
        // CSVLoadingFilter: loads the first file entirely to memory.
        filters.add(new CSVLoadingFilter());

        // IdenticalRecordRemoverFilter: iterates over the second file.
        filters.add(new IdenticalRecordRemoverFilter());

        // SimilarRecordFilter: A configurable filter that compares unmatched records in both maps trying to match them with provided strategy.
        List<RecordModifierMatcher> recordModifierMatchers = new ArrayList<>();
        recordModifierMatchers.add(RecordModifierMatchers.ignoreSpaces());
        recordModifierMatchers.add(RecordModifierMatchers.ignoreCase());
        filters.add(new SimilarRecordFilter(RecordMatchers.anyOf(recordModifierMatchers)));

        // ResemblingRecordFilter: A configurable filter that compares remaining records in both maps trying to find resemblance based on the provided strategy.
        List<RecordModifierMatcher> recordModifierMatchers_2 = new ArrayList<>();
        recordModifierMatchers_2.add(RecordModifierMatchers.equal(ConfigProperties.getStringProperty(SystemConstants.KEY_COLUMN)));
        filters.add(new ResemblingRecordFilter(RecordMatchers.anyOf(recordModifierMatchers_2)));

        // ReportDataOrganizationFilter
        filters.add(new ReportDataOrganizationFilter());

        return new CSVProcessingWorkflow(filters);
    }

    public static CSVProcessingWorkflow instantiate(List<Filter> filters) {
        return new CSVProcessingWorkflow(filters);
    }

    public FilterOutput process(File file1, File file2) throws ReconciliationException {
        FilterInput filterInput = new FilterInput(file1.getAbsolutePath(), file2.getAbsolutePath());
        FilterOutput filterOutput = new FilterOutput();

        filters.forEach((m) -> {
            try {
                m.doFilter(filterInput,filterOutput);
            } catch (FilterException e) {
                e.printStackTrace();
            }
        });

        return filterOutput;
    }

}
