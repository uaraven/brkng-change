package net.ninjacat.brking;

import java.util.List;

import com.beust.jcommander.Parameter;

public class Args {
  /**
   * Coordinates in gradle format {groupId}:{artifactId}:{versionId}
   */
  @Parameter(order = -1, description = "[<gradle artifact coordinates>]")
  private List<String> coordinates;

  @Parameter(names = {"--groupId", "-g"}, description = "Group Id", order = 0)
  private String groupId;

  @Parameter(names = {"-a", "--artifactId"}, description = "Artifact Id", order = 1)
  private String artifactId;

  @Parameter(names = {"-v", "--version"}, description = "Version number", order = 2)
  private String version;

  @Parameter(names = {"-p", "--prev-version"}, description = "Previous version number to compare against", order = 3)
  private String previousVersion;

  @Parameter(names = {"-r", "--repository-url"},
      description = "Maven repository URL, default is Maven Central",
      order = 4)
  private String mavenRepo = "https://repo1.maven.org/maven2/";

  @Parameter(names = {"--older-jar"}, description = "Previous version jar file", order = 5)
  private String previousJar;

  @Parameter(names = {"--newer-jar"}, description = "Current version jar file", order = 6)
  private String currentJar;

  @Parameter(names = "--help", help = true, order = 200)
  private boolean help;

  public String getCoordinates() {
    return coordinates.isEmpty() ? "" : coordinates.get(0);
  }

  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getVersion() {
    return version;
  }

  public String getPreviousVersion() {
    return previousVersion;
  }

  public String getMavenRepo() {
    return mavenRepo;
  }

  public String getPreviousJar() {
    return previousJar;
  }

  public String getCurrentJar() {
    return currentJar;
  }

  public boolean isHelp() {
    return help;
  }
}
