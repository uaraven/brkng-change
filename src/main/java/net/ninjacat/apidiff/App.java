package net.ninjacat.apidiff;

import com.beust.jcommander.JCommander;
import net.ninjacat.apidiff.logging.ConsoleLogger;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.ansi;

public final class App {
  private App() {
  }

  public static void main(final String[] args) {
    final var params = new Args();
    final var comm = JCommander.newBuilder().addObject(params).build();
    comm.setProgramName("apidiff");
    comm.parse(args);

    AnsiConsole.systemInstall();

    if (params.isHelp()) {
      System.out.println("Detect public API differences between versions");
      comm.usage();
      System.out.println(ansi().reset()
                               .a("Provide artifact coordinates either in gradle format ")
                               .fgBrightBlue().a("groupId:artifactId:version")
                               .fgDefault().a(" or as separate options, i.e. ")
                               .fgBrightBlue().a("--groupId groupId --artifactId artifactId")
                               .fgDefault().a(", etc. "));

      System.out.println(ansi().reset()
                               .a("Alternatively paths to current and previous JAR files can be provided with ")
                               .fgBrightBlue().a("--newer-jar").fgDefault().a(" and ").fgBrightBlue().a("--older-jar")
                               .fgDefault().a(" parameters").reset());
    }

    try {
      new Director().execute(params);
    } catch (final Exception ex) {
      ex.printStackTrace();
      ConsoleLogger.getLogger().err(ex.getMessage());
    }
  }
}
