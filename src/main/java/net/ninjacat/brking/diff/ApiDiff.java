package net.ninjacat.brking.diff;

import java.util.Comparator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Streams;
import net.ninjacat.brking.api.ClassPool;
import net.ninjacat.brking.diff.rules.ClassComparator;

import static java.util.Comparator.reverseOrder;

public class ApiDiff {
  private final ClassPool olderClassPool;

  private final ClassPool newerClassPool;

  private boolean ignoreNameChanges;

  private ApiDiff(final ClassPool older, final ClassPool newer, final boolean ignoreNameChanges) {
    this.olderClassPool = older;
    this.newerClassPool = newer;
    this.ignoreNameChanges = ignoreNameChanges;
  }

  public static List<DiffElement> ofJars(final JarFile older, final JarFile newer, final SortType sortType) {
    return new ApiDiff(ClassPool.ofOlder(older), ClassPool.ofNewer(newer), false).diff(sortType);
  }

  public static List<DiffElement> ofClasses(final String olderClassName,
                                            final String newerClassName,
                                            final SortType sortType) {
    return new ApiDiff(
        ClassPool.ofClass(olderClassName, true),
        ClassPool.ofClass(newerClassName, false), true).diff(sortType);
  }

  public List<DiffElement> diff(final SortType sortType) {
    final var newerClasses = newerClassPool.all().keySet();
    final Stream<DiffElement> classChanges = ignoreNameChanges
                                             ? Stream.empty()
                                             : Stream.concat(addedClasses().stream(), removedClasses().stream());
    final Stream<DiffElement> diffElementStream = Streams.concat(
        classChanges,
        olderClassPool.all().entrySet().stream()
                      .filter(it -> ignoreNameChanges || newerClasses.contains(it.getKey()))
                      .flatMap(it -> ClassComparator.compare(it.getValue(), newerClassPool.get(it.getKey())).stream()));

    return diffElementStream.sorted(sortType.comparator).collect(Collectors.toUnmodifiableList());
  }

  private List<DiffElement> addedClasses() {
    final var olderClasses = olderClassPool.all().keySet();
    return newerClassPool.all().entrySet().stream()
                         .filter(
                             it -> !olderClasses.contains(it.getKey()) && it.getValue().isPublic())
                         .map(it -> ImmutableDiffElement.builder()
                                                        .diffType(DiffType.ClassAdded)
                                                        .changedObject(it.getValue())
                                                        .ownerClass(it.getValue())
                                                        .changedFrom("")
                                                        .changedTo(it.getValue().apiName())
                                                        .build())
                         .collect(Collectors.toUnmodifiableList());
  }

  private List<DiffElement> removedClasses() {
    final var newerClasses = newerClassPool.all().keySet();
    return olderClassPool.all().entrySet().stream()
                         .filter(it -> !newerClasses.contains(it.getKey()))
                         .map(it -> ImmutableDiffElement.builder()
                                                        .diffType(DiffType.ClassRemoved)
                                                        .changedObject(it.getValue())
                                                        .ownerClass(it.getValue())
                                                        .changedFrom("")
                                                        .changedTo(it.getValue().apiName())
                                                        .build())
                         .collect(Collectors.toUnmodifiableList());
  }

  public enum SortType {
    BY_SEVERITY(Comparator.<DiffElement, ChangeSeverity>
        comparing(o -> o.diffType().getSeverity(), reverseOrder())
                    .thenComparing(DiffElement::diffType)
                    .thenComparing(o -> o.ownerClass().descriptor())
                    .thenComparing(o -> o.changedObject().apiName())),
    BY_ELEMENT(Comparator.<DiffElement, String>
        comparing(o -> o.ownerClass().descriptor())
                   .thenComparing(DiffElement::diffType).reversed()
                   .thenComparing(o -> o.changedObject().apiName()));

    private final Comparator<DiffElement> comparator;

    SortType(final Comparator<DiffElement> comparator) {
      this.comparator = comparator;
    }
  }
}
