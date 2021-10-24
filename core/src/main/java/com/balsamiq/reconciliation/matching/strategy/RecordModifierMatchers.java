package com.balsamiq.reconciliation.matching.strategy;

import com.balsamiq.reconciliation.matching.matcher.FieldMatcher;
import com.balsamiq.reconciliation.matching.matcher.FieldMatchers;
import com.balsamiq.reconciliation.matching.modifier.RecordModifier;
import com.balsamiq.reconciliation.matching.modifier.RecordModifiers;

/**
 * A RecordModifierMatcher has the opportunity to virtually apply modifications to a record before carrying out an actual matching operation
 * <p>RecordModifierMatchers must implement both the {@link RecordModifier} and the {@link FieldMatcher} interface.</p>
 */
public class RecordModifierMatchers {

    public static RecordModifierMatcher ignoreSpaces() {
        return SimpleRecordModifierMatcher.simple(RecordModifiers.normalizeSpaces(), FieldMatchers.equals());
    }

    public static RecordModifierMatcher ignoreCase() {
        return SimpleRecordModifierMatcher.simple(RecordModifiers.toUpperCase(), FieldMatchers.ignoreCase());
    }

    public static RecordModifierMatcher equal(String columnToMatch) {
        return SimpleRecordModifierMatcher.simple(RecordModifiers.doNothing(), FieldMatchers.equalColumnValue(columnToMatch));
    }

}

