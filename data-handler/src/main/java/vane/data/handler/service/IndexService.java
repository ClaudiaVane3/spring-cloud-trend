package vane.data.handler.service;

import cn.hutool.core.collection.CollectionUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vane.data.handler.pojo.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IndexService {

  @Value("${raw.data.url}")
  private String rawDataUrl;

  @Autowired RestTemplate restTemplate;

  public IndexService() {}

  @HystrixCommand(fallbackMethod = "fetchIndexFailed")
  public List<Index> fetchIndex() {
    List<Map> rawData = restTemplate.getForObject(rawDataUrl, List.class);
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
