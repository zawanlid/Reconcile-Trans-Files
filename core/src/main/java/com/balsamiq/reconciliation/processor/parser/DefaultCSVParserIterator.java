package com.balsamiq.reconciliation.processor.parser;

import com.balsamiq.reconciliation.matching.modifier.RecordModifiers;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Default iteration mechanism used by {@link CSVParser} in order to detect records one by one.
 */
public class DefaultCSVParserIterator extends AbstractCSVParserIterator {

    public DefaultCSVParserIterator(Scanner scanner) throws FileNotFoundException {
        super(scanner, RecordModifiers.addBlankValues(), RecordModifiers.doNothing());
    }
}
