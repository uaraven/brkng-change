package net.ninjacat.brking.logging;

import io.vavr.Lazy;

public final class ConsoleLogger implements Logger {
  private static final Lazy<ConsoleLogger> INSTANCE = Lazy.of(ConsoleLogger::new);

  private ConsoleLogger() {
  }

  public static Logger getLogger() {
    return INSTANCE.get();
  }

  @Override
  public void print(final String format, final Object... params) {
    System.out.println(String.format(format, params));
  }

  @Override
  public void print(final String message) {
    System.out.println(message);
  }

  @Override
  public void err(final Throwable cause, final String message) {
    System.err.println(message);
    cause.printStackTrace();
  }

  @Override
  public void err(final Throwable cause, final String format, final Object... params) {
      System.err.println(String.format(format, params));
      cause.printStackTrace();
  }

  @Override
  public void err(final String message) {
    System.err.println(message);
  }

  @Override
  public void err(final String format, final Object... params) {
      System.err.println(String.format(format, params));
  }

  @Override
  public void fail(final String format, final Object... params) {
    err(format, params);
    System.exit(1);
  }

  @Override
  public void fail(final Throwable cause, final String format, final Object... params) {
    err(cause, format, params);
    System.exit(1);
  }
}
