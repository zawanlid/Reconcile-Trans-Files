package com.balsamiq.reconciliation.filter.model;

import com.balsamiq.reconciliation.config.ConfigProperties;
import com.balsamiq.reconciliation.constant.SystemConstants;

/**
 * Abstract class to be implemented by concrete classes to carry data for filters.
 * @see FilterInput
 * @see FilterOutput
 */
public abstract class FilterData {
    public final String KEY_COLUMN_NAME = ConfigProperties.getStringProperty(SystemConstants.KEY_COLUMN);
}
