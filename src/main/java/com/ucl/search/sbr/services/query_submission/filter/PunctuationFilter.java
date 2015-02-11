package com.ucl.search.sbr.services.query_submission.filter;

import com.ucl.search.sbr.domain.QueryEntity;

import java.util.List;

/**
 * Filter that removes punctuation from entities' query text
 *
 * Created by Gabriel on 2/11/2015.
 */
public class PunctuationFilter implements Filter {

    @Override
    public List<QueryEntity> filterEntities(List<QueryEntity> entities) {
        return null;
    }

}
