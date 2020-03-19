package net.ninjacat.brking.api.internal;

import net.ninjacat.brking.api.*;
import net.ninjacat.brking.api.ImmutableApiField.Builder;
import net.ninjacat.brking.utils.AccessLevel;
import net.ninjacat.brking.utils.AsmUtils;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.ninjacat.brking.utils.AsmUtils.className;
import static org.objectweb.asm.ClassReader.*;

public final class ApiClassParser
        extends ClassVisitor {
    private final List<ImmutableApiField.Builder> fields = new ArrayList<>();

    private final List<ImmutableApiMethod.Builder> methods = new ArrayList<>();

    private final ImmutableApiClass.Builder apiClassBuilder = ImmutableApiClass.builder();

    private final boolean publicOnly;

    private String className;

    private ApiClassParser(final boolean publicOnly) {
        super(Opcodes.ASM7);
        this.publicOnly = publicOnly;
    }

    public static Optional<ApiClass> of(final InputStream stream, final boolean publicOnly) {
        final var classParser = new ApiClassParser(publicOnly);
        classParser.readClass(stream);
        return classParser.getApiClass();
    }

    public static Optional<ApiClass> of(final String className, final boolean publicOnly) {
        final var classParser = new ApiClassParser(publicOnly);
        classParser.readClass(className);
        return classParser.getApiClass();
    }

    public static Optional<ApiClass> ofPublic(final String className) {
        final var jarClassReader = new ApiClassParser(true);
        jarClassReader.readClass(className);
        return jarClassReader.getApiClass();
    }

    public static Optional<ApiClass> of(final String className) {
        final var jarClassReader = new ApiClassParser(false);
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
    public void visit(
            final int version,
            final int access,
            final String name,
            final String signature,
            final String superName,
            final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        apiClassBuilder.version(version);
        apiClassBuilder.access(access);
        this.className = className(name);
        apiClassBuilder.name(className);
        apiClassBuilder.signature(signature);
        apiClassBuilder.superName(className(superName));
        apiClassBuilder.interfaces(List.of(interfaces).stream().map(AsmUtils::className).collect(Collectors.toList()));
    }

    @Override
    public FieldVisitor visitField(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        if (!publicOnly || AccessLevel.fromAccess(access) == AccessLevel.PUBLIC && !AsmUtils.isSynthetic(access)) {
            final Builder fieldBuilder = ImmutableApiField.builder()
                    .access(access)
                    .ownerName(className)
                    .name(name)
                    .descriptor(descriptor)
                    .signature(signature);
            fields.add(fieldBuilder);
            return new ApiFieldParser(api, super.visitField(access, name, descriptor, signature, value), fieldBuilder);
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        if ((!publicOnly || AccessLevel.fromAccess(access) == AccessLevel.PUBLIC) && !AsmUtils.isSynthetic(access)) {
            final var builder = ImmutableApiMethod.builder()
                    .access(access)
                    .ownerName(className)
                    .name(name)
                    .descriptor(descriptor)
                    .signature(signature)
                    .exceptions(Optional.ofNullable(exceptions)
                            .map(List::of)
                            .orElse(List.of()).stream()
                            .map(AsmUtils::className)
                            .collect(Collectors.toUnmodifiableList()));
            methods.add(builder);
            return new ApiMethodParser(api, super.visitMethod(access, name, descriptor, signature, exceptions), builder);
        } else {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        apiClassBuilder.addAnnotations(ImmutableApiAnnotation.of(AsmUtils.descriptorToClass(descriptor), visible));
        return super.visitAnnotation(descriptor, visible);
    }

    public Optional<ApiClass> getApiClass() {
        if (className.equals("module-info")) {
            return Optional.empty();
        }
        return Optional.of(apiClassBuilder
                .fields(fields.stream().map(ImmutableApiField.Builder::build).collect(Collectors.toUnmodifiableList()))
                .methods(methods.stream().map(ImmutableApiMethod.Builder::build).collect(Collectors.toUnmodifiableList()))
                .build());
    }

    private static class ApiFieldParser
            extends FieldVisitor {
        private final ImmutableApiField.Builder builder;

        public ApiFieldParser(final int api, final FieldVisitor fieldVisitor, final ImmutableApiField.Builder builder) {
            super(api, fieldVisitor);
            this.builder = builder;
        }

        @Override
        public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
            builder.addAnnotations(ImmutableApiAnnotation.of(AsmUtils.descriptorToClass(descriptor), visible));
            return super.visitAnnotation(descriptor, visible);
        }
    }

    private static class ApiMethodParser
            extends MethodVisitor {
        private final ImmutableApiMethod.Builder builder;

        public ApiMethodParser(final int api, final MethodVisitor methodVisitor, final ImmutableApiMethod.Builder builder) {
            super(api, methodVisitor);
            this.builder = builder;
        }

        @Override
        public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
            builder.addAnnotations(ImmutableApiAnnotation.of(AsmUtils.descriptorToClass(descriptor), visible));
            return super.visitAnnotation(descriptor, visible);
        }
    }
}
