package com.balsamiq.reconciliation.matching.strategy;

import com.balsamiq.reconciliation.exception.HeaderNotFoundException;
import com.balsamiq.reconciliation.matching.matcher.RecordMatcher;
import com.balsamiq.reconciliation.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Executes a logical disjunction: evaluates multiple matchers until one of them is positive. As soon as one of all
 * the internal matchers evaluates to <code>true</code> then this matcher finishes and returns <code>true</code>. If
 * none of all the internal matchers is positive then this matcher will return <code>false</code>.
 */
public class AnyOfRecordMatcher implements RecordMatcher {

    private final static Logger logger = LoggerFactory.getLogger(AnyOfRecordMatcher.class);

    private final Iterable<RecordModifierMatcher> recordModifierMatchers;

    public AnyOfRecordMatcher(List<RecordModifierMatcher> recordModifierMatchers) {
        if (recordModifierMatchers == null) {
            throw new IllegalArgumentException("recordModifierMatchers cannot be null.");
        }
        this.recordModifierMatchers = Collections.unmodifiableList(recordModifierMatchers);
    }

    @Override
    public boolean matches(Record recordOne, Record recordTwo) {
        Record oneRecordModified = recordOne;
        Record anotherRecordModified = recordTwo;
        for (RecordModifierMatcher recordModifierMatcher : recordModifierMatchers) {
            oneRecordModified = recordModifierMatcher.modify(oneRecordModified);
            anotherRecordModified = recordModifierMatcher.modify(anotherRecordModified);
        }

        Map<String, Integer> headers = recordOne.getHeader();
        for (RecordModifierMatcher recordModifierMatcher : recordModifierMatchers) {
            boolean oneFieldUnmatched = false;
            for (String header : headers.keySet()) {
                try {
                    String recordOneField = oneRecordModified.get(header);
                    String recordTwoField = anotherRecordModified.get(header);
                    if (!recordModifierMatcher.matches(header, recordOneField, recordTwoField)) {
                        oneFieldUnmatched = true;
                        break;
                    }
                } catch (HeaderNotFoundException e) {
                    logger.debug("Exception occurred comparing {} with {}", recordOne, recordTwo, e);
                    return false;
                }
            }
            if (!oneFieldUnmatched) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a matcher that matches if the examined object matches <b>ANY</b> of the specified matchers (i.e. executes a
     * logical disjunction)
     * @param recordModifierMatchers the list of {@link RecordModifierMatcher RecordModifierMatcher} that will be evaluated.
     * @return a new instance of {@link AnyOfRecordMatcher}.
     */
    public static RecordMatcher anyOf(List<RecordModifierMatcher> recordModifierMatchers) {
        return new AnyOfRecordMatcher(recordModifierMatchers);
    }

}