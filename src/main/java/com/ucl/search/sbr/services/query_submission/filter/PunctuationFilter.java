package com.ucl.search.sbr.services.query_submission.filter;

import com.ucl.search.sbr.domain.QueryEntity;

import java.util.List;

/**
 * Filter that removes punctuation from entities' query text
 * <p/>
 * Created by Gabriel on 2/11/2015.
 */
public class PunctuationFilter implements Filter {


    public PunctuationFilter() {
    }


    @Override
    public List<QueryEntity> filterEntities(List<QueryEntity> entities) {

        for (QueryEntity entity : entities) {
            String filteredQueryText = entity.getQueryText().replaceAll("[^a-zA-Z0-9\\s]", "");
            entity.setQueryText(filteredQueryText);
        }

        return entities;
    }

}
