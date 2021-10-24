package com.balsamiq.reconciliation.filter.model;

import com.balsamiq.reconciliation.model.RecordPair;
import com.balsamiq.reconciliation.processor.store.RecordStore;
import com.balsamiq.reconciliation.model.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holder for data that will be passed as output to the processing workflow.
 */
public class FilterOutput extends FilterData {

    private AtomicInteger matchedRecords;
    private AtomicInteger totalRecordsFile1;
    private AtomicInteger unmatchedRecordsFile1;
    private AtomicInteger totalRecordsFile2;
    private AtomicInteger unmatchedRecordsFile2;

    private RecordStore file1RecordStore;
    private RecordStore file2RecordStore;
    private List<RecordPair> recordPairs;
    private List<Record> unmatchedListFile1;
    private List<Record> unmatchedListFile2;

    public FilterOutput() {
        matchedRecords = new AtomicInteger(0);
        totalRecordsFile1 = new AtomicInteger(0);
        unmatchedRecordsFile1 = new AtomicInteger(0);
        totalRecordsFile2 = new AtomicInteger(0);
        unmatchedRecordsFile2 = new AtomicInteger(0);
        recordPairs = new ArrayList<>();
        unmatchedListFile1 = new ArrayList<>();
        unmatchedListFile2 = new ArrayList<>();
    }

    public RecordStore getFile1RecordStore() {
        return file1RecordStore;
    }

    public void setFile1RecordStore(RecordStore file1RecordStore) {
        this.file1RecordStore = file1RecordStore;
    }

    public RecordStore getFile2RecordStore() {
        return file2RecordStore;
    }

    public void setFile2RecordStore(RecordStore file2RecordStore) {
        this.file2RecordStore = file2RecordStore;
    }

    public synchronized void addRecordPair(Record record1, Record record2) {
        this.recordPairs.add(new RecordPair(record1, record2));
    }

    /**
     * Returns a copy.
     */
    public List<RecordPair> getRecordPairs() {
        return new ArrayList<>(recordPairs);
    }

    public List<Record> getUnmatchedListFile1() {
        return unmatchedListFile1;
    }

    public List<Record> getUnmatchedListFile2() {
        return unmatchedListFile2;
    }

    public AtomicInteger getMatchedRecords() {
        return matchedRecords;
    }

    public AtomicInteger getTotalRecordsFile1() {
        return totalRecordsFile1;
    }

    public AtomicInteger getUnmatchedRecordsFile1() {
        return unmatchedRecordsFile1;
    }

    public AtomicInteger getTotalRecordsFile2() {
        return totalRecordsFile2;
    }

    public AtomicInteger getUnmatchedRecordsFile2() {
        return unmatchedRecordsFile2;
    }

}
