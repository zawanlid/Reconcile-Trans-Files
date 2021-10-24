package com.balsamiq.reconciliation.matching.matcher;

import com.balsamiq.reconciliation.matching.strategy.AnyOfRecordMatcher;
import com.balsamiq.reconciliation.matching.strategy.RecordModifierMatcher;
import com.balsamiq.reconciliation.matching.strategy.SimpleRecordMatcher;

import java.util.List;

public class RecordMatchers {

    public static RecordMatcher anyOf(List<RecordModifierMatcher> recordModifierMatchers) {
        return AnyOfRecordMatcher.anyOf(recordModifierMatchers);
    }

    public static RecordMatcher simple(FieldMatcher fieldMatcher) {
        return SimpleRecordMatcher.simple(fieldMatcher);
    }

}
