package org.spin.ams.process;

import org.compiere.process.SvrProcess;
import org.json.JSONArray;
import org.spin.ams.model.AbstractImporter;
import org.spin.ams.model.ThreadImporter;
import org.spin.ams.model.TopicsImporter;
import org.spin.ams.model.TipologyImporter;
import org.spin.ams.model.ClientsImporter;
import org.spin.ams.util.RequestUtil;

public class ImportEntityProcess extends SvrProcess {

    private String entity;

    @Override
    protected void prepare() {
        entity = getParameterAsString("AppwriteEntityType");
    }

    @Override
    protected String doIt() throws Exception {
        JSONArray data = new JSONArray(RequestUtil.fetchDataFor(entity));

        AbstractImporter importer;

        switch (entity) {
            case "Threads":
                importer = new ThreadImporter();
                break;
            case "Tipology":
                importer = new TipologyImporter();
                break;
            case "Topics":
                importer = new TopicsImporter();
                break;
            case "Clients":
                importer = new ClientsImporter();
                break;
            default:
                throw new IllegalArgumentException("Entidad no soportada: " + entity);
        }

        importer.importData(data, getCtx(), get_TrxName());

        return data.length() + " registros importados de " + entity;
    }
}
