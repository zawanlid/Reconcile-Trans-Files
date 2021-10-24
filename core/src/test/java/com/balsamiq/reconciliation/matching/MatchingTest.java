package com.balsamiq.reconciliation.matching;

import com.balsamiq.reconciliation.exception.ReconciliationException;
import com.balsamiq.reconciliation.matching.matcher.FieldMatcher;
import com.balsamiq.reconciliation.matching.matcher.FieldMatchers;
import com.balsamiq.reconciliation.matching.modifier.RecordModifier;
import com.balsamiq.reconciliation.matching.modifier.RecordModifiers;
import com.balsamiq.reconciliation.matching.strategy.RecordModifierMatcher;
import com.balsamiq.reconciliation.matching.strategy.RecordModifierMatchers;
import com.balsamiq.reconciliation.model.Record;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MatchingTest {

    @Test
    public void fieldMatchingTest() {
        FieldMatcher matcher = null;

        matcher = FieldMatchers.equals();
        assertFalse(matcher.matches("columnA", "Corp.", "Corporation"));
        assertTrue(matcher.matches("columnA", "123Aa", "123Aa"));
        assertFalse(matcher.matches("columnA", "123Aa", "123AA"));
        assertFalse(matcher.matches("columnA", "123Aa", "123 AA"));

        matcher = FieldMatchers.ignoreCase();
        assertFalse(matcher.matches("columnA", "Corp.", "Corporation"));
        assertTrue(matcher.matches("columnA", "123Aa", "123Aa"));
        assertTrue(matcher.matches("columnA", "123Aa", "123AA"));
        assertFalse(matcher.matches("columnA", "12 3Aa", "123AA"));

        matcher = FieldMatchers.equalColumnValue("aColumn");
        assertTrue(matcher.matches("columnA", "Corp.", "Corporation"));
        assertFalse(matcher.matches("aColumn", "Corp.", "Corporation"));
        assertTrue(matcher.matches("columnA", "123Aa", "123Aa"));
        assertTrue(matcher.matches("aColumn", "123Aa", "123Aa"));
        assertFalse(matcher.matches("aColumn", "123Aa", "123AA"));
        assertTrue(matcher.matches("aColumn", "12 3Aa", "12 3Aa"));
        assertTrue(matcher.matches("anotherColumn", "12 3Aa", "12 3Aa"));
    }

    @Test
    public void recordModifierTest() {
        RecordModifier matcher = RecordModifiers.toUpperCase();
        Record recordA = createRecord("A a", "B");
        Record recordB = createRecord("A A", "B");
        Record recordC = createRecord("A A", "B", "C");
        Record recordD = createRecord("A    A", "   B", "C");
        assertEquals(recordB, matcher.modify(recordA));
        assertEquals(recordB, matcher.modify(recordB));
        assertEquals(recordB, matcher.modify(recordB));
        assertNotEquals(recordC, matcher.modify(recordD));

        try {
            assertEquals(recordC, matcher.modify(recordB));
            fail("Should have thrown");
        } catch (ReconciliationException e) {
            assertEquals("Mapping for 'column2' not found, expected one of '[column1, column0]'", e.getMessage());
        }

        matcher = RecordModifiers.normalizeSpaces();
        assertEquals(recordC, matcher.modify(recordD));
        assertNotEquals(recordD, matcher.modify(recordC));
    }

    @Test
    public void recordRevisitorTest() {
        RecordModifierMatcher modifierMatcher = RecordModifierMatchers.ignoreCase();
        Record recordA = createRecord("A a", "B");
        Record recordB = createRecord("A A", "B");
        Record recordC = createRecord("A A", "B", "C");
        Record recordD = createRecord("A    A", "   B", "C");
        assertEquals(recordB, modifierMatcher.modify(recordA));
        assertEquals(recordB, modifierMatcher.modify(recordB));
        assertEquals(recordB, modifierMatcher.modify(recordB));
        assertNotEquals(recordC, modifierMatcher.modify(recordD));

        try {
            assertEquals(recordC, modifierMatcher.modify(recordB));
            fail("Should have thrown");
        } catch (ReconciliationException e) {
            assertEquals("Mapping for 'column2' not found, expected one of '[column1, column0]'", e.getMessage());
        }

        modifierMatcher = RecordModifierMatchers.ignoreSpaces();
        assertEquals(recordC, modifierMatcher.modify(recordD));
        assertNotEquals(recordD, modifierMatcher.modify(recordC));
    }

    private Record createRecord(String... values) {
        Map<String, Integer> header = new HashMap<>();
        for (int i = values.length - 1; i>=0; i--) {
            header.put("column" + i, i);
        }
        return new Record(1, Arrays.asList(values), header);
    }

}
