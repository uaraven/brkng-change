package net.ninjacat.brking.output;

import net.ninjacat.brking.Args;

import java.util.function.Function;

public enum Printers {
    CONSOLE(args -> new ConsolePrinter()),
    JSON(args -> new ConsolePrinter()),
    HTML(args -> new ConsolePrinter());

    private final Function<Args, DiffPrinter> printerProvider;

    Printers(final Function<Args, DiffPrinter> printerProvider) {
        this.printerProvider = printerProvider;
    }

    public DiffPrinter getPrinter(final Args args) {
        return printerProvider.apply(args);
    }
}
