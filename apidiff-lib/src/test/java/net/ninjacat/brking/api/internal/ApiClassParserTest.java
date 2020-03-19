package net.ninjacat.brking.api.internal;

import net.ninjacat.brking.api.ApiClass;
import org.junit.Test;

import javax.annotation.concurrent.Immutable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ApiClassParserTest {
  @Test
  public void shouldParseClass() {
    final ApiClass testCls = ApiClassParser.of(TestClass.class.getName()).get();

    assertThat(testCls.superName(), is("java.lang.Object"));
    assertThat(testCls.interfaces(), hasSize(0));

    assertThat(testCls.fields(), hasSize(2));
    assertThat(testCls.methods(), hasSize(3));
    assertThat(testCls.annotations(), hasSize(1));
  }

  @Immutable
  public static class TestClass {
    private long longField = 10;

    public String strField;

    public void setStr(String value) {
      strField = value;
    }

    protected long getLong() {
      return longField;
    }
  }
}