package com.ucl.search.sbr.services.query_submission.filter;

import com.ucl.search.sbr.domain.QueryEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestPunctuationFilter {

    private PunctuationFilter filter;

    @Before
    public void createObjInstance() {
        filter = new PunctuationFilter();
    }

    /* test for a list that contains entities with punctuation */
    @Test
    public void testFullPunctuationList() {

        List<QueryEntity> testEntities = Arrays.asList(
                new QueryEntity("sh(.op"),
                new QueryEntity("..fashion"),
                new QueryEntity("U.S.")
        );

        List<QueryEntity> expectedEntities = Arrays.asList(
                new QueryEntity("shop"),
                new QueryEntity("fashion"),
                new QueryEntity("US")
            );

        /* run the method */
        List<QueryEntity> resultEntities = filter.filterEntities(testEntities);

        assertTrue(expectedEntities.size() == resultEntities.size());

        for (int i = 0; i < resultEntities.size(); i++) {
            assertTrue(resultEntities.get(i).getQueryText().equals(expectedEntities.get(i).getQueryText()));
        }

    }

    /* test with empty list */
    @Test
    public void testEmptyList() {

        List<QueryEntity> testEntities = new ArrayList<>();

        /* run the method */
        List<QueryEntity> resultEntities = filter.filterEntities(testEntities);

        assertTrue(resultEntities.isEmpty());
    }

    /* test with a list that doesn't contain punctuation */
    @Test
    public void testNoPunctuationList(){
        List<QueryEntity> testEntities = Arrays.asList(
                new QueryEntity("magazine"),
                new QueryEntity("London restaurant"),
                new QueryEntity("the closest museum to Big Ben")
        );

        /* run the method */
        List<QueryEntity> resultEntities = filter.filterEntities(testEntities);

        assertTrue(testEntities.size() == resultEntities.size());

        for (int i = 0; i < resultEntities.size(); i++) {
            assertTrue(resultEntities.get(i).getQueryText().equals(testEntities.get(i).getQueryText()));
        }
    }

}