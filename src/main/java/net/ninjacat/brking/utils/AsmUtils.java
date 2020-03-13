package net.ninjacat.brking.utils;

import java.util.Optional;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class AsmUtils {
  private AsmUtils() {}

  public static String className(final String descriptor) {
    return Type.getObjectType(descriptor).getClassName();
  }

  public static String modifiers(final int access) {
    final var sb = new StringBuilder();
    if ((access & Opcodes.ACC_PUBLIC) != 0) {
      sb.append("public ");
    }
    if ((access & Opcodes.ACC_PRIVATE) != 0) {
      sb.append("private ");
    }
    if ((access & Opcodes.ACC_PROTECTED) != 0) {
      sb.append("protected ");
    }
    if ((access & Opcodes.ACC_ABSTRACT) != 0) {
      sb.append("abstract ");
    }
    if ((access & Opcodes.ACC_STATIC) != 0) {
      sb.append("static ");
    }
    if ((access & Opcodes.ACC_FINAL) != 0) {
      sb.append("final ");
    }
    if ((access & Opcodes.ACC_NATIVE) != 0) {
      sb.append("native ");
    }
    return sb.toString();
  }

  public static Optional<String> stricterAccessWarning(final int newer, final int older) {
    final var newerAccess = AccessLevel.fromAccess(newer);
    final var olderAccess = AccessLevel.fromAccess(older);
    if (newerAccess.ordinal() < olderAccess.ordinal()) {
      return Optional
          .of(String.format("Access level has changed from '%s' to '%s'", olderAccess.text(), newerAccess.text()));
    }
    else {
      return Optional.empty();
    }
  }
}
