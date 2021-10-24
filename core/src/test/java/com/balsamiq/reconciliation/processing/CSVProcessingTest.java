package com.balsamiq.reconciliation.processing;

import com.balsamiq.reconciliation.exception.FilterException;
import com.balsamiq.reconciliation.filter.CustomCSVProcessingWorkflow;
import com.balsamiq.reconciliation.filter.model.FilterOutput;
import com.balsamiq.reconciliation.workflow.CSVProcessingWorkflow;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class CSVProcessingTest {

    private CSVProcessingWorkflow defaultWorkflow = CSVProcessingWorkflow.instantiate();
    private CSVProcessingWorkflow customWorkflow = CustomCSVProcessingWorkflow.omitResembling();
    private final static ClassLoader classLoader = CSVProcessingTest.class.getClassLoader();

    @Test
    public void happyPathTest() throws FilterException {
        final String filePrefix = "all_good";
        FilterOutput output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(5,output.getTotalRecordsFile1().get());
        assertEquals(5,output.getTotalRecordsFile2().get());
        assertEquals(5,output.getMatchedRecords().get());
        assertEquals(0,output.getUnmatchedRecordsFile1().get());
        assertEquals(0,output.getUnmatchedRecordsFile2().get());
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void identicalButWithCommasInFieldsWithQuotesTest() throws FilterException {
        final String filePrefix = "identical_with_commas_in_fields";
        FilterOutput output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(5,output.getTotalRecordsFile1().get());
        assertEquals(5,output.getTotalRecordsFile2().get());
        assertEquals(5,output.getMatchedRecords().get());
        assertEquals(0,output.getUnmatchedRecordsFile1().get());
        assertEquals(0,output.getUnmatchedRecordsFile2().get());
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void columnsInDifferentOrderTest() throws FilterException {
        final String filePrefix = "columns_in_different_order";
        FilterOutput output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(5,output.getTotalRecordsFile1().get());
        assertEquals(5,output.getTotalRecordsFile2().get());
        assertEquals(5,output.getMatchedRecords().get());
        assertEquals(0,output.getUnmatchedRecordsFile1().get());
        assertEquals(0,output.getUnmatchedRecordsFile2().get());
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void recordsWithDifferentSpacesTest() throws FilterException {
        final String filePrefix = "records_with_different_spaces";
        FilterOutput output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(6,output.getTotalRecordsFile1().get());
        assertEquals(6,output.getTotalRecordsFile2().get());
        assertEquals(6,output.getMatchedRecords().get());
        assertEquals(0,output.getUnmatchedRecordsFile1().get());
        assertEquals(0,output.getUnmatchedRecordsFile2().get());
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void differentSpacesAndCaseTest() throws FilterException {
        final String filePrefix = "different_spaces_and_case";
        FilterOutput output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(6,output.getTotalRecordsFile1().get());
        assertEquals(6,output.getTotalRecordsFile2().get());
        assertEquals(6,output.getMatchedRecords().get());
        assertEquals(0,output.getUnmatchedRecordsFile1().get());
        assertEquals(0,output.getUnmatchedRecordsFile2().get());
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    //abbreviations will not be detected in this processor since the abbreviation matcher is missing
    @Test
    public void differentSpacesAndCaseAndAbbreviationsTest() throws FilterException {
        String filePrefix = filePrefix = "different_spaces_and_case_and_abbreviations";
        FilterOutput output = customWorkflow.process(getFile1(filePrefix), getFile1(filePrefix));


        assertEquals(6,output.getTotalRecordsFile1().get());
        assertEquals(6,output.getTotalRecordsFile2().get());
        assertEquals(6,output.getMatchedRecords().get());
        assertEquals(0,output.getUnmatchedRecordsFile1().get());
        assertEquals(0,output.getUnmatchedRecordsFile2().get());
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }

    @Test
    public void emptyFileTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        final String filePrefix = "empty_file";
        FilterOutput output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(0, output.getTotalRecordsFile1().get());
        assertEquals(5, output.getTotalRecordsFile2().get());
        assertEquals(0, output.getMatchedRecords().get());
        assertEquals(0, output.getUnmatchedRecordsFile1().get());
        assertEquals(5, output.getUnmatchedRecordsFile2().get());
        assertEquals(0, output.getFile1RecordStore().getSize());
        assertEquals(5, output.getFile2RecordStore().getSize());
    }

    @Test
    public void onlyTheHeaderTest() throws FilterException, NoSuchFieldException, IllegalAccessException {
        final String filePrefix = "only_header";
        FilterOutput output = defaultWorkflow.process(getFile1(filePrefix), getFile2(filePrefix));

        assertEquals(5, output.getTotalRecordsFile1().get());
        assertEquals(0, output.getTotalRecordsFile2().get());
        assertEquals(0, output.getMatchedRecords().get());
        assertEquals(5, output.getUnmatchedRecordsFile1().get());
        assertEquals(0, output.getUnmatchedRecordsFile2().get());
        assertEquals(5, output.getFile1RecordStore().getSize());
        assertEquals(0, output.getFile2RecordStore().getSize());
    }



    private File getFile1(String fileName) {
        return new File(classLoader.getResource(fileName + "-file1.csv").getFile());
    }

    private File getFile2(String fileName) {
        return new File(classLoader.getResource(fileName + "-file2.csv").getFile());
    }


}
