/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2018, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/
package schemacrawler.crawl;


import static java.util.Objects.requireNonNull;

import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;

/**
 * Represents a single column mapping from a primary key column to a
 * foreign key column.
 *
 * @author Sualeh Fatehi
 */
public abstract class BaseColumnReference
  implements ColumnReference, Comparable<ColumnReference>
{

  private static final long serialVersionUID = -4411771492159843382L;

  private final Column foreignKeyColumn;
  private final Column primaryKeyColumn;

  protected BaseColumnReference(final Column primaryKeyColumn,
                                final Column foreignKeyColumn)
  {
    this.primaryKeyColumn = requireNonNull(primaryKeyColumn,
                                           "No primary key column provided");
    this.foreignKeyColumn = requireNonNull(foreignKeyColumn,
                                           "No foreign key column provided");
  }

  @Override
  public int compareTo(final ColumnReference columnRef)
  {
    if (columnRef == null)
    {
      return -1;
    }

    int compare = 0;
    if (compare == 0)
    {
      compare = primaryKeyColumn.getFullName()
        .compareTo(columnRef.getPrimaryKeyColumn().getFullName());
    }
    if (compare == 0)
    {
      compare = foreignKeyColumn.getFullName()
        .compareTo(columnRef.getForeignKeyColumn().getFullName());
    }
    return compare;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    final ColumnReference other = (ColumnReference) obj;
    if (foreignKeyColumn == null)
    {
      if (other.getForeignKeyColumn() != null)
      {
        return false;
      }
    }
    else if (!foreignKeyColumn.equals(other.getForeignKeyColumn()))
    {
      return false;
    }
    if (primaryKeyColumn == null)
    {
      if (other.getPrimaryKeyColumn() != null)
      {
        return false;
      }
    }
    else if (!primaryKeyColumn.equals(other.getPrimaryKeyColumn()))
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Column getForeignKeyColumn()
  {
    return foreignKeyColumn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Column getPrimaryKeyColumn()
  {
    return primaryKeyColumn;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result
             + (foreignKeyColumn == null? 0: foreignKeyColumn.hashCode());
    result = prime * result
             + (primaryKeyColumn == null? 0: primaryKeyColumn.hashCode());
    return result;
  }

  @Override
  public String toString()
  {
    return primaryKeyColumn + " <-- " + foreignKeyColumn;
  }

}
