package com.example.demo.mybatis;

import com.example.demo.annotation.DaoLoad;
import com.example.demo.utils.ReflectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.util.*;

/**
 * @author jiejT
 * @ClassName: LoadDAOClasses
 * @description: 根据包路径加载DAO的classes文件
 * @date 2021/12/11
 */
public class LoadDAOClasses {
    /*
     *包扫描的基础路径
     */
    private String basePath;
    private static final String DAO_UPPER_PATH = "/**/*DAO.class";
    private static final String DAO_LOWER_PATH = "/**/*Dao.class";
    private List<AnnotationTypeFilter> typeFilters = new ArrayList<>();
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    protected final Log logger = LogFactory.getLog(this.getClass());

    public LoadDAOClasses(String basePath) {
        this.basePath = basePath.replace(".","/");
        this.typeFilters.add(new AnnotationTypeFilter(DaoLoad.class, false));
    }

    public Map<String,Set<String>> getClassMap() throws IOException {
        String strPatternDAO = basePath+DAO_UPPER_PATH;
        String strPatternDao = basePath+DAO_LOWER_PATH;
        Resource [] DAOResources = resourcePatternResolver.getResources(strPatternDAO);
        Resource [] DaoResources = resourcePatternResolver.getResources(strPatternDao);
        Resource [] resources = Arrays.copyOf(DAOResources, DAOResources.length + DaoResources.length);
        System.arraycopy(DaoResources, 0, resources, DAOResources.length, DaoResources.length);

        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

        Map<String,Set<String>> result = new HashMap<>();
        for(int idx = 0,size=resources.length; idx < size; ++idx) {
            Set<String> tmpList = null;
            Resource resource = resources[idx];
            if (resource.isReadable()) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                if (this.matchesEntityTypeFilter(reader, readerFactory)) {
                    String strClassName = reader.getClassMetadata().getClassName();
                    String strDataSourceName = this.getDataSourceName(strClassName);
                    if(result.containsKey(strDataSourceName)){
                        tmpList = result.get(strDataSourceName);
                    }else{
                        tmpList = new HashSet<>();
                    }
                    tmpList.add(strClassName);
                    result.put(strDataSourceName, tmpList);
                }
            }
        }

        return result;
    }

    private boolean matchesEntityTypeFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
        Iterator var3 = this.typeFilters.iterator();

        AnnotationTypeFilter filter;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            filter = (AnnotationTypeFilter)var3.next();
        } while(!filter.match(reader, readerFactory));

        return true;
    }

    private String getDataSourceName(String className) {
        String dataSource = "default";

        try {
            Class objClass = Class.forName(className);
            dataSource = (String) ReflectUtil.getFiledValue4I(objClass, "dataSourceName");
        } catch (Exception var4) {
            logger.error(String.format("读取 dataSource ，请检查你的DAO是否继承BaseDAO，className：" + className, var4));
        }

        return dataSource;
    }
}
