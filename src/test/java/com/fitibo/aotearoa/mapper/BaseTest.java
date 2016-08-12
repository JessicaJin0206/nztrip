package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.Application;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:sql/20160724-ddl.sql", "classpath:sql/20160729-dml.sql", "classpath:sql/20160806-ddl.sql", "classpath:sql/20160810-ddl.sql", "classpath:sql/20160810-dml.sql", "classpath:sql/20160811-ddl.sql", "classpath:sql/20160812-ddl.sql"})
public abstract class BaseTest {
}
