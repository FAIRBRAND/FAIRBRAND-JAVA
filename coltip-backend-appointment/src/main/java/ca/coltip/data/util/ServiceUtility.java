package ca.coltip.data.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ServiceUtility {
  public static <T> void modify(Consumer<T> setter, Supplier<T> getter) {
    final var value = getter.get();
    if (value != null) setter.accept(value);
  }
}
