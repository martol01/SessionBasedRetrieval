package com.ucl.search.sbr.services.entityDb;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private Long corpusLength = null;

    public MysqlEntityMetricsProvider(String host, String username, String password) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setCreateDatabaseIfNotExist(false);

        connection = dataSource.getConnection(username, password);
        prepareStatements();
    }

    private void prepareStatements() throws SQLException {
        entityCorpusCountStatement = connection.prepareStatement("SELECT corpus_count FROM Entities WHERE id=?;");
        corpusLengthStatement = connection.prepareStatement("SELECT SUM(corpus_count) AS corpus_length FROM Entities;");
        entityTextStatement = connection.prepareStatement("SELECT text FROM Entities WHERE id=?;");
        entityDocumentCountStatement = connection.prepareStatement(
                "SELECT `count` FROM DocumentsEntities WHERE document_id=? AND entity_id=?;");
        documentLengthStatement = connection.prepareStatement(
                "SELECT total_count FROM DocumentsTotalEntities WHERE document_id=?;");
    }

    @Override
    public long getEntityCorpusCount(String id) {
        ResultSet rs = null;
        long count;
        try {
            entityCorpusCountStatement.setString(1, id);
            rs = entityCorpusCountStatement.executeQuery();
            rs.first();
            count = rs.getLong("corpus_count");
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
            } catch (NullPointerException|SQLException ignored) {
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
            rs.first();
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
        ResultSet rs = null;
        try {
            entityDocumentCountStatement.setString(1, documentId);
            entityDocumentCountStatement.setString(2, entityId);
            rs = entityDocumentCountStatement.executeQuery();
            rs.first();
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
            rs.first();
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
    public boolean checkEntityOccurence(String entityId, List<String> docIds) {
        return false;
    }

    public void close() throws IOException, SQLException {
        List<PreparedStatement> allStatements = Arrays.asList(entityCorpusCountStatement, corpusLengthStatement,
                entityTextStatement, entityDocumentCountStatement, documentLengthStatement);
        for (PreparedStatement statement : allStatements) {
            if (!statement.isClosed())
                statement.close();
        }

        if (!connection.isClosed())
            connection.close();
    }
}
