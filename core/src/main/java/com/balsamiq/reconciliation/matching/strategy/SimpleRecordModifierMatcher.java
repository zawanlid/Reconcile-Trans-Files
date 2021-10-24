package com.balsamiq.reconciliation.matching.strategy;

import com.balsamiq.reconciliation.matching.matcher.FieldMatcher;
import com.balsamiq.reconciliation.matching.modifier.RecordModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleRecordModifierMatcher<T extends RecordModifier, S extends FieldMatcher> extends RecordModifierMatcher {

    private final static Logger logger = LoggerFactory.getLogger(SimpleRecordModifierMatcher.class);

    public SimpleRecordModifierMatcher(T recordModifier, S fieldMatcher) {
        super(recordModifier, fieldMatcher);
    }

    public static <T extends RecordModifier, S extends FieldMatcher> SimpleRecordModifierMatcher simple(T recordModifier, S fieldMatcher) {
        return new SimpleRecordModifierMatcher<>(recordModifier, fieldMatcher);
    }

}
