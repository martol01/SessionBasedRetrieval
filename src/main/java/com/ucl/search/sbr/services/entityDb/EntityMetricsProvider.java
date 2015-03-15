package com.ucl.search.sbr.services.entityDb;

import java.util.List;

/**
 * Wrapper for returning entity metrics for Clueweb documents.
 *
 * Created by gabriel on 14/03/15.
 */
public interface EntityMetricsProvider {

    /**
     * @param id the entity's Freebase id
     * @return the number of times the entity occurs in the corpus
     */
    public long getEntityCorpusCount(String id);

    /**
     * Get the number of (non-unique) entities in the document.
     * Multiple occurrences of same entity are counted as well.
     */
    public long getCorpusLength();

    /**
     * Get the Freebase text representation of the entity with specified id
     * @param entityId Freebase identifier of entity
     * @return the text representation of the entity
     */
    public String getEntityText(String entityId);

    /**
     * Number of times a Clueweb document contains a Freebase entity
     * @param entityId Freebase id of entity
     * @param documentId Document id in the Clueweb collection
     * @return how many times the document contains the entity
     */
    public long getEntityDocumentCount(String entityId, String documentId);

    /**
     * Get the number of entities in the Clueweb document. Counts entity
     * occurrences, meaning that duplicates are also counted.
     * @param documentId Clueweb id of document
     * @return number of entity occurrences in the document
     */
    public long getDocumentLength(String documentId);

    /**
     * Checks if the entity occurs in the documents specified in the docIds list
     * @param entityId Freebase id of the entity
     * @param docIds Clueweb ids for the documents that need to be checked
     * @return true if the entity occurs in any of the docs; false otherwise
     */
    public boolean checkEntityOccurence(String entityId, List<String> docIds);
}
