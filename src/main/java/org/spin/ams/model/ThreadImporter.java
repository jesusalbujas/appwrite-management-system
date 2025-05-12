package org.spin.ams.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

import org.compiere.model.PO;
import org.compiere.util.DB;
import org.json.JSONObject;

public class ThreadImporter extends AbstractImporter {

    @Override
    protected String getTableName() {
        return "JAU01_Threads";
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

        po.set_ValueOfColumn("Value", json.optString("$id"));
        po.set_ValueOfColumn("Name", json.optString("name"));
        po.set_ValueOfColumn("Description", json.optString("description"));
        po.set_ValueOfColumn("Qty", json.optString("qty"));
        po.set_ValueOfColumn("QtyMin", json.optString("qtymin"));
        po.set_ValueOfColumn("Status", json.optString("status"));
        po.set_ValueOfColumn("Support_User", json.optString("supportuser"));

        String dateClosed = json.optString("DateClosed");
        if (!dateClosed.isEmpty()) {
            OffsetDateTime odt = OffsetDateTime.parse(dateClosed);
            po.set_ValueOfColumn("DateClosed", Timestamp.from(odt.toInstant()));
        }

        po.set_ValueOfColumn("Applicant_Name", json.optString("applicantname"));
        po.set_ValueOfColumn("Threads_Status", json.optString("status"));
        po.set_ValueOfColumn("url", json.optString("uri"));

        // Client
        String clientChannelCode = json.optString("clientchannel").toUpperCase();
        int clientId = getClientIdByValue(clientChannelCode);
        if (clientId > 0) {
            po.set_ValueOfColumn("JAU01_Clients_ID", clientId);
        } else {
            throw new Exception("No se encontró el cliente con código: " + clientChannelCode);
        }

        // Topic
        String topicCode = json.optString("topic").toUpperCase();
        int topicId = getTopicIdByValue(topicCode);
        if (topicId > 0) {
            po.set_ValueOfColumn("JAU01_Topic_ID", topicId);
        } else {
            throw new Exception("No se encontró el topic con código: " + topicCode);
        }

        // Tipology
        String tipologyCode = json.optString("tipology").toUpperCase();
        int tipologyId = getTipologyIdByValue(tipologyCode);
        if (tipologyId > 0) {
            po.set_ValueOfColumn("JAU01_Tipology_ID", tipologyId);
        } else {
            throw new Exception("No se encontró el tipology con código: " + tipologyCode);
        }
    }

    private int getClientIdByValue(String value) throws SQLException {
        String sql = "SELECT JAU01_Clients_ID FROM JAU01_Clients WHERE UPPER(Value) = ?";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setString(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("JAU01_Clients_ID");
                }
            }
        }
        return -1;
    }

    private int getTopicIdByValue(String value) throws SQLException {
        String sql = "SELECT JAU01_Topic_ID FROM JAU01_Topic WHERE UPPER(Value) = ?";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setString(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("JAU01_Topic_ID");
                }
            }
        }
        return -1;
    }

    private int getTipologyIdByValue(String value) throws SQLException {
        String sql = "SELECT JAU01_Tipology_ID FROM JAU01_Tipology WHERE UPPER(Value) = ?";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setString(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("JAU01_Tipology_ID");
                }
            }
        }
        return -1;
    }
}
