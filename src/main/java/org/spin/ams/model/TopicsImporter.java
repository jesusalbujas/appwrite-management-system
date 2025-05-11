package org.spin.ams.model;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import org.compiere.model.PO;
import org.json.JSONObject;

public class TopicsImporter extends AbstractImporter {

    @Override
    protected String getTableName() {
        return "JAU01_Topic";
    }

    @Override
    protected void mapFields(JSONObject json, PO po) throws Exception {
    	po.set_ValueOfColumn("AD_Client_ID", 11);
        po.set_ValueOfColumn("AD_Org_ID", 50006);

        String createdAt = json.optString("createdAt");
        if (!createdAt.isEmpty()) {
            OffsetDateTime odt = OffsetDateTime.parse(createdAt);
            po.set_ValueOfColumn("Created", Timestamp.from(odt.toInstant()));
        }

        String updatedAt = json.optString("updatedAt");
        if (!updatedAt.isEmpty()) {
            OffsetDateTime odt = OffsetDateTime.parse(updatedAt);
            po.set_ValueOfColumn("Updated", Timestamp.from(odt.toInstant()));
        }

        po.set_ValueOfColumn("CreatedBy", 100);
        po.set_ValueOfColumn("UpdatedBy", 100);
        po.set_ValueOfColumn("IsActive", "Y");
        po.set_ValueOfColumn("UUID", getUuid());
        
        po.set_ValueOfColumn("Value", json.optString("code"));
        po.set_ValueOfColumn("Name", json.optString("name"));
    }
}