package net.ninjacat.apidiff.utils;

import org.objectweb.asm.Opcodes;

public enum AccessLevel {
  PRIVATE("private"),
  PROTECTED("protected"),
  PACKAGE_PRIVATE("package private"),
  PUBLIC("public");

  private final String text;

  AccessLevel(final String text) {
    this.text = text;
  }

  public String text() {
    return text;
  }

  public static AccessLevel fromAccess(final int access) {
    if ((access & Opcodes.ACC_PUBLIC) != 0) {
      return PUBLIC;
    }
    else if ((access & Opcodes.ACC_PRIVATE) != 0) {
      return PRIVATE;
    }
    else if ((access & Opcodes.ACC_PROTECTED) != 0) {
      return PROTECTED;
    }
    else {
      return PACKAGE_PRIVATE;
    }
  }
}
