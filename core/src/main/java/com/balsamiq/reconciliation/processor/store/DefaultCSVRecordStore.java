package com.balsamiq.reconciliation.processor.store;

import com.balsamiq.reconciliation.config.ConfigProperties;
import com.balsamiq.reconciliation.constant.SystemConstants;

/**
 * This is the default {@link RecordStore} implementation. Records are processed in memory during the reconciliation
 * process. Records can be added, removed, retrieved and iterated from this store.
 */
public class DefaultCSVRecordStore extends AbstractCSVRecordStore {

    private final static String keyColumn = ConfigProperties.getStringProperty(SystemConstants.KEY_COLUMN);

    public DefaultCSVRecordStore() {
        super(keyColumn);
    }

}
