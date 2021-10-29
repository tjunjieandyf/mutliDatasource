package com.example.demo.hello.dao;

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
@Repository
public interface MyDAO {
    List<Map<String,Object>> getRecord();
}
