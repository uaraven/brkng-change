package net.ninjacat.brking.utils;

import com.google.common.collect.ImmutableMap;
import net.ninjacat.brking.api.ApiObject;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class AsmUtils {
    private static final Map<Integer, String> ACCESS_MODS = Map.of(
            Opcodes.ACC_PUBLIC, "public",
            Opcodes.ACC_PRIVATE, "private",
            Opcodes.ACC_PROTECTED, "protected"
    );

    private static final Map<Integer, String> MODIFIERS = Map.of(
            Opcodes.ACC_STATIC, "static",
            Opcodes.ACC_FINAL, "final",
            Opcodes.ACC_NATIVE, "native",
            Opcodes.ACC_SYNCHRONIZED, "synchronized",
            Opcodes.ACC_VOLATILE, "volatile");

    private static final Map<Integer, String> ACC_ALL = buildAccAll();

    private static Map<Integer, String> buildAccAll() {
        final var acc = new HashMap<Integer, String>();
        acc.putAll(ACCESS_MODS);
        acc.putAll(MODIFIERS);
        return ImmutableMap.copyOf(acc);
    }

    private AsmUtils() {
    }

    public static String className(final String descriptor) {
        return Type.getObjectType(descriptor).getClassName();
    }

    public static String modifiersToString(final int access) {
        return ACC_ALL.entrySet().stream()
                .filter(e -> (access & e.getKey()) != 0)
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(" "));
    }

    public static String modifiersOnly(final int access) {
        return MODIFIERS.entrySet().stream()
                .filter(e -> (access & e.getKey()) != 0)
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(" "));
    }

    public static String accessToString(final int access) {
        return ACCESS_MODS.entrySet().stream()
                .filter(e -> (e.getKey() & access) != 0)
                .map(Map.Entry::getValue)
                .findFirst().orElse("package private");
    }

    public static boolean hasAccessChangedToStricter(final int older, final int newer) {
        return AccessLevel.fromAccess(older).ordinal() < AccessLevel.fromAccess(newer).ordinal();
    }

    public static boolean hasModifierChanged(final ApiObject older, final ApiObject newer) {
        return hasModifierChanged(older, newer, Opcodes.ACC_FINAL)
                || hasModifierChanged(older, newer, Opcodes.ACC_STATIC);
    }

    public static boolean hasModifierChanged(final ApiObject older, final ApiObject newer, final int modBit) {
        return (older.access() & modBit) != (newer.access() & modBit);
    }
}
