package net.ninjacat.apidiff.logging;

public interface Logger {
  void print(String format, Object... params);

  void print(String message);

  void err(Throwable cause, String message);

  void err(Throwable cause, String format, Object... params);

  void err(String message);

  void err(String format, Object... params);

  void fail(String format, Object... params);

  void fail(Throwable cause, String format, Object... params);
}
