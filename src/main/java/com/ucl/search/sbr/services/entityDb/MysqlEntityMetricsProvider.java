package com.ucl.search.sbr.services.entityDb;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation using Mysql database to access the data
 * <p/>
 * Created by gabriel on 15/03/15.
 */
public class MysqlEntityMetricsProvider implements EntityMetricsProvider {
    private static final String DATABASE_NAME = "clueweb_entities";
    private Connection connection;
    private PreparedStatement entityCorpusCountStatement;
    private PreparedStatement corpusLengthStatement;
    private PreparedStatement entityTextStatement;
    private PreparedStatement entityDocumentCountStatement;
    private PreparedStatement documentLengthStatement;
    private Statement entityOccurrenceStatement;
    private PreparedStatement entityDocumentFrequencyStatement;

    // entity occurrence count in corpus
    private Long corpusLength = null;
    // number of documents in corpus
    private Long documentCount = null;
    private CacheMap<String, Double> entityIdfs;

    public MysqlEntityMetricsProvider(String host, String username, String password) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setCreateDatabaseIfNotExist(false);

        connection = dataSource.getConnection(username, password);

        prepareStatements();
        entityIdfs = new CacheMap<>(50);
    }

    private void prepareStatements() throws SQLException {
        entityCorpusCountStatement = connection.prepareStatement("SELECT corpus_count FROM Entities WHERE id=?;");
        corpusLengthStatement = connection.prepareStatement("SELECT SUM(corpus_count) AS corpus_length FROM Entities;");
        entityTextStatement = connection.prepareStatement("SELECT text FROM Entities WHERE id=?;");
        entityDocumentCountStatement = connection.prepareStatement(
                "SELECT `count` FROM DocumentsEntities WHERE document_id=? AND entity_id=?;");
        documentLengthStatement = connection.prepareStatement(
                "SELECT total_count FROM DocumentsTotalEntities WHERE document_id=?;");
        entityOccurrenceStatement = connection.createStatement();
        entityDocumentFrequencyStatement = connection.prepareStatement(
                "SELECT COUNT(document_id) AS document_frequency FROM DocumentsEntities WHERE entity_id=?;");
    }

    @Override
    public long getEntityCorpusCount(String id) {
        ResultSet rs = null;
        long count;
        try {
            entityCorpusCountStatement.setString(1, id);
            rs = entityCorpusCountStatement.executeQuery();
            if (!rs.first())
                return 0;
            count = rs.getLong("corpus_count");
        } catch (SQLException e) {
            return 0;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return count;
    }

    @Override
    public long getCorpusLength() {
        if (corpusLength != null)
            return corpusLength;
        ResultSet rs = null;
        try {
            rs = corpusLengthStatement.executeQuery();
            rs.first();
            corpusLength = rs.getLong("corpus_length");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (NullPointerException | SQLException ignored) {
            }
        }
        return corpusLength;
    }

    @Override
    public String getEntityText(String entityId) {
        String entityText = null;
        ResultSet rs = null;
        try {
            entityTextStatement.setString(1, entityId);
            rs = entityTextStatement.executeQuery();
            if (!rs.first())
                return null;
            entityText = rs.getString("text");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return entityText;
    }

    @Override
    public long getEntityDocumentCount(String entityId, String documentId) {
        long entityDocumentCount = 0;
        System.out.println("trying to get getEntityDocCount with entity id : " + entityId +" and doc id: " + documentId);
        ResultSet rs = null;
        try {
            entityDocumentCountStatement.setString(1, documentId);
            entityDocumentCountStatement.setString(2, entityId);
            rs = entityDocumentCountStatement.executeQuery();
            if (!rs.first())
                return 0;
            entityDocumentCount = rs.getLong("count");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return entityDocumentCount;
    }

    @Override
    public long getDocumentLength(String documentId) {
        long documentLength = 0;
        ResultSet rs = null;
        try {
            documentLengthStatement.setString(1, documentId);
            rs = documentLengthStatement.executeQuery();
            if (!rs.first())
                return 0;
            documentLength = rs.getLong("total_count");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return documentLength;
    }

    @Override
    public boolean checkEntityOccurrence(String entityId, List<String> docIds) {
        int docSize = docIds.size();
        if (docSize == 0)
            return false;

        StringBuilder inClause = new StringBuilder("(");
        for (int i = 0; i < docSize - 1; i++) {
            inClause.append("'").append(docIds.get(i)).append("',");
        }
        inClause.append("'").append(docIds.get(docSize-1)).append("'");
        inClause.append(")");
        String query = String.format("SELECT COUNT(*) AS occurrence_count FROM DocumentsEntities " +
                "WHERE document_id IN %s AND entity_id='%s';", inClause, entityId);

        long count = 0;
        ResultSet rs = null;
        try {
            rs = entityOccurrenceStatement.executeQuery(query);
            rs.first();
            count = rs.getLong("occurrence_count");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
        }

        return count > 0;
    }

    @Override
    public double getEntityIdf(String entityId) {
        if (entityIdfs.containsKey(entityId))
            return entityIdfs.get(entityId);

        double idf = Math.log((double) getDocumentCount() / getEntityDocumentFrequency(entityId));
        entityIdfs.put(entityId, idf);
        return idf;
    }

    private long getEntityDocumentFrequency(String entityId) {
        long df = 0;
        ResultSet rs = null;
        try {
            entityDocumentFrequencyStatement.setString(1, entityId);
            rs = entityDocumentFrequencyStatement.executeQuery();
            rs.first();
            df = rs.getLong("document_frequency");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
        }

        return df;
    }

    private long getDocumentCount() {
        if (documentCount != null)
            return documentCount;

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS document_count FROM DocumentsTotalEntities;")
        ) {
            rs.first();
            documentCount = rs.getLong("document_count");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return documentCount;
    }

    public void close() throws IOException, SQLException {
        List<Statement> allStatements = Arrays.asList(entityCorpusCountStatement,
                corpusLengthStatement, entityTextStatement, entityDocumentCountStatement,
                documentLengthStatement, entityOccurrenceStatement, entityDocumentFrequencyStatement);
        for (Statement statement : allStatements) {
            if (!statement.isClosed())
                statement.close();
        }
        if (!connection.isClosed())
            connection.close();
    }
}