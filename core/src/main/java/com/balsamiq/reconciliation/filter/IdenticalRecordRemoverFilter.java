package com.balsamiq.reconciliation.filter;

import com.balsamiq.reconciliation.exception.FilterException;
import com.balsamiq.reconciliation.exception.UnsupportedDataException;
import com.balsamiq.reconciliation.filter.model.FilterInput;
import com.balsamiq.reconciliation.filter.model.FilterOutput;
import com.balsamiq.reconciliation.matching.consumer.RecordConsumers;
import com.balsamiq.reconciliation.matching.matcher.FieldMatchers;
import com.balsamiq.reconciliation.matching.matcher.RecordMatchers;
import com.balsamiq.reconciliation.processor.store.RecordStores;
import com.balsamiq.reconciliation.model.Record;
import com.balsamiq.reconciliation.processor.parser.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * A specific {@link Filter} used to try to find records that have identical values. This implementation iterates over
 * the second file. For each record in file #2:
 * <ul>
 *     <li>
 *         If an identical record is found in file 1: it will just remove the file 1's record from memory and discard the record from file 2.
 *     </li>
 *     <li>
 *         If no identical record is found in file 1: the record from file 2 will be loaded into memory in a separate map.
 *     </li>
 * </ul>
 * <p>Important Note: records are compared based on their headers. Two records having the exact same data but in different order
 * will match positively.</p>
 */
public class IdenticalRecordRemoverFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(IdenticalRecordRemoverFilter.class);

    @Override
    public boolean doFilter(FilterInput input, FilterOutput output) throws FilterException {
        logger.info("IdenticalRecordRemoverFilter is about to run.");
        try (CSVParser file2CSVParser = CSVParser.parse(new File(input.getFile2Path()))) { //autocloseable
            // Initialize the record store for file2. Records will be loaded next...
            output.setFile2RecordStore(RecordStores.newInstance());
            for (Record file2Record : file2CSVParser) {
                output.getTotalRecordsFile2().incrementAndGet();
                Record file1Record = output.getFile1RecordStore().get(file2Record);
                if (file1Record != null
                        && RecordMatchers
                        .simple(FieldMatchers.equals())
                        .matches(file1Record, file2Record)) {
                    RecordConsumers.matched().accept(file1Record, file2Record, output);
                    output.getFile1RecordStore().remove(file1Record);
                } else {
                    output.getFile2RecordStore().add(file2Record);
                }
            }
            logger.info("IdenticalRecordRemoverFilter has just finished running.");
            return true;
        } catch (FileNotFoundException | UnsupportedDataException e) {
            logger.error("Exception during IdenticalRecordRemoverFilter execution: " + e.getMessage());
            throw new FilterException(e.getMessage(), e);
        }
    }

}
