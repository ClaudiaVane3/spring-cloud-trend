package vane.data.handler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vane.data.handler.service.IndexService;

import java.util.List;

@RestController
public class IndexController {

  @Autowired IndexService indexService;

  @GetMapping("/freshIndex")
  public String fresh() {
    indexService.fresh();
    return "fresh indexes successfully!";
  }

  @GetMapping("/getIndex")
  public List get() {
    return indexService.get();
  }

  @GetMapping("/removeIndex")
  public String remove() {
    indexService.remove();
    return "index removed.";
  }
}
