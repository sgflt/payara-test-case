package cz.i.mojarra;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Lukáš Kvídera
 */
@Named("abc")
@ViewScoped
public class AjaxBugComponent implements Serializable {

  public String getSomething() {
    System.out.println("getSomething()");
    return "sometext";
  }

  public void setSomething(String something) {
    System.out.println("getSomething(something={})");
  }

  public void doAjax() {
    System.out.println("doAjax()");
  }
}
