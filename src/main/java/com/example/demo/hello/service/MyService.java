package com.example.demo.hello.service;

import com.example.demo.hello.dao.MyDAO;
import com.example.demo.hello.dao.MyNewDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jiejT
 * @ClassName: MyService
 * @description: TODO
 * @date 2021/10/26
 */
@Service
public class MyService {
        @Autowired
        private MyDAO dao;
        @Autowired
        private MyNewDAO newDao;

        public Map<String,Object> getRecordInfo(){
//            List<Map<String,Object>> result = dao.getRecord();
//            print(result.get(0));
//            return result.get(0);
            int count = dao.update();
            if(count>0){
                Map<String,Object> record = new HashMap<>();
                record.put("XH", UUID.randomUUID());
                record.put("USERID", UUID.randomUUID());
                record.put("USERMC", "aaa");
                record.put("INSERTTIME", new Date());
                newDao.save(record);
                return record;
            }
            return null;
        }

        private void print(Map<String,Object> map){
            if(map !=null &&map.size()>0){
                for (Map.Entry<String,Object> entry:map.entrySet()){
                    StringBuffer str = new StringBuffer();
                    str.append(entry.getKey()).append("=").append(entry.getValue());
                    System.out.println(str.toString());
                }
            }
        }
}
