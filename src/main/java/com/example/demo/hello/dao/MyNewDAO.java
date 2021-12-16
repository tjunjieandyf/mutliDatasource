package com.example.demo.hello.dao;

import com.example.demo.annotation.DaoLoad;
import com.example.demo.base.dao.BaseDAO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author jiejT
 * @ClassName: MyDao
 * @description: TODO
 * @date 2021/10/26
 */
//@Repository
@DaoLoad
public interface MyNewDAO extends BaseDAO {
    String dataSourceName = "test1";
    int save(Map<String,Object> param);
}
