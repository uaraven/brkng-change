package net.ninjacat.brking;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class SemVerTest {
  @Test
  public void shouldParseVersion() {
    assertThat(SemVer.of("1.2.3").getElements(), contains(1, 2, 3));
    assertThat(SemVer.of("1").getElements(), contains(1));
    assertThat(SemVer.of("1.2.3.4.5.6.7").getElements(), contains(1, 2, 3, 4, 5, 6, 7));
    assertThat(SemVer.of("1.2.3-SNAPSHOT").getElements(), contains(1, 2, 3));
    assertThat(SemVer.of("1.2.3").getQualifier(), is(""));
    assertThat(SemVer.of("1.2.3-jre27").getQualifier(), is("jre27"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToParseNonNumeric() {
    SemVer.of("1.2.3alpha");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToParseEmptyString() {
    SemVer.of("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailToParseVersionWithEmptyElement() {
    SemVer.of("1..1");
  }

  @Test
  public void shouldCorrectlyCompareVersions() {
    assertThat(SemVer.of("1.2.3").compareTo(SemVer.of("1.2.4")), is(-1));
    assertThat(SemVer.of("1.3.3").compareTo(SemVer.of("1.2.4")), is(1));
    assertThat(SemVer.of("2.3.3").compareTo(SemVer.of("1.2.4")), is(1));
    assertThat(SemVer.of("2.3").compareTo(SemVer.of("2.3.1")), is(-1));
    assertThat(SemVer.of("2.3.1").compareTo(SemVer.of("2")), is(1));
    assertThat(SemVer.of("2.3").compareTo(SemVer.of("2.3")), is(0));
  }

  @Test
  public void shouldFindDifferenceIndex() {
    assertThat(SemVer.of("1.2.3").differsIn(SemVer.of("1.2.4")), is(2));
    assertThat(SemVer.of("1.3.3").differsIn(SemVer.of("1.2.4")), is(1));
    assertThat(SemVer.of("2.3.3").differsIn(SemVer.of("1.2.4")), is(0));
    assertThat(SemVer.of("2.3").differsIn(SemVer.of("2.3.1")), is(2));
    assertThat(SemVer.of("2.3.1").differsIn(SemVer.of("2")), is(1));
    assertThat(SemVer.of("2.3").differsIn(SemVer.of("2.3")), is(-1));
  }
}