package com.baidu.mapper;

import com.baidu.Entity.AccEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * @ClassName test
 * @Description: TODO
 * @Author liuhaojie
 * @Date 2020/12/30
 * @Version V1.0
 **/
public class test {
    public static void main(String[] args) {
        InputStream inputStream =
                test.class.getClassLoader().getResourceAsStream("config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new
                SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory =
                sqlSessionFactoryBuilder.build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        String statement = "com.baidu.mapper.AccMapper.save";
        AccEntity account = new AccEntity(1,"张三","123123",22);
        sqlSession.insert(statement,account);
        sqlSession.commit();
    }
}
