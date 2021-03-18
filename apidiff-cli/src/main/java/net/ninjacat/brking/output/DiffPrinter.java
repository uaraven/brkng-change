package net.ninjacat.brking.output;

import java.io.PrintStream;
import java.util.List;

import net.ninjacat.brking.diff.DiffElement;

public interface DiffPrinter
{
  void print(PrintStream writer, List<DiffElement> diff);
}
