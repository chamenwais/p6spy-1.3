/*
 *
 * ====================================================================
 *
 * The P6Spy Software License, Version 1.1
 *
 * This license is derived and fully compatible with the Apache Software
 * license, see http://www.apache.org/LICENSE.txt
 *
 * Copyright (c) 2001-2002 Andy Martin, Ph.D. and Jeff Goke
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 * any, must include the following acknowlegement:
 * "The original concept and code base for P6Spy was conceived
 * and developed by Andy Martin, Ph.D. who generously contribued
 * the first complete release to the public under this license.
 * This product was due to the pioneering work of Andy
 * that began in December of 1995 developing applications that could
 * seamlessly be deployed with minimal effort but with dramatic results.
 * This code is maintained and extended by Jeff Goke and with the ideas
 * and contributions of other P6Spy contributors.
 * (http://www.p6spy.com)"
 * Alternately, this acknowlegement may appear in the software itself,
 * if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "P6Spy", "Jeff Goke", and "Andy Martin" must not be used
 * to endorse or promote products derived from this software without
 * prior written permission. For written permission, please contact
 * license@p6spy.com.
 *
 * 5. Products derived from this software may not be called "P6Spy"
 * nor may "P6Spy" appear in their names without prior written
 * permission of Jeff Goke and Andy Martin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

/**
 * Description: JDBC Driver Extension implementing PreparedStatement.
 *
 * $Author$
 * $Revision$
 * $Date$
 *
 * $Id$
 * $Source$
 * $Log$
 * Revision 1.1  2002/04/10 04:24:26  jeffgoke
 * added support for callable statements and fixed numerous bugs that allowed the real class to be returned
 *
 *
 */

package com.p6spy.engine.spy;

import java.io.*;
import java.sql.*;

public class P6DatabaseMetaData implements java.sql.DatabaseMetaData {
    
    protected DatabaseMetaData passthru;
    protected P6Connection connection;
    
    public P6DatabaseMetaData(DatabaseMetaData metadata, P6Connection connection) {
        this.passthru = metadata;
        this.connection = connection;
    }
    
    public final boolean allProceduresAreCallable() throws java.sql.SQLException {
        return passthru.allProceduresAreCallable();
    }
    
    public final boolean allTablesAreSelectable() throws java.sql.SQLException {
        return passthru.allTablesAreSelectable();
    }
    
    public final boolean dataDefinitionCausesTransactionCommit() throws java.sql.SQLException {
        return passthru.dataDefinitionCausesTransactionCommit();
    }
    
    public final boolean dataDefinitionIgnoredInTransactions() throws java.sql.SQLException {
        return passthru.dataDefinitionIgnoredInTransactions();
    }
    
    public final boolean deletesAreDetected(int p0) throws java.sql.SQLException {
        return passthru.deletesAreDetected(p0);
    }
    
    public final boolean doesMaxRowSizeIncludeBlobs() throws java.sql.SQLException {
        return passthru.doesMaxRowSizeIncludeBlobs();
    }
    
    public final java.sql.ResultSet getBestRowIdentifier(String p0, String p1, String p2, int p3, boolean p4) throws java.sql.SQLException {
        return passthru.getBestRowIdentifier(p0,p1,p2,p3,p4);
    }
    
    public final String getCatalogSeparator() throws java.sql.SQLException {
        return passthru.getCatalogSeparator();
    }
    
    public final String getCatalogTerm() throws java.sql.SQLException {
        return passthru.getCatalogTerm();
    }
    
    public final java.sql.ResultSet getCatalogs() throws java.sql.SQLException {
        return passthru.getCatalogs();
    }
    
    public final java.sql.ResultSet getColumnPrivileges(String p0, String p1, String p2, String p3) throws java.sql.SQLException {
        return passthru.getColumnPrivileges(p0,p1,p2,p3);
    }
    
    public final java.sql.ResultSet getColumns(String p0, String p1, String p2, String p3) throws java.sql.SQLException {
        return passthru.getColumns(p0,p1,p2,p3);
    }
    
    public final java.sql.Connection getConnection() throws java.sql.SQLException {
        // return our p6connection
        return connection;
    }
    
    public final java.sql.ResultSet getCrossReference(String p0, String p1, String p2, String p3, String p4, String p5) throws java.sql.SQLException {
        return passthru.getCrossReference(p0,p1,p2,p3,p4,p5);
    }
    
    public final String getDatabaseProductName() throws java.sql.SQLException {
        return passthru.getDatabaseProductName();
    }
    
    public final String getDatabaseProductVersion() throws java.sql.SQLException {
        return passthru.getDatabaseProductVersion();
    }
    
    public final int getDefaultTransactionIsolation() throws java.sql.SQLException {
        return passthru.getDefaultTransactionIsolation();
    }
    
    public final int getDriverMajorVersion() {
        return passthru.getDriverMajorVersion();
    }
    
    public final int getDriverMinorVersion() {
        return passthru.getDriverMinorVersion();
    }
    
    public final String getDriverName() throws java.sql.SQLException {
        return passthru.getDriverName();
    }
    
    public final String getDriverVersion() throws java.sql.SQLException {
        return passthru.getDriverVersion();
    }
    
    public final java.sql.ResultSet getExportedKeys(String p0, String p1, String p2) throws java.sql.SQLException {
        return passthru.getExportedKeys(p0,p1,p2);
    }
    
    public final String getExtraNameCharacters() throws java.sql.SQLException {
        return passthru.getExtraNameCharacters();
    }
    
    public final String getIdentifierQuoteString() throws java.sql.SQLException {
        return passthru.getIdentifierQuoteString();
    }
    
    public final java.sql.ResultSet getImportedKeys(String p0, String p1, String p2) throws java.sql.SQLException {
        return passthru.getImportedKeys(p0,p1,p2);
    }
    
    public final java.sql.ResultSet getIndexInfo(String p0, String p1, String p2, boolean p3, boolean p4) throws java.sql.SQLException {
        return passthru.getIndexInfo(p0,p1,p2,p3,p4);
    }
    
    public final int getMaxBinaryLiteralLength() throws java.sql.SQLException {
        return passthru.getMaxBinaryLiteralLength();
    }
    
    public final int getMaxCatalogNameLength() throws java.sql.SQLException {
        return passthru.getMaxCatalogNameLength();
    }
    
    public final int getMaxCharLiteralLength() throws java.sql.SQLException {
        return passthru.getMaxCharLiteralLength();
    }
    
    public final int getMaxColumnNameLength() throws java.sql.SQLException {
        return passthru.getMaxColumnNameLength();
    }
    
    public final int getMaxColumnsInGroupBy() throws java.sql.SQLException {
        return passthru.getMaxColumnsInGroupBy();
    }
    
    public final int getMaxColumnsInIndex() throws java.sql.SQLException {
        return passthru.getMaxColumnsInIndex();
    }
    
    public final int getMaxColumnsInOrderBy() throws java.sql.SQLException {
        return passthru.getMaxColumnsInOrderBy();
    }
    
    public final int getMaxColumnsInSelect() throws java.sql.SQLException {
        return passthru.getMaxColumnsInSelect();
    }
    
    public final int getMaxColumnsInTable() throws java.sql.SQLException {
        return passthru.getMaxColumnsInTable();
    }
    
    public final int getMaxConnections() throws java.sql.SQLException {
        return passthru.getMaxConnections();
    }
    
    public final int getMaxCursorNameLength() throws java.sql.SQLException {
        return passthru.getMaxCursorNameLength();
    }
    
    public final int getMaxIndexLength() throws java.sql.SQLException {
        return passthru.getMaxIndexLength();
    }
    
    public final int getMaxProcedureNameLength() throws java.sql.SQLException {
        return passthru.getMaxProcedureNameLength();
    }
    
    public final int getMaxRowSize() throws java.sql.SQLException {
        return passthru.getMaxRowSize();
    }
    
    public final int getMaxSchemaNameLength() throws java.sql.SQLException {
        return passthru.getMaxSchemaNameLength();
    }
    
    public final int getMaxStatementLength() throws java.sql.SQLException {
        return passthru.getMaxStatementLength();
    }
    
    public final int getMaxStatements() throws java.sql.SQLException {
        return passthru.getMaxStatements();
    }
    
    public final int getMaxTableNameLength() throws java.sql.SQLException {
        return passthru.getMaxTableNameLength();
    }
    
    public final int getMaxTablesInSelect() throws java.sql.SQLException {
        return passthru.getMaxTablesInSelect();
    }
    
    public final int getMaxUserNameLength() throws java.sql.SQLException {
        return passthru.getMaxUserNameLength();
    }
    
    public final String getNumericFunctions() throws java.sql.SQLException {
        return passthru.getNumericFunctions();
    }
    
    public final java.sql.ResultSet getPrimaryKeys(String p0, String p1, String p2) throws java.sql.SQLException {
        return passthru.getPrimaryKeys(p0,p1,p2);
    }
    
    public final java.sql.ResultSet getProcedureColumns(String p0, String p1, String p2, String p3) throws java.sql.SQLException {
        return passthru.getProcedureColumns(p0,p1,p2,p3);
    }
    
    public final String getProcedureTerm() throws java.sql.SQLException {
        return passthru.getProcedureTerm();
    }
    
    public final java.sql.ResultSet getProcedures(String p0, String p1, String p2) throws java.sql.SQLException {
        return passthru.getProcedures(p0,p1,p2);
    }
    
    public final String getSQLKeywords() throws java.sql.SQLException {
        return passthru.getSQLKeywords();
    }
    
    public final String getSchemaTerm() throws java.sql.SQLException {
        return passthru.getSchemaTerm();
    }
    
    public final java.sql.ResultSet getSchemas() throws java.sql.SQLException {
        return passthru.getSchemas();
    }
    
    public final String getSearchStringEscape() throws java.sql.SQLException {
        return passthru.getSearchStringEscape();
    }
    
    public final String getStringFunctions() throws java.sql.SQLException {
        return passthru.getStringFunctions();
    }
    
    public final String getSystemFunctions() throws java.sql.SQLException {
        return passthru.getSystemFunctions();
    }
    
    public final java.sql.ResultSet getTablePrivileges(String p0, String p1, String p2) throws java.sql.SQLException {
        return passthru.getTablePrivileges(p0,p1,p2);
    }
    
    public final java.sql.ResultSet getTableTypes() throws java.sql.SQLException {
        return passthru.getTableTypes();
    }
    
    public final java.sql.ResultSet getTables(String p0, String p1, String p2, String[] p3) throws java.sql.SQLException {
        return passthru.getTables(p0,p1,p2,p3);
    }
    
    public final String getTimeDateFunctions() throws java.sql.SQLException {
        return passthru.getTimeDateFunctions();
    }
    
    public final java.sql.ResultSet getTypeInfo() throws java.sql.SQLException {
        return passthru.getTypeInfo();
    }
    
    public final java.sql.ResultSet getUDTs(String p0, String p1, String p2, int[] p3) throws java.sql.SQLException {
        return passthru.getUDTs(p0,p1,p2,p3);
    }
    
    public final String getURL() throws java.sql.SQLException {
        return passthru.getURL();
    }
    
    public final String getUserName() throws java.sql.SQLException {
        return passthru.getUserName();
    }
    
    public final java.sql.ResultSet getVersionColumns(String p0, String p1, String p2) throws java.sql.SQLException {
        return passthru.getVersionColumns(p0,p1,p2);
    }
    
    public final boolean insertsAreDetected(int p0) throws java.sql.SQLException {
        return passthru.insertsAreDetected(p0);
    }
    
    public final boolean isCatalogAtStart() throws java.sql.SQLException {
        return passthru.isCatalogAtStart();
    }
    
    public final boolean isReadOnly() throws java.sql.SQLException {
        return passthru.isReadOnly();
    }
    
    public final boolean nullPlusNonNullIsNull() throws java.sql.SQLException {
        return passthru.nullPlusNonNullIsNull();
    }
    
    public final boolean nullsAreSortedAtEnd() throws java.sql.SQLException {
        return passthru.nullsAreSortedAtEnd();
    }
    
    public final boolean nullsAreSortedAtStart() throws java.sql.SQLException {
        return passthru.nullsAreSortedAtStart();
    }
    
    public final boolean nullsAreSortedHigh() throws java.sql.SQLException {
        return passthru.nullsAreSortedHigh();
    }
    
    public final boolean nullsAreSortedLow() throws java.sql.SQLException {
        return passthru.nullsAreSortedLow();
    }
    
    public final boolean othersDeletesAreVisible(int p0) throws java.sql.SQLException {
        return passthru.othersDeletesAreVisible(p0);
    }
    
    public final boolean othersInsertsAreVisible(int p0) throws java.sql.SQLException {
        return passthru.othersInsertsAreVisible(p0);
    }
    
    public final boolean othersUpdatesAreVisible(int p0) throws java.sql.SQLException {
        return passthru.othersUpdatesAreVisible(p0);
    }
    
    public final boolean ownDeletesAreVisible(int p0) throws java.sql.SQLException {
        return passthru.ownDeletesAreVisible(p0);
    }
    
    public final boolean ownInsertsAreVisible(int p0) throws java.sql.SQLException {
        return passthru.ownInsertsAreVisible(p0);
    }
    
    public final boolean ownUpdatesAreVisible(int p0) throws java.sql.SQLException {
        return passthru.ownUpdatesAreVisible(p0);
    }
    
    public final boolean storesLowerCaseIdentifiers() throws java.sql.SQLException {
        return passthru.storesLowerCaseIdentifiers();
    }
    
    public final boolean storesLowerCaseQuotedIdentifiers() throws java.sql.SQLException {
        return passthru.storesLowerCaseQuotedIdentifiers();
    }
    
    public final boolean storesMixedCaseIdentifiers() throws java.sql.SQLException {
        return passthru.storesMixedCaseIdentifiers();
    }
    
    public final boolean storesMixedCaseQuotedIdentifiers() throws java.sql.SQLException {
        return passthru.storesMixedCaseQuotedIdentifiers();
    }
    
    public final boolean storesUpperCaseIdentifiers() throws java.sql.SQLException {
        return passthru.storesUpperCaseIdentifiers();
    }
    
    public final boolean storesUpperCaseQuotedIdentifiers() throws java.sql.SQLException {
        return passthru.storesUpperCaseQuotedIdentifiers();
    }
    
    public final boolean supportsANSI92EntryLevelSQL() throws java.sql.SQLException {
        return passthru.supportsANSI92EntryLevelSQL();
    }
    
    public final boolean supportsANSI92FullSQL() throws java.sql.SQLException {
        return passthru.supportsANSI92FullSQL();
    }
    
    public final boolean supportsANSI92IntermediateSQL() throws java.sql.SQLException {
        return passthru.supportsANSI92IntermediateSQL();
    }
    
    public final boolean supportsAlterTableWithAddColumn() throws java.sql.SQLException {
        return passthru.supportsAlterTableWithAddColumn();
    }
    
    public final boolean supportsAlterTableWithDropColumn() throws java.sql.SQLException {
        return passthru.supportsAlterTableWithDropColumn();
    }
    
    public final boolean supportsBatchUpdates() throws java.sql.SQLException {
        return passthru.supportsBatchUpdates();
    }
    
    public final boolean supportsCatalogsInDataManipulation() throws java.sql.SQLException {
        return passthru.supportsCatalogsInDataManipulation();
    }
    
    public final boolean supportsCatalogsInIndexDefinitions() throws java.sql.SQLException {
        return passthru.supportsCatalogsInIndexDefinitions();
    }
    
    public final boolean supportsCatalogsInPrivilegeDefinitions() throws java.sql.SQLException {
        return passthru.supportsCatalogsInPrivilegeDefinitions();
    }
    
    public final boolean supportsCatalogsInProcedureCalls() throws java.sql.SQLException {
        return passthru.supportsCatalogsInProcedureCalls();
    }
    
    public final boolean supportsCatalogsInTableDefinitions() throws java.sql.SQLException {
        return passthru.supportsCatalogsInTableDefinitions();
    }
    
    public final boolean supportsColumnAliasing() throws java.sql.SQLException {
        return passthru.supportsColumnAliasing();
    }
    
    public final boolean supportsConvert() throws java.sql.SQLException {
        return passthru.supportsConvert();
    }
    
    public final boolean supportsConvert(int p0, int p1) throws java.sql.SQLException {
        return passthru.supportsConvert (p0,p1);
    }
    
    public final boolean supportsCoreSQLGrammar() throws java.sql.SQLException {
        return passthru.supportsCoreSQLGrammar();
    }
    
    public final boolean supportsCorrelatedSubqueries() throws java.sql.SQLException {
        return passthru.supportsCorrelatedSubqueries();
    }
    
    public final boolean supportsDataDefinitionAndDataManipulationTransactions() throws java.sql.SQLException {
        return passthru.supportsDataDefinitionAndDataManipulationTransactions();
    }
    
    public final boolean supportsDataManipulationTransactionsOnly() throws java.sql.SQLException {
        return passthru.supportsDataManipulationTransactionsOnly();
    }
    
    public final boolean supportsDifferentTableCorrelationNames() throws java.sql.SQLException {
        return passthru.supportsDifferentTableCorrelationNames();
    }
    
    public final boolean supportsExpressionsInOrderBy() throws java.sql.SQLException {
        return passthru.supportsExpressionsInOrderBy();
    }
    
    public final boolean supportsExtendedSQLGrammar() throws java.sql.SQLException {
        return passthru.supportsExtendedSQLGrammar();
    }
    
    public final boolean supportsFullOuterJoins() throws java.sql.SQLException {
        return passthru.supportsFullOuterJoins();
    }
    
    public final boolean supportsGroupBy() throws java.sql.SQLException {
        return passthru.supportsGroupBy();
    }
    
    public final boolean supportsGroupByBeyondSelect() throws java.sql.SQLException {
        return passthru.supportsGroupByBeyondSelect();
    }
    
    public final boolean supportsGroupByUnrelated() throws java.sql.SQLException {
        return passthru.supportsGroupByUnrelated();
    }
    
    public final boolean supportsIntegrityEnhancementFacility() throws java.sql.SQLException {
        return passthru.supportsIntegrityEnhancementFacility();
    }
    
    public final boolean supportsLikeEscapeClause() throws java.sql.SQLException {
        return passthru.supportsLikeEscapeClause();
    }
    
    public final boolean supportsLimitedOuterJoins() throws java.sql.SQLException {
        return passthru.supportsLimitedOuterJoins();
    }
    
    public final boolean supportsMinimumSQLGrammar() throws java.sql.SQLException {
        return passthru.supportsMinimumSQLGrammar();
    }
    
    public final boolean supportsMixedCaseIdentifiers() throws java.sql.SQLException {
        return passthru.supportsMixedCaseIdentifiers();
    }
    
    public final boolean supportsMixedCaseQuotedIdentifiers() throws java.sql.SQLException {
        return passthru.supportsMixedCaseQuotedIdentifiers();
    }
    
    public final boolean supportsMultipleResultSets() throws java.sql.SQLException {
        return passthru.supportsMultipleResultSets();
    }
    
    public final boolean supportsMultipleTransactions() throws java.sql.SQLException {
        return passthru.supportsMultipleTransactions();
    }
    
    public final boolean supportsNonNullableColumns() throws java.sql.SQLException {
        return passthru.supportsNonNullableColumns();
    }
    
    public final boolean supportsOpenCursorsAcrossCommit() throws java.sql.SQLException {
        return passthru.supportsOpenCursorsAcrossCommit();
    }
    
    public final boolean supportsOpenCursorsAcrossRollback() throws java.sql.SQLException {
        return passthru.supportsOpenCursorsAcrossRollback();
    }
    
    public final boolean supportsOpenStatementsAcrossCommit() throws java.sql.SQLException {
        return passthru.supportsOpenStatementsAcrossCommit();
    }
    
    public final boolean supportsOpenStatementsAcrossRollback() throws java.sql.SQLException {
        return passthru.supportsOpenStatementsAcrossRollback();
    }
    
    public final boolean supportsOrderByUnrelated() throws java.sql.SQLException {
        return passthru.supportsOrderByUnrelated();
    }
    
    public final boolean supportsOuterJoins() throws java.sql.SQLException {
        return passthru.supportsOuterJoins();
    }
    
    public final boolean supportsPositionedDelete() throws java.sql.SQLException {
        return passthru.supportsPositionedDelete();
    }
    
    public final boolean supportsPositionedUpdate() throws java.sql.SQLException {
        return passthru.supportsPositionedUpdate();
    }
    
    public final boolean supportsResultSetConcurrency(int p0, int p1) throws java.sql.SQLException {
        return passthru.supportsResultSetConcurrency(p0,p1);
    }
    
    public final boolean supportsResultSetType(int p0) throws java.sql.SQLException {
        return passthru.supportsResultSetType(p0);
    }
    
    public final boolean supportsSchemasInDataManipulation() throws java.sql.SQLException {
        return passthru.supportsSchemasInDataManipulation();
    }
    
    public final boolean supportsSchemasInIndexDefinitions() throws java.sql.SQLException {
        return passthru.supportsSchemasInIndexDefinitions();
    }
    
    public final boolean supportsSchemasInPrivilegeDefinitions() throws java.sql.SQLException {
        return passthru.supportsSchemasInPrivilegeDefinitions();
    }
    
    public final boolean supportsSchemasInProcedureCalls() throws java.sql.SQLException {
        return passthru.supportsSchemasInProcedureCalls();
    }
    
    public final boolean supportsSchemasInTableDefinitions() throws java.sql.SQLException {
        return passthru.supportsSchemasInTableDefinitions();
    }
    
    public final boolean supportsSelectForUpdate() throws java.sql.SQLException {
        return passthru.supportsSelectForUpdate();
    }
    
    public final boolean supportsStoredProcedures() throws java.sql.SQLException {
        return passthru.supportsStoredProcedures();
    }
    
    public final boolean supportsSubqueriesInComparisons() throws java.sql.SQLException {
        return passthru.supportsSubqueriesInComparisons();
    }
    
    public final boolean supportsSubqueriesInExists() throws java.sql.SQLException {
        return passthru.supportsSubqueriesInExists();
    }
    
    public final boolean supportsSubqueriesInIns() throws java.sql.SQLException {
        return passthru.supportsSubqueriesInIns();
    }
    
    public final boolean supportsSubqueriesInQuantifieds() throws java.sql.SQLException {
        return passthru.supportsSubqueriesInQuantifieds();
    }
    
    public final boolean supportsTableCorrelationNames() throws java.sql.SQLException {
        return passthru.supportsTableCorrelationNames();
    }
    
    public final boolean supportsTransactionIsolationLevel(int p0) throws java.sql.SQLException {
        return passthru.supportsTransactionIsolationLevel(p0);
    }
    
    public final boolean supportsTransactions() throws java.sql.SQLException {
        return passthru.supportsTransactions();
    }
    
    public final boolean supportsUnion() throws java.sql.SQLException {
        return passthru.supportsUnion();
    }
    
    public final boolean supportsUnionAll() throws java.sql.SQLException {
        return passthru.supportsUnionAll();
    }
    
    public final boolean updatesAreDetected(int p0) throws java.sql.SQLException {
        return passthru.updatesAreDetected(p0);
    }
    
    public final boolean usesLocalFilePerTable() throws java.sql.SQLException {
        return passthru.usesLocalFilePerTable();
    }
    
    public final boolean usesLocalFiles() throws java.sql.SQLException {
        return passthru.usesLocalFiles();
    }
    
}
