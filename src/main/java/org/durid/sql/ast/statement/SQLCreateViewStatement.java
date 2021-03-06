/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
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
package org.durid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import org.durid.sql.ast.SQLExpr;
import org.durid.sql.ast.SQLName;
import org.durid.sql.ast.SQLStatementImpl;
import org.durid.sql.visitor.SQLASTVisitor;

public class SQLCreateViewStatement extends SQLStatementImpl implements SQLDDLStatement {

    private static final long     serialVersionUID = 1L;
    protected SQLName             name;
    protected SQLSelect           subQuery;

    protected final List<SQLExpr> columns          = new ArrayList<SQLExpr>();

    private Level                 with;

    public SQLCreateViewStatement(){

    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public Level getWith() {
        return with;
    }

    public void setWith(Level with) {
        this.with = with;
    }

    public SQLSelect getSubQuery() {
        return subQuery;
    }

    public void setSubQuery(SQLSelect subQuery) {
        this.subQuery = subQuery;
    }

    public List<SQLExpr> getColumns() {
        return columns;
    }

    public void output(StringBuffer buf) {
        buf.append("CREATE VIEW ");
        this.name.output(buf);

        if (this.columns.size() > 0) {
            buf.append(" (");
            for (int i = 0, size = this.columns.size(); i < size; ++i) {
                if (i != 0) {
                    buf.append(", ");
                }
                this.columns.get(i).output(buf);
            }
            buf.append(")");
        }

        buf.append(" AS ");
        this.subQuery.output(buf);

        if (this.with != null) {
            buf.append(" WITH ");
            buf.append(this.with.name());
        }
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.name);
            acceptChild(visitor, this.columns);
            acceptChild(visitor, this.subQuery);
        }
        visitor.endVisit(this);
    }

    public static enum Level {
        CASCADED, LOCAL
    }
}
