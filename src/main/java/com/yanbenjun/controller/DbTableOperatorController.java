package com.yanbenjun.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.yanbenjun.db.DbTable;
import com.yanbenjun.db.dao.DaoBeanFactory;
import com.yanbenjun.db.dao.IDbTableDao;

@RestController
public class DbTableOperatorController {

	@RequestMapping("/createTable")
	public String createTable(@RequestBody DbTable dbTable)
	{
		Gson gson = new Gson();
		//DbTable dbTable = gson.fromJson(tableJsonInfo, DbTable.class);
		String sql = dbTable.getCreateTableSql();
		IDbTableDao dao = (IDbTableDao) DaoBeanFactory.getBean(IDbTableDao.class);
        dao.executeSql(sql);
        return sql;
	}
	@RequestMapping("/addAccountInfo")
	public String addAccountInfo(@RequestParam String accountStr)
	{
		String[] lines = accountStr.trim().split(",");
		int i=0;
		IDbTableDao dao = (IDbTableDao) DaoBeanFactory.getBean(IDbTableDao.class);
		StringBuffer sb = new StringBuffer("INSERT INTO tbl_website_account_info VALUES (" + (1));
		for(String s : lines)
		{
			int index = i++%3;
		    sb.append(",'"+s+"'");
		    if(index == 2)
		    {
		    	sb.append(");");
		    	dao.executeSql(sb.toString());
		        sb = new StringBuffer("INSERT INTO tbl_website_account_info VALUES (" + (i+1));
		    }
		}
		return "SUCCESS";
	}
}
