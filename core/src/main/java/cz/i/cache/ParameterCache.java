package cz.i.cache;

import java.io.Serializable;
import java.util.function.Function;


public class ParameterCache extends CodebookCache<String, ParameterCache.Parameter> {

  @Override
  protected Function<Parameter, String> getKeyFunction() {
    return Parameter::getValue;
  }


  public static class Parameter implements Serializable {
    private static final long serialVersionUID = 5464990270100240335L;


    String getValue() {
      return "value";
    }
  }


  public ParameterCache() {
    super(Parameter.class);
  }


  public boolean isEnabled(final String code) {
    final Parameter parameter = get(code);
    if (parameter == null) {
      return false;
    }
    return "A".equals(parameter.getValue());
  }


  public String getStringValue(final String code) {
    final Parameter parameter = get(code);
    if (parameter == null) {
      return null;
    }
    return parameter.getValue();
  }

}
