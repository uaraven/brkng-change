package net.ninjacat.brking.diff;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.annotations.VisibleForTesting;
import net.ninjacat.brking.diff.ApiDiff.SortType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ApiDiffTest {
  @Test
  public void testClassDetection() {
    final var diffElements = ApiDiff.ofClasses(Older.class.getName(), Newer.class.getName(),
                                               SortType.BY_SEVERITY);

    assertThat(diffElements, hasSize(10));

    testClassAnnotationsChanged(diffElements);
    testClassInterfaceAdded(diffElements);
  }

  public void testClassInterfaceAdded(final List<DiffElement> diffElements) {
    final List<DiffElement> interfaceAdded = filter(diffElements, it -> it.diffType() == DiffType.ClassInterfaceAdded);
    assertThat(interfaceAdded, hasSize(1));
    assertThat(interfaceAdded.get(0).changedTo(), is(Serializable.class.getName()));
  }

  public void testClassAnnotationsChanged(final List<DiffElement> diffElements) {
    final var annotationChanged = filter(diffElements, it -> it.diffType() == DiffType.ClassAnnotationsChanged);
    assertThat(annotationChanged, hasSize(1));
    assertThat(annotationChanged.get(0).changedTo(), is(VisibleForTesting.class.getName()));
  }

  private List<DiffElement> filter(final List<DiffElement> original, final Predicate<DiffElement> predicate) {
    return original.stream().filter(predicate).collect(Collectors.toUnmodifiableList());
  }

  public static class Older {
    public int outdatedField;

    public int intField;

    @Nonnull
    public String getter() {
      return "";
    }

    public long longerGetter() {
      return 0L;
    }
  }

  @interface Anno { }

  @VisibleForTesting
  public static class Newer implements Serializable {
    @Anno
    public float intField;

    public int newIntField;

    public native String getter();

    public long longerGetter() throws RuntimeException {
      return 0L;
    }

    public long longerGetter(long def) {
      return 1L;
    }
  }
}