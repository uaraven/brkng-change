package net.ninjacat.brking;

import com.beust.jcommander.Parameter;
import net.ninjacat.brking.output.Printers;

public class Args
{
    @Parameter(names = {"-r", "--repository-url"},
               description = "Maven repository URL, default is Maven Central",
               order = 10)
    private String mavenRepo = "https://repo1.maven.org/maven2/";

    @Parameter(names = {"-a", "--repository-auth"},
               description = "Maven repository user name and password for HTTP basic authentication in 'user:password' format",
               order = 15)
    private String repoAuth = "";

    @Parameter(names = {"--older"},
               description = "Older version of jar. Maven coordinates, file or URL",
               required = true,
               order = 20)
    private String previousJar;

    @Parameter(names = {"--newer"},
               description = "Older version of jar. Maven coordinates, file or URL",
               required = true,
               order = 30)
    private String currentJar;

    @Parameter(names = {"--relaxed"},
               description = "Relaxed diff rules for return type changes. If return type changed to subclass of older return type it will be treated as warning",
               order = 40)
    private boolean relaxed = false;

    @Parameter(names = {"--non-public"}, description = "Detect changes in non-public APIs", order = 45)
    private boolean nonPublic = false;

    @Parameter(names = {"-f", "--format"}, description = "Output format. One of CONSOLE, JSON or HTML", order = 50)
    private Printers outputFormat = Printers.CONSOLE;

    @Parameter(names = {"-o", "--output"}, description = "Output file name", order = 60)
    private String outputPath = "./";

    @Parameter(names = "--help", help = true, order = 200)
    private boolean help;

    public String getMavenRepo() {
        return mavenRepo;
    }

    public String getRepoAuth() {
        return repoAuth;
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

    public boolean isPublicOnly() {
        return !nonPublic;
    }

    public boolean isRelaxed() {
        return relaxed;
    }

    public boolean isHelp() {
        return help;
    }
}
