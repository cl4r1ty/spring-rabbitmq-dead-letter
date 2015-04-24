package vc.c4.deadletter.domain;

import java.util.Date;

public class ExampleObject {

  private Date date = new Date();

  public ExampleObject() {
  }

  @Override
  public String toString() {
    return "ExampleObject{" +
        "date= " + date +
        '}';
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

}
