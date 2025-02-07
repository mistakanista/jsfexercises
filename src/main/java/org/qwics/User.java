package org.qwics;


import java.io.Serializable;

public class User implements Serializable {

  private String vorname;
  private String nachname;

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  public String getNachname() {
    return nachname;
  }

  public void setNachname(String nachname) {
    this.nachname = nachname;
  }
}
