package com.balsamiq.reconciliation.matching.matcher;

import com.balsamiq.reconciliation.config.ConfigProperties;
import com.balsamiq.reconciliation.constant.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Utility factory class to construct {@link FieldMatcher FieldMatchers}
 */
public class FieldMatchers {

    private static final Logger logger = LoggerFactory.getLogger(FieldMatchers.class);

    protected final static String keyColumn = ConfigProperties.getStringProperty(SystemConstants.KEY_COLUMN);
    protected final static List<String> caseSensitiveColumns = Collections.unmodifiableList(ConfigProperties.getListProperty("case_sensitive.columns"));

    /**
     * Returns a field matcher that will execute a regular `equals` operation between <var>recordOneField</var> and <var>recordTwoField</var>.
     */
    public static FieldMatcher equals() {
        return (columnName, recordOneField, recordTwoField) -> recordOneField.equals(recordTwoField);
    }

    /**
     * Returns a field matcher that will execute an `equalsIgnoreCase` operation over the fields unless they are case-sensitive
     * columns or the key column. For those columns, a regular `equals` operation will be performed.
     * <p>The key column and case-sensitive ones are defined via the configuration file.</p>
     *
     * @see ConfigProperties
     */
    public static FieldMatcher ignoreCase() {
        return (columnName, recordOneField, recordTwoField) -> {
            //we do not want to do a case-insensitive comparison for the key and for case-sensitive columns
            if (!caseSensitiveColumns.contains(columnName) && !columnName.equals(keyColumn)) {
                return recordOneField.equalsIgnoreCase(recordTwoField);
            } else {
                return recordOneField.equals(recordTwoField);
            }
        };
    }

    /**
     * This field matcher will only return <code>false</code> if the expected <var>columnToMatch</var> is not equal for <var>recordOneField</var> and
     * <var>recordTwoField</var>. It will return <code>true</code> in any other case.
     * <p>
     * For example, lets' assume that <var>columnToMatch</var> param is <code>TransactionID</code>, them this matcher will
     * behave the following way:
     * <ul>
     *     <li>If <var>columnName</var> equals `TransactionID`: then the matcher will check if the value of the column
     *     `TransactionID` in <var>recordOneField</var> equals <var>recordTwoField</var>.
     *     </li>
     *     <li>If <var>columnName</var> is not `TransactionID` then this matcher will just return `true`.</li>
     * </ul>
     * </p>
     * @param columnToMatch the expected column to match. In other words, the sole column that will be compared for equality. Other columns will always return true.
     * @return <code>false</code> if the expected <var>columnToMatch</var> is not equal for <var>recordOneField</var> and
     * <code>recordTwoField</code>. It will return <code>true</code> in any other case.
     */
    public static FieldMatcher equalColumnValue(String columnToMatch) {
        return (columnName, recordOneField, recordTwoField) -> {
            if (columnToMatch == null || columnToMatch.length() == 0) {
                throw new IllegalArgumentException("columnToMatch cannot be null or empty");
            }
            if (columnToMatch.equals(columnName)) {
                return recordOneField.equals(recordTwoField);
            }
            return true;
        };
    }

}
