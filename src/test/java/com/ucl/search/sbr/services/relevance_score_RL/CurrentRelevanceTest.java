package com.ucl.search.sbr.services.relevance_score_RL;

import com.ucl.search.sbr.domain.EntityInteraction;
import com.ucl.search.sbr.services.entityDb.MysqlEntityMetricsProvider;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;
import junit.framework.TestCase;
import org.junit.Test;

import java.sql.SQLException;

public class CurrentRelevanceTest extends TestCase {

    @Test
    public void testCurrentRelevance() throws SQLException {

         /* preparing the test data; here I used the first query in the first session from TREC14 */
        EntityInteraction entInteraction = new EntityInteraction();
        Session[] sessions = entInteraction.getSessions();
        Interaction query = sessions[0].getInteractions()[0];

        MysqlEntityMetricsProvider mysqlEntityMetricsProvider = new MysqlEntityMetricsProvider("localhost", "root", "gogaie");
        CurrentRelevance reward = new CurrentRelevance(mysqlEntityMetricsProvider);
       // long a = reward.getSomeCount();

        //System.out.println(a);

        assertTrue(true == true);

    }

}