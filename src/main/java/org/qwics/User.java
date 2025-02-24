package org.qwics;


import java.io.Serializable;
import lombok.Data;

@Data
public class User implements Serializable {

  private int id;
  private String vorname;
  private String nachname;
  private String address;
}
