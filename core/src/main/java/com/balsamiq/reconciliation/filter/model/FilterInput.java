package com.balsamiq.reconciliation.filter.model;

/**
 * Holder for data that will be passed as input to the processing workflow.
 */
public class FilterInput extends FilterData {
    private final String file1Path;
    private final String file2Path;

    public FilterInput(String file1Path, String file2Path) {
        this.file1Path = file1Path;
        this.file2Path = file2Path;
    }

    public String getFile1Path() {
        return file1Path;
    }

    public String getFile2Path() {
        return file2Path;
    }
}
