package net.ninjacat.brking.diff.rules;

import net.ninjacat.brking.api.ApiClass;
import net.ninjacat.brking.diff.ChangeSeverity;
import net.ninjacat.brking.diff.DiffElement;
import net.ninjacat.brking.diff.ImmutableDiffElement;

import java.util.List;

public class ClassVersion implements ClassDiffRule {
    @Override
    public List<DiffElement> process(final ApiClass older, final ApiClass newer) {
        final var builder = ImmutableDiffElement.builder().apiObject(older);
        switch (Integer.compare(older.version(), newer.version())) {
            case -1:
                builder.severity(ChangeSeverity.WARNING)
                        .description(String.format("Bytecode version has changed from %d to %d. "
                                        + "Newer version may require newer JDK to run", older.version(),
                                newer.version()));
                break;
            case 0:
                return List.of();
      case 1:
        builder.severity(ChangeSeverity.SAFE)
               .description(
                   String.format("Bytecode version has changed from %d to %d.", older.version(), newer.version()));
        break;
    }
    return List.of(builder.build());
  }
}
