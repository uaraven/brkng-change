package net.ninjacat.brking;

import com.beust.jcommander.Parameter;
import net.ninjacat.brking.output.Printers;

public class Args {

  @Parameter(names = {"-r", "--repository-url"},
          description = "Maven repository URL, default is Maven Central",
          order = 1)
  private String mavenRepo = "https://repo1.maven.org/maven2/";

  @Parameter(names = {"--older"}, description = "Older version of jar. Maven coordinates, file or URL", order = 2)
  private String previousJar;

  @Parameter(names = {"--newer"}, description = "Older version of jar. Maven coordinates, file or URL", order = 3)
  private String currentJar;

  @Parameter(names = {"-f", "--format"}, description = "Output format. One of CONSOLE, JSON or HTML", order = 4)
  private Printers outputFormat = Printers.CONSOLE;

  @Parameter(names = {"-o", "--output"}, description = "Output file name", order = 5)
  private String outputPath = "./";


  @Parameter(names = "--help", help = true, order = 200)
  private boolean help;

  public String getMavenRepo() {
    return mavenRepo;
  }

  public String getPreviousJar() {
    return previousJar;
  }

  public String getCurrentJar() {
    return currentJar;
  }

  public Printers getOutputFormat() {
    return outputFormat;
  }

  public String getOutputPath() {
    return outputPath;
  }

  public boolean isHelp() {
    return help;
  }
}
