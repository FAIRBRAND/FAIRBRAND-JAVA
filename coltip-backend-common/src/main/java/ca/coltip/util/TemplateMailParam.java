package ca.coltip.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.context.AbstractContext;

import java.util.Locale;

@Getter
@Setter
public class TemplateMailParam extends AbstractContext {
  @Data
  public static class Subject {
    private String code;
    private Object[] args;

    public Subject(String code) {
      this.code = code;
    }

    public Subject(String code, Object[] args) {
      this.code = code;
      this.args = args;
    }
  }

  private final String[] targetAddresses;
  private final Subject subject;
  private final String template;

  public TemplateMailParam(
    String[] targetAddresses,
    String template,
    Subject subject
  ) {
    super();
    this.targetAddresses = targetAddresses;
    this.template = template;
    this.subject = subject;
  }

  public TemplateMailParam(
    String[] targetAddresses,
    Locale locale,
    String template,
    Subject subject
  ) {
    super(locale);
    this.targetAddresses = targetAddresses;
    this.template = template;
    this.subject = subject;
  }

  public TemplateMailParam(
    String targetAddress,
    String template,
    Subject subject
  ) {
    this(new String[] {targetAddress}, template, subject);
  }

  public TemplateMailParam(
    String targetAddress,
    Locale locale,
    String template,
    Subject subject
  ) {
    this(new String[] {targetAddress}, locale, template, subject);
  }
}
