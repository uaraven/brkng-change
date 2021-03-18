package net.ninjacat.brking.output;

import java.util.function.Function;

import net.ninjacat.brking.Args;

public enum Printers
{
    TEXT(args -> new ConsolePrinter()),
    JSON(args -> new ConsolePrinter()),
    HTML(args -> new HtmlPrinter());

    private final Function<Args, DiffPrinter> printerProvider;

    Printers(final Function<Args, DiffPrinter> printerProvider) {
        this.printerProvider = printerProvider;
    }

    public DiffPrinter getPrinter(final Args args) {
        return printerProvider.apply(args);
    }
}
