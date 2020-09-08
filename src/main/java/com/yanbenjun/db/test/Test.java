package com.yanbenjun.db.test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.yanbenjun.db.DbField;
import com.yanbenjun.db.DbFieldDataSource;
import com.yanbenjun.db.DbTable;
import com.yanbenjun.db.dao.DaoBeanAware;
import com.yanbenjun.db.dao.DaoBeanFactory;
import com.yanbenjun.db.dao.IDbFieldDao;
import com.yanbenjun.db.dao.IDbTableDao;
import com.yanbenjun.db.dao.IStudyDatabaseDao;
import com.yanbenjun.db.type.SqlType;

public class Test
{
    public static long getId()
    {
        return Math.abs(UUID.randomUUID().getMostSignificantBits());
    }
    /**
     * `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `level` int(3) DEFAULT NULL,
  `vip_level` int(2) DEFAULT '0',
  `total_power` int(20) DEFAULT NULL,
  `last_online_time` int(2) DEFAULT NULL,
  `main_hero` varchar(20) DEFAULT NULL,
  `main_hero_power` int(20) DEFAULT NULL,
  `max_microcosm` int(2) DEFAULT NULL,
     * @param args
     */
    public static void main(String[] args)
    {
        testThread();
        testForkjoin();
        //List<DbField> list = createTestTable2();
        
        /*IDbTableDao dao = (IDbTableDao) DaoBeanFactory.getBean(IDbTableDao.class);
        
        DbTable table = dao.queryTableInfoById(3L);
        System.out.println(new Gson().toJson(table));
        System.out.println(table.getCreateTableSql());*/
        testsynInsert();
    }
    
    private static void testThread() {
        final AtomicInteger ai = new AtomicInteger();
        CountDownLatch cdl = new CountDownLatch(10000);
        for (int i=0; i<10000; i++) {
            new Thread() {
                @Override
                public void run() {
                    ai.getAndIncrement();
                    cdl.countDown();
                }
            }.start();
        }
        
        try
        {
            cdl.await();
            System.out.println(ai.get());
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private static void testForkjoin() {
        final AtomicInteger ai = new AtomicInteger();
        Stream.generate(() -> {
            ai.getAndIncrement();
            return "S";
        }).limit(10000).collect(Collectors.toList());
        System.out.println(ai.get());
        
    }
    
    public static void testInsert() {
        List<DbTable> allTestTables = generate(10000);
        long s = System.currentTimeMillis();
        allTestTables.parallelStream().forEach(Test::addTable);
        long s1 = System.currentTimeMillis();
        System.out.println("cost " +(s1-s));
    }
    
    public static void testsynInsert() {
        List<DbTable> allTestTables = generate(10000);
        long s = System.currentTimeMillis();
        allTestTables.stream().forEach(Test::addTable);
        long s1 = System.currentTimeMillis();
        System.out.println("cost " +(s1-s));
    }
    
    private static void addTable(DbTable dbTable) {
        DaoBeanAware<IDbTableDao>  tableDao = new DaoBeanAware<>(IDbTableDao.class);
        tableDao.getDaoBean().addAll(Stream.of(dbTable).collect(Collectors.toList()));
        tableDao.getSession().commit();
        tableDao.getSession().close();
    }
    
    private static List<DbTable> generate(int size) {
        final AtomicInteger ai = new AtomicInteger();
        return Stream.generate(() ->{
            DbTable table = new DbTable();
            table.setId(getId());
            table.setName("test");
            table.setTableName("test" + ai.getAndIncrement());
            table.setComment("test");
            return table;
        }).limit(size).collect(Collectors.toList());
    }
    
    public static List<DbField> createTestTable2()
    {
        IStudyDatabaseDao dao = DaoBeanFactory.getBean(IStudyDatabaseDao.class);
        dao.executeSql("TRUNCATE TABLE tbl_db_table;");
        dao.executeSql("TRUNCATE TABLE tbl_db_field;");
        
        DbTable dt = new DbTable();
        long tuanjuTableId = getId();
        dt.setId(tuanjuTableId);
        dt.setTableName("tbl_tuanju_info");
        dt.setName("团聚军团团员信息");
        dt.setComment("团聚军团团员信息");
        DbTable dt1 = new DbTable();
        dt1.setId(getId());
        dt1.setTableName("tbl_hero_info");
        dt1.setName("圣斗士信息");
        dt1.setComment("圣斗士信息");
        List<DbTable> dts = Stream.of(dt).collect(Collectors.toList());
        dts.add(dt1);
        IDbTableDao tableDao = DaoBeanFactory.getBean(IDbTableDao.class);
        tableDao.addAll(dts);
        DbFieldDataSource dfds = new DbFieldDataSource();
        dfds.setId(getId());
        
        List<DbField> list = new ArrayList<DbField>();
        
        DbField df = new DbField();
        df.setId(getId());
        df.setOriginName("id");
        df.setPrimaryKey(true);
        df.setNotNull(true);
        df.setSqlType(SqlType.LONG.getValue());
        df.setComment("用户ID");
        df.setOrderInTable(0);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("name");
        df.setPrimaryKey(false);
        df.setNotNull(true);
        df.setSqlType(SqlType.STRING.getValue());
        df.setComment("名称");
        df.setOrderInTable(1);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("level");
        df.setPrimaryKey(false);
        df.setNotNull(false);
        df.setSqlType(SqlType.INTEGER.getValue());
        df.setComment("等级");
        df.setOrderInTable(2);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("vip_level");
        df.setPrimaryKey(false);
        df.setNotNull(false);
        df.setSqlType(SqlType.INTEGER.getValue());
        df.setComment("VIP等级");
        df.setOrderInTable(3);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("total_power");
        df.setPrimaryKey(false);
        df.setNotNull(false);
        df.setSqlType(SqlType.INTEGER.getValue());
        df.setComment("总战力");
        df.setOrderInTable(4);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("last_online_time");
        df.setPrimaryKey(false);
        df.setNotNull(false);
        df.setSqlType(SqlType.INTEGER.getValue());
        df.setComment("最后在线时间");
        df.setOrderInTable(5);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("main_hero_id");
        df.setPrimaryKey(false);
        df.setNotNull(true);
        df.setSqlType(SqlType.LONG.getValue());
        df.setComment("主英雄ID");
        df.setOrderInTable(6);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("main_hero");
        df.setPrimaryKey(false);
        df.setNotNull(false);
        df.setSqlType(SqlType.STRING.getValue());
        df.setComment("主英雄名称");
        df.setOrderInTable(7);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("main_hero_power");
        df.setPrimaryKey(false);
        df.setNotNull(false);
        df.setSqlType(SqlType.INTEGER.getValue());
        df.setComment("主英雄战力");
        df.setOrderInTable(8);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("max_microcosm");
        df.setPrimaryKey(false);
        df.setNotNull(false);
        df.setSqlType(SqlType.INTEGER.getValue());
        df.setComment("小宇宙最大等级");
        df.setOrderInTable(9);
        df.setDbTable(dt);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        //tbl_hero_info
        df = new DbField();
        df.setId(getId());
        df.setOriginName("id");
        df.setPrimaryKey(true);
        df.setNotNull(true);
        df.setSqlType(SqlType.LONG.getValue());
        df.setComment("英雄ID");
        df.setDbTable(dt1);
        df.setFieldDataSource(dfds);
        list.add(df);
        
        df = new DbField();
        df.setId(getId());
        df.setOriginName("name");
        df.setPrimaryKey(false);
        df.setNotNull(true);
        df.setSqlType(SqlType.STRING.getValue());
        df.setComment("英雄名称");
        df.setDbTable(dt1);
        df.setFieldDataSource(dfds);
        list.add(df);
        IDbFieldDao fieldDao =  DaoBeanFactory.getBean(IDbFieldDao.class);
        fieldDao.addFields(list);
        
        DbTable table = tableDao.queryTableInfoById(tuanjuTableId);
        return list;
    }
}
