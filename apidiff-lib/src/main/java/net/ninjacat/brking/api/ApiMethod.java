package net.ninjacat.brking.api;

import io.vavr.Lazy;
import net.ninjacat.brking.utils.AsmUtils;
import org.immutables.value.Value;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Value.Immutable
public abstract class ApiMethod implements ApiClassElement {
    private final Lazy<Method> method = Lazy.of(() -> new Method(name(), descriptor()));
    private final Lazy<String> identifier = Lazy.of(() -> {
        final var params = Arrays.stream(method.get().getArgumentTypes())
                .map(Type::getDescriptor)
                .collect(Collectors.joining(""));

        //return String.format("%s.%s(%s)", ownerName(), name(), params);
        return String.format("%s(%s)", name(), params);
    });

    public abstract List<String> exceptions();

    public String identifier() {
        return identifier.get();
    }

    @Override
    public String returnType() {
        return method.get().getReturnType().getClassName();
    }

    @Override
    public String apiName() {
        final var params = Arrays.stream(method.get().getArgumentTypes())
                .map(Type::getClassName)
                .collect(Collectors.joining(","));

        return String.format("%s %s %s(%s)", AsmUtils.modifiersToString(access()),
                method.get().getReturnType().getClassName(),
                name(),
                params);
    }

    @Override
    public String apiDescription(final ApiObject owner) {
        return apiName() + " in " + owner.apiDescription(null);
    }
}
