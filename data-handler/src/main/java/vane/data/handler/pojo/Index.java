package vane.data.handler.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Index implements Serializable {
  String code;
  String name;
}
