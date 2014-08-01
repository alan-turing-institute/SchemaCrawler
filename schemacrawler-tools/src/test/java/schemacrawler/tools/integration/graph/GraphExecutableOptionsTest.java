/*
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2014, Sualeh Fatehi.
 * This library is free software; you can redistribute it and/or modify it under
 * the terms
 * of the GNU Lesser General Public License as published by the Free Software
 * Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package schemacrawler.tools.integration.graph;


import static schemacrawler.test.utility.TestUtility.currentMethodName;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.test.utility.BaseExecutableTest;
import schemacrawler.tools.text.schema.SchemaTextDetailType;
import sf.util.Utility;

public class GraphExecutableOptionsTest
  extends BaseExecutableTest
{

  private static final String GRAPH_OPTIONS_OUTPUT = "graph_options_output/";
  private static final boolean GENERATE_FOR_SITE = false;

  @Test
  public void executableForGraph_00()
    throws Exception
  {

    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions.setSchemaInfoLevel(SchemaInfoLevel.maximum());
    final GraphOptions graphOptions = new GraphOptions();

    executableGraph(schemaCrawlerOptions, graphOptions, currentMethodName());
  }

  @Test
  public void executableForGraph_01()
    throws Exception
  {
    final GraphOptions graphOptions = new GraphOptions();
    graphOptions.setAlphabeticalSortForTableColumns(true);
    graphOptions.setShowOrdinalNumbers(true);

    executableGraph(new SchemaCrawlerOptions(),
                    graphOptions,
                    currentMethodName());
  }

  @Test
  public void executableForGraph_02()
    throws Exception
  {
    final GraphOptions graphOptions = new GraphOptions();
    graphOptions.setHideForeignKeyNames(true);

    executableGraph(new SchemaCrawlerOptions(),
                    graphOptions,
                    currentMethodName());
  }

  @Test
  public void executableForGraph_03()
    throws Exception
  {
    final GraphOptions graphOptions = new GraphOptions();
    graphOptions.setNoInfo(true);

    executableGraph(new SchemaCrawlerOptions(),
                    graphOptions,
                    currentMethodName());
  }

  @Test
  public void executableForGraph_04()
    throws Exception
  {
    final GraphOptions graphOptions = new GraphOptions();
    graphOptions.setShowUnqualifiedNames(true);

    executableGraph(new SchemaCrawlerOptions(),
                    graphOptions,
                    currentMethodName());
  }

  @Test
  public void executableForGraph_05()
    throws Exception
  {
    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions
      .setTableInclusionRule(new RegularExpressionInclusionRule(".*BOOKS"));
    final GraphOptions graphOptions = new GraphOptions();

    executableGraph(schemaCrawlerOptions, graphOptions, currentMethodName());
  }

  @Test
  public void executableForGraph_06()
    throws Exception
  {
    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions.setSchemaInfoLevel(SchemaInfoLevel.maximum());
    final GraphOptions graphOptions = new GraphOptions();
    graphOptions.setSchemaTextDetailType(SchemaTextDetailType.list);

    executableGraph(schemaCrawlerOptions, graphOptions, currentMethodName());
  }

  @Test
  public void executableForGraph_07()
    throws Exception
  {
    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions.setSchemaInfoLevel(SchemaInfoLevel.maximum());
    final GraphOptions graphOptions = new GraphOptions();
    graphOptions.setSchemaTextDetailType(SchemaTextDetailType.schema);

    executableGraph(schemaCrawlerOptions, graphOptions, currentMethodName());
  }

  @Test
  public void executableForGraph_08()
    throws Exception
  {
    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions
      .setTableInclusionRule(new RegularExpressionInclusionRule(".*BOOKS"));

    final GraphOptions graphOptions = new GraphOptions();
    graphOptions.setShowUnqualifiedNames(true);
    graphOptions.setHideForeignKeyNames(true);

    executableGraph(schemaCrawlerOptions, graphOptions, currentMethodName());
  }

  @Test
  public void executableForGraph_09()
    throws Exception
  {
    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions
      .setTableInclusionRule(new RegularExpressionInclusionRule(".*BOOKS"));
    schemaCrawlerOptions.setGrepOnlyMatching(true);

    final GraphOptions graphOptions = new GraphOptions();
    graphOptions.setShowUnqualifiedNames(true);
    graphOptions.setHideForeignKeyNames(true);

    executableGraph(schemaCrawlerOptions, graphOptions, currentMethodName());
  }

  @Test
  public void executableForGraph_10()
    throws Exception
  {
    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions
      .setGrepColumnInclusionRule(new RegularExpressionInclusionRule(".*\\.REGIONS\\..*"));

    final GraphOptions graphOptions = new GraphOptions();

    executableGraph(schemaCrawlerOptions, graphOptions, currentMethodName());
  }

  @Test
  public void executableForGraph_11()
    throws Exception
  {
    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions
      .setGrepColumnInclusionRule(new RegularExpressionInclusionRule(".*\\.REGIONS\\..*"));
    schemaCrawlerOptions.setGrepOnlyMatching(true);

    final GraphOptions graphOptions = new GraphOptions();

    executableGraph(schemaCrawlerOptions, graphOptions, currentMethodName());
  }

  private void executableGraph(final SchemaCrawlerOptions schemaCrawlerOptions,
                               final GraphOptions graphOptions,
                               final String testMethodName)
    throws Exception
  {
    final GraphExecutable executable = new GraphExecutable();
    executable.setSchemaCrawlerOptions(schemaCrawlerOptions);
    executable.setAdditionalConfiguration(graphOptions.toConfig());

    // Check DOT file
    final String referenceFileName = testMethodName;
    executeExecutableAndCheckForOutputFile(executable,
                                           "echo",
                                           GRAPH_OPTIONS_OUTPUT
                                               + referenceFileName + ".dot");

    // Check diagram
    final File testDiagramFile = executeExecutable(executable, "png");
    saveDiagramForSite(testMethodName, testDiagramFile);
    checkDiagramFile(testDiagramFile);
  }

  private void saveDiagramForSite(final String testMethodName,
                                  final File testDiagramFile)
    throws IOException
  {
    if (GENERATE_FOR_SITE)
    {
      final String buildDirectory = System.getProperty("buildDirectory");
      final File siteImages = new File(buildDirectory,
                                       "../../schemacrawler-site/src/site/resources/images")
        .getCanonicalFile();
      final File siteImage = new File(siteImages, testMethodName + ".png");
      Utility.copyFile(testDiagramFile, siteImage);
    }
  }
}
