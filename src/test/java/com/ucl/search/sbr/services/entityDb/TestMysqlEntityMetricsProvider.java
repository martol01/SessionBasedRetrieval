package com.ucl.search.sbr.services.entityDb;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class TestMysqlEntityMetricsProvider {

    private MysqlEntityMetricsProvider provider;

    @Before
    public void init() throws SQLException {
        provider = new MysqlEntityMetricsProvider("localhost", "root", "gogaie");
    }

    @Test
    public void testDF() {
        System.out.println(provider.getEntityDocumentFrequency("/m/05qh6g"));
    }

    @Test
    public void getDocumentLength() {
        System.out.println(provider.getDocumentLength("clueweb12-0000tw-00-00008"));
    }

}
