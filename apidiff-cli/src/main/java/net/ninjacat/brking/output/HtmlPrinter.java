package net.ninjacat.brking.output;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.DiffType;
import org.apache.commons.text.StrSubstitutor;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

public class HtmlPrinter
    implements DiffPrinter
{
  Map<DiffType, String> MESSAGES = ImmutableMap.<DiffType, String>builder()
      .put(DiffType.ClassAdded, "Class <div class='name_to'>${changed.to}</div> has been added")
      .put(DiffType.ClassRemoved, "Class <div class='name_from'>${changed.from}has been removed")
      .put(DiffType.ClassInterfaceAdded, "Interfaces were added: <div class='name_to'>${changed.to}</div>")
      .put(DiffType.ClassInterfaceRemoved, "Interfaces were removed: <div class='name_from'>${changed.from}</div>")
      .put(DiffType.ClassModifiersChanged, "Modifiers changed from <div class='name_from'>" +
          "${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.ClassVisibilityChanged, "Visibility has changed: <div class='name_to'>${changed.to}</div>")
      .put(DiffType.ClassVersionIncremented, "Class file version changed from <div class='name_from'>${changed.from}" +
          "</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.ClassVersionDecremented, "Class file version changed from <div class='name_from'>${changed.from}" +
          "</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.ClassSuperClassChanged, "Super class changed from <div class='name_from'>${changed.from}" +
          "</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.ClassDeprecated, "Class <div class='element'>${object.apiName} became deprecated")
      .put(DiffType.ClassAnnotationsChanged, "Annotations changed from <div class='name_from'>${changed.from}" +
          "</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.FieldModifiersChanged,
          "Field <div class='element'>${object.apiName}</div> modifiers changed from " +
              "<div class='name_from'>${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.FieldRemoved, "Field <div class='element'>${object.apiName}</div> has been removed")
      .put(DiffType.FieldAdded, "Field <div class='element'>${object.apiName}</div> has been added")
      .put(DiffType.FieldTypeChanged,
          "Field <div class='element'>${object.apiName}</div> type changed from <div class='name_from'>" +
              "${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.FieldVisibilityChanged,
          "Field <div class='element'>${object.apiName}</div> visibility changed from " +
              "<div class='name_from'>${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.FieldDeprecated, "Field <div class='element'>${object.apiName}</div> became deprecated")
      .put(DiffType.FieldAnnotationsChanged,
          "Field <div class='element'>${object.apiName}</div> annotations changed from " +
              "<div class='name_from'>${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.MethodModifiersChanged,
          "Method <div class='element'>${object.apiName}</div> modifiers changed from " +
              "<div class='name_from'>${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.MethodRemoved,
          "Method <div class='element'>${object.apiName}</div> has been removed")
      .put(DiffType.MethodAdded, "Method <div class='element'>${object.apiName}</div> has been added")
      .put(DiffType.MethodReturnTypeChanged,
          "Method <div class='element'>${object.apiName}</div> return type changed from " +
              "<div class='name_from'>${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.MethodVisibilityChanged,
          "Method <div class='element'>${object.apiName}</div> visibility changed from " +
              "<div class='name_from'>${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.MethodExceptionsChanged,
          "Method <div class='element'>${object.apiName}</div> exception list changed from " +
              "<div class='name_from'>${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.MethodAnnotationsChanged,
          "Method <div class='element'>${object.apiName}</div> annotations changed from " +
              "<div class='name_from'>${changed.from}</div> to <div class='name_to'>${changed.to}</div>")
      .put(DiffType.MethodDeprecated, "Method <div class='element'>${object.apiName}</div> became deprecated")
      .build();

  @Override
  public void print(final PrintStream out, final List<DiffElement> diff) {
    final Multimap<String, DiffElement> byClass = HashMultimap.create();
    diff.forEach(elem -> byClass.put(elem.ownerClass().apiName(), elem));

    final StringBuilder sb = new StringBuilder();

    byClass.keySet().forEach(className -> {
      sb.append("<h2>").append(className).append("</h2>");
      printDiffs(sb, Optional.ofNullable(byClass.get(className)).orElseGet(List::of));
    });

    try {
      final String template = Files.readString(Paths.get(getClass().getResource("/html_report.template.html").toURI()));
      out.println(template.replace("%INSERT_HERE%", sb.toString()));
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private void printDiffs(final StringBuilder sb, final Collection<DiffElement> diffElements) {
    final List<DiffElement> elements = diffElements.stream()
        .sorted(Comparator.comparing(DiffElement::diffType))
        .collect(Collectors.toUnmodifiableList());

    elements.forEach(elem -> {
          sb.append("<p>");
          final Map<String, Object> stringObjectMap = elem.asTemplateContext();
          final Map<String, String> context = new HashMap<>();
          stringObjectMap.forEach((s, o) -> context.put(s, escapeHtml4(o.toString())));
          final var template = new StrSubstitutor(context);
          final var prefix = "<div class='severity " + elem.diffType().getSeverity().name() + "'>"
              + elem.diffType().getSeverity().name() + "</div>";
          final var message = template.replace(MESSAGES.get(elem.diffType()));
          sb.append(prefix).append(message).append("\n");
          sb.append("</p>\n");
        }
    );
  }
}
