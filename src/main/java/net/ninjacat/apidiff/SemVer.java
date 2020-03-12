package net.ninjacat.apidiff;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class SemVer implements Comparable<SemVer> {
  private final List<Integer> versions;

  private final String qualifier;

  private SemVer(final java.util.List<Integer> versions, final String qualifier) {
    this.versions = io.vavr.collection.List.ofAll(versions);
    this.qualifier = qualifier;
  }

  public static SemVer of(final String version) {
    final String ver;
    final String qualifier;
    final int endIndex = version.indexOf('-');
    if (endIndex > 0) {
      ver = version.substring(0, endIndex);
      qualifier = version.substring(endIndex + 1);
    }
    else {
      ver = version;
      qualifier = "";
    }
    return Try.of(() -> new SemVer(Arrays.stream(ver.split("\\."))
                                         .map(Integer::parseInt)
                                         .collect(Collectors.toList()), qualifier))
              .getOrElseThrow(thr -> new IllegalArgumentException("Invalid version string \"%s\"".formatted(version)));
  }

  public java.util.List<Integer> getElements() {
    return versions.asJava();
  }

  public String getQualifier() {
    return qualifier;
  }

  /**
   * Finds number of version element where this version differs from previous. Version element indices are numbered from
   * 0, where major version being index 0.
   * <p>
   * Returns -1 if versions do not differ.
   *
   * @param other The over version
   * @return Index of the first version element that is different between this and the other versions or -1 if the
   * versions are the same
   */
  public int differsIn(final SemVer other) {
    return zip(this.versions, other.versions)
        .zipWithIndex()
        .dropWhile(pair -> pair._1._1.compareTo(pair._1._2) == 0)
        .map(pair -> pair._2)
        .peekOption()
        .getOrElse(-1);
  }

  @Override
  public int compareTo(final SemVer other) {
    return zip(this.versions, other.versions)
        .map(pair -> pair._1.compareTo(pair._2))
        .dropWhile(it -> it == 0)
        .peekOption()
        .getOrElse(0);
  }

  private Tuple2<List<Integer>, List<Integer>> makeSameLength(final List<Integer> first, final List<Integer> second) {
    final var a = first.length() < second.length()
                  ? first.padTo(second.length(), 0)
                  : first;
    final var b = second.length() < first.length()
                  ? second.padTo(first.length(), 0)
                  : second;
    return Tuple.of(a, b);
  }

  private List<Tuple2<Integer, Integer>> zip(final List<Integer> first, final List<Integer> second) {
    final var lists = makeSameLength(first, second);
    return lists._1().zip(lists._2());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final SemVer semVer = (SemVer) o;
    return versions.equals(semVer.versions) &&
           qualifier.equals(semVer.qualifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(versions, qualifier);
  }

  @Override
  public String toString() {
    return "SemVer{" + versions.mkString(".") + '-' + qualifier + '}';
  }
}
