package vane.raw.data;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RawDataApplication {

  public static void main(String[] args) {
    int port = 8090;
    int eurekaServerPort = 8761;

    if (NetUtil.isUsableLocalPort(eurekaServerPort)) {
      System.err.println(eurekaServerPort + " is not on service, eureka service failed.");
      System.exit(1);
    }

    if (args != null && args.length != 0) {
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
      System.err.println(port + "is occupied");
      System.exit(1);
    }

    new SpringApplicationBuilder(RawDataApplication.class)
        .properties("server.port=" + port)
        .run(args);
  }
}
