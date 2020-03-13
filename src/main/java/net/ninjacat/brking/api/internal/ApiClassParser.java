package net.ninjacat.brking.api.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.api.ApiField;
import net.ninjacat.brking.api.ApiMethod;
import net.ninjacat.brking.api.ImmutableApiClass;
import net.ninjacat.brking.api.ImmutableApiField;
import net.ninjacat.brking.api.ImmutableApiMethod;
import net.ninjacat.brking.utils.AccessLevel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static net.ninjacat.brking.utils.AsmUtils.className;
import static org.objectweb.asm.ClassReader.SKIP_CODE;
import static org.objectweb.asm.ClassReader.SKIP_DEBUG;
import static org.objectweb.asm.ClassReader.SKIP_FRAMES;

public final class ApiClassParser extends ClassVisitor {
  private final List<ApiField> fields = new ArrayList<>();

  private final List<ApiMethod> methods = new ArrayList<>();

  private final ImmutableApiClass.Builder apiClassBuilder = ImmutableApiClass.builder();

  private ApiClassParser() {
    super(Opcodes.ASM7);
  }

  public static ApiClass of(final InputStream stream) {
    final var classParser = new ApiClassParser();
    classParser.readClass(stream);
    return classParser.getApiClass();
  }

  public static ApiClass of(final String className) {
    final var jarClassReader = new ApiClassParser();
    jarClassReader.readClass(className);
    return jarClassReader.getApiClass();
  }

  public void readClass(final String className) {
    try {
      final ClassReader classReader = new ClassReader(className);
      classReader.accept(this, SKIP_CODE + SKIP_DEBUG + SKIP_FRAMES);
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public void readClass(final InputStream stream) {
    try {
      final ClassReader classReader = new ClassReader(stream);
      classReader.accept(this, SKIP_CODE + SKIP_DEBUG + SKIP_FRAMES);
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void visit(final int version,
                    final int access,
                    final String name,
                    final String signature,
                    final String superName,
                    final String[] interfaces) {
    super.visit(version, access, name, signature, superName, interfaces);
    apiClassBuilder.version(version);
    apiClassBuilder.access(access);
    apiClassBuilder.name(className(name));
    apiClassBuilder.signature(signature);
    apiClassBuilder.superName(className(superName));
    apiClassBuilder.interfaces(List.of(interfaces));
  }

  @Override
  public FieldVisitor visitField(final int access,
                                 final String name,
                                 final String descriptor,
                                 final String signature,
                                 final Object value) {
    if (AccessLevel.fromAccess(access) == AccessLevel.PUBLIC) {
      fields.add(ImmutableApiField.builder()
                                  .access(access)
                                  .name(name)
                                  .descriptor(descriptor)
                                  .signature(signature)
                                  .build());
    }
    return super.visitField(access, name, descriptor, signature, value);
  }

  @Override
  public MethodVisitor visitMethod(final int access,
                                   final String name,
                                   final String descriptor,
                                   final String signature,
                                   final String[] exceptions) {
    if (AccessLevel.fromAccess(access) == AccessLevel.PUBLIC) {
      methods.add(ImmutableApiMethod.builder()
                                    .access(access)
                                    .name(name)
                                    .descriptor(descriptor)
                                    .signature(signature)
                                    .exceptions(Optional.ofNullable(exceptions).map(List::of).orElse(List.of()))
                                    .build());
    }
    return super.visitMethod(access, name, descriptor, signature, exceptions);
  }

  public ApiClass getApiClass() {
    return apiClassBuilder
        .fields(fields)
        .methods(methods)
        .build();
  }
}
