package net.ninjacat.brking.output;

import com.google.common.collect.ImmutableMap;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StrSubstitutor;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static io.vavr.API.*;
import static io.vavr.Predicates.is;
import static org.fusesource.jansi.Ansi.ansi;

public class ConsolePrinter implements DiffPrinter {
    /**
     * Color to hightlight changes
     */
    private static final Ansi.Color HL = Color.BLUE;

    /**
     * Color to hightlight member names
     */
    private static final Ansi.Color MBR = Color.YELLOW;

    /**
     * Messages for different types of changes
     */
    private static final Map<DiffType, String> MESSAGES = ImmutableMap.<DiffType, String>builder()
            .put(DiffType.ClassAdded, ansi().a("Class [").fgBright(HL).a("${changed.to}").fgDefault().a("] has been added")
                    .toString())
            .put(DiffType.ClassRemoved, ansi().a("Class [").fgBright(HL).a("${changed.from}").fgDefault()
                    .a("] has been removed").toString())
            .put(DiffType.ClassInterfaceAdded, ansi().a("Interfaces were added: [").fgBright(HL).a("${changed.to}")
                    .fgDefault().a("]").toString())
            .put(DiffType.ClassInterfaceRemoved, ansi().a("Interfaces were removed: [").fgBright(HL).a("${changed.from}")
                    .fgDefault().a("]").toString())
            .put(DiffType.ClassModifiersChanged, ansi().a("Modifiers changed from [").fgBright(HL).a(
                    "${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]").toString())
            .put(DiffType.ClassVisibilityChanged, ansi().a("Visibility has changed: [").fgBright(HL).a("${changed.to}")
                    .fgDefault().a("]").toString())
            .put(DiffType.ClassVersionIncremented, ansi().a("Class file version changed from [").fgBright(HL).a(
                    "${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]").toString())
            .put(DiffType.ClassVersionDecremented, ansi().a("Class file version changed from [").fgBright(HL).a(
                    "${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]").toString())
            .put(DiffType.ClassSuperClassChanged, ansi().a("Super class changed from [").fgBright(HL).a("${changed.from}")
                    .fgDefault().a("] to [").fgBright(HL).a("${changed.to}").a("]").fgDefault().toString())
            .put(DiffType.ClassAnnotationsChanged, ansi().a("Annotations changed from [").fgBright(HL).a(
                    "${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]").toString())
            .put(DiffType.FieldModifiersChanged, ansi().a("Field [").fg(MBR).a("${object.apiName}").fgDefault().a(
                    "] modifiers changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]")
                    .toString())
            .put(DiffType.FieldRemoved, ansi().a("Field [").fg(MBR).a("${object.apiName}").fgDefault().a("] has been removed")
                    .fgDefault().toString())
            .put(DiffType.FieldAdded, ansi().a("Field [").fg(MBR).a("${object.apiName}").fgDefault().a("] has been added")
                    .fgDefault().toString())
            .put(DiffType.FieldTypeChanged, ansi().a("Field [").fg(MBR).a("${object.apiName}").fgDefault().a(
                    "] type changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]")
                    .toString())
            .put(DiffType.FieldVisibilityChanged, ansi().a("Field [").fg(MBR).a("${object.apiName}").fgDefault().a(
                    "] visibility changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]")
                    .toString())
            .put(DiffType.FieldAnnotationsChanged, ansi().a("Field ]").fg(MBR).a("${object.apiName}").fgDefault().a(
                    "] annotations changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").a("]").fgDefault()
                    .toString())
            .put(DiffType.MethodModifiersChanged, ansi().a("Method [").fg(MBR).a("${object.apiName}").fgDefault().a(
                    "] modifiers changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]")
                    .toString())
            .put(DiffType.MethodRemoved, ansi().a("Method [").fg(MBR).a("${object.apiName}").fgDefault().a("] has been removed")
                    .fgDefault().toString())
            .put(DiffType.MethodAdded, ansi().a("Method [").fg(MBR).a("${object.apiName}").fgDefault().a("] has been added")
                    .fgDefault().toString())
            .put(DiffType.MethodReturnTypeChanged, ansi().a("Method [").fg(MBR).a("${object.apiName}").fgDefault()
                    .a("] return type changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault().a("]")
                    .toString())
            .put(DiffType.MethodVisibilityChanged, ansi().a("Method ").fg(MBR).a("${object.apiName}").fgDefault().a(
                    "] visibility changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}").fgDefault()
                    .a("]").toString())
            .put(DiffType.MethodExceptionsChanged, ansi().a("Method [").fg(MBR).a("${object.apiName}").fgDefault().a(
                    "] exception list changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}")
                    .fgDefault().a("]").toString())
            .put(DiffType.MethodAnnotationsChanged, ansi().a("Method [").fg(MBR).a("${object.apiName}").fgDefault().a(
                    "] annotations changed from [")
                    .fgBright(HL).a("${changed.from}").fgDefault().a("] to [").fgBright(HL).a("${changed.to}")
                    .fgDefault().a("]").toString())
            .build();

    private static Ansi.Color sevColor(final DiffType type) {
        return Match(type.getSeverity()).of(
                Case($(is(ChangeSeverity.BREAKING)), s -> Color.RED),
                Case($(is(ChangeSeverity.WARNING)), s -> Color.YELLOW),
                Case($(is(ChangeSeverity.SAFE)), s -> Color.GREEN)
        );
    }

    @Override
    public void print(final List<DiffElement> diff) {
        final AtomicReference<String> currentClass = new AtomicReference<>("");
        diff.forEach(e -> {
            if (!e.ownerClass().apiName().equals(currentClass.get())) {
                System.out.println();
                System.out.println(ansi().fgDefault().a("Class ").fgBrightDefault().a(e.ownerClass().apiName()));
                System.out.println(ansi().a(StringUtils.leftPad("", 6 + e.ownerClass().apiName().length(), '-')).reset());
                currentClass.set(e.ownerClass().apiName());
            }
            final var template = new StrSubstitutor(e.asTemplateContext());
            final var prefix = ansi().fgDefault().a("[").fg(sevColor(e.diffType())).a(e.diffType().getSeverity().name())
                    .fgDefault().a("] ").toString();
            final var message = template.replace(MESSAGES.get(e.diffType()));

            System.out.println(prefix + message);
        });
    }
}
