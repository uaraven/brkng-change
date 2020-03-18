package net.ninjacat.brking.output;

import net.ninjacat.brking.diff.DiffElement;

import java.util.List;

public interface DiffPrinter {
  void print(List<DiffElement> diff);
}
