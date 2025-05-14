package org.spin.ams.model;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;
import java.util.Properties;

public abstract class AbstractImporter {

    protected abstract String getTableName();
    protected abstract void mapFields(JSONObject json, PO po) throws Exception;
    protected abstract PO lookupExistingRecord(JSONObject json);

    public void importData(JSONArray data, Properties ctx, String trxName) throws Exception {
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            PO po = lookupExistingRecord(item);

            if (po == null) {
                po = MTable.get(ctx, getTableName()).getPO(0, trxName);
            }

            mapFields(item, po);
            po.saveEx();
        }
    }

    protected String getUuid() {
        return UUID.randomUUID().toString();
    }
}
