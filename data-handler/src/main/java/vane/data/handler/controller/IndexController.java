package vane.data.handler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vane.data.handler.pojo.Index;
import vane.data.handler.service.IndexService;

import java.util.List;

@RestController
public class IndexController {

  @Autowired IndexService indexService;

  @GetMapping("/getIndex")
  public List<Index> get() {
    return indexService.fetchIndex();
  }
}
