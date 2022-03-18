package vane.data.handler.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vane.data.handler.pojo.Index;
import vane.data.handler.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Service
// 保存到 redis 的缓存名称是 indexes
@CacheConfig(cacheNames = "indexes")
public class IndexService {

  private List<Index> indexList;

  @Autowired RestTemplate restTemplate;

  @Value("${raw.data.url}")
  private String rawDataUrl;

  @HystrixCommand(fallbackMethod = "fetchIndexFailed")
  public List fresh() {
    indexList = fetchIndex();
    // SpringContextUtil.getBean 重新获取 IndexService
    // 从已经存在的方法里调用 redis 相关方法，并不能触发 redis 相关操作，所以用这种方式重新获取一次
    IndexService indexService = SpringContextUtil.getBean(IndexService.class);
    indexService.remove();
    return indexService.store();
  }

  @CacheEvict(allEntries = true)
  public void remove() {}

  @Cacheable(key = "'all_indexes'")
  public List<Index> store() {
    System.out.println(this);
    return indexList;
  }

  @Cacheable(key = "'all_indexes'")
  public List<Index> get() {
    return CollUtil.toList();
  }

  public List fetchIndex() {
    List rawData = restTemplate.getForObject(rawDataUrl, List.class);
    return map2Index(rawData);
  }

  private List<Index> map2Index(List<Map> rawDataList) {
    List<Index> indexList = new ArrayList<>();
    for (Map map : rawDataList) {
      Index index = new Index(map.get("code").toString(), map.get("name").toString());
      indexList.add(index);
    }
    return indexList;
  }

  private List<Index> fetchIndexFailed() {
    System.out.println("raw data connected failed.");
    Index index = new Index("000000", "无效指数代码");
    return CollectionUtil.toList(index);
  }
}
