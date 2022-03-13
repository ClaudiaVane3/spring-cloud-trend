package vane.data.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class DataHandlerApplication {
  public static void main(String[] args) {

    int port = 8001;
    int eurekaServerPort = 8761;
    if (NetUtil.isUsableLocalPort(eurekaServerPort)) {
      System.err.println(eurekaServerPort + " is not on service");
      System.exit(1);
    }

    if (null != args && 0 != args.length) {
      for (String arg : args) {
        if (arg.startsWith("port=")) {
          String strPort = StrUtil.subAfter(arg, "port=", true);
          if (NumberUtil.isNumber(strPort)) {
            port = Convert.toInt(strPort);
          }
        }
      }
    }

    if (!NetUtil.isUsableLocalPort(port)) {
      System.err.printf("端口%d被占用了，无法启动%n", port);
      System.exit(1);
    }
    new SpringApplicationBuilder(DataHandlerApplication.class)
        .properties("server.port=" + port)
        .run(args);
  }

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
