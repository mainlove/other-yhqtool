package org.mainolove.project.java.tool.yhqtool.yhqtool.db;

import java.sql.ResultSet;

public interface ResultHandler<T> {

	T handle(ResultSet ret) throws Exception;

}
