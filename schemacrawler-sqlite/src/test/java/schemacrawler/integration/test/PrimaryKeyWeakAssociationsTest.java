/*
 * SchemaCrawler
 * Copyright (c) 2000-2015, Sualeh Fatehi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package schemacrawler.integration.test;


import static org.junit.Assert.assertEquals;
import static schemacrawler.test.utility.TestUtility.copyResourceToTempFile;
import static schemacrawler.test.utility.TestUtility.currentMethodFullName;
import static schemacrawler.utility.MetaDataUtility.findForeignKeyCardinality;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.ConnectionOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.test.utility.BaseDatabaseTest;
import schemacrawler.test.utility.TestWriter;
import schemacrawler.tools.analysis.associations.CatalogWithAssociations;
import schemacrawler.tools.sqlite.SQLiteDatabaseConnector;
import schemacrawler.utility.NamedObjectSort;
import schemacrawler.utility.SchemaCrawlerUtility;

public class PrimaryKeyWeakAssociationsTest
  extends BaseDatabaseTest
{

  @Test
  public void weakAssociations()
    throws Exception
  {
    final Path sqliteDbFile = copyResourceToTempFile("/pk_test.db");
    final Config config = new Config();
    config.put("server", "sqlite");
    config.put("database", sqliteDbFile.toString());

    try (final TestWriter out = new TestWriter("text");)
    {
      final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
      schemaCrawlerOptions.setSchemaInfoLevel(SchemaInfoLevel.maximum());

      ConnectionOptions connectionOptions = new SQLiteDatabaseConnector()
        .getDatabaseSystemConnector().newDatabaseConnectionOptions(config);

      final Catalog baseCatalog = SchemaCrawlerUtility
        .getCatalog(connectionOptions.getConnection(), schemaCrawlerOptions);
      final CatalogWithAssociations catalog = new CatalogWithAssociations(baseCatalog);
      final Schema[] schemas = catalog.getSchemas().toArray(new Schema[0]);
      assertEquals("Schema count does not match", 1, schemas.length);
      for (final Schema schema: schemas)
      {
        out.println("schema: " + schema.getFullName());
        final Table[] tables = catalog.getTables(schema).toArray(new Table[0]);
        Arrays.sort(tables, NamedObjectSort.alphabetical);
        for (final Table table: tables)
        {
          out.println("  table: " + table.getFullName());
          final Collection<ColumnReference> weakAssociations = CatalogWithAssociations
            .getWeakAssociations(table);
          for (final ColumnReference weakAssociation: weakAssociations)
          {
            out.println(String
              .format("    weak association: %s (%s)",
                      weakAssociation,
                      findForeignKeyCardinality(weakAssociation)));
          }
        }
      }

      out.assertEquals(currentMethodFullName());
    }
  }

}
