package com.ucl.search.sbr.services.query_submission.filter;

import com.ucl.search.sbr.domain.QueryEntity;

import java.util.List;

/**
 *
 *
 * Created by Gabriel on 2/11/2015.
 */
public interface Filter {

    public List<QueryEntity> filterEntities(List<QueryEntity> entities);

}
