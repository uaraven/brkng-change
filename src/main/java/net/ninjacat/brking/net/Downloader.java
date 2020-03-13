package net.ninjacat.brking.net;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import io.vavr.control.Try;
import net.ninjacat.brking.logging.ConsoleLogger;
import net.ninjacat.brking.logging.Logger;

import static org.fusesource.jansi.Ansi.ansi;

public class Downloader {
  private static final Logger LOGGER = ConsoleLogger.getLogger();

  private final HttpClient client;

  public Downloader() {
    this.client = HttpClient.newBuilder()
                            .followRedirects(Redirect.NORMAL)
                            .connectTimeout(Duration.ofSeconds(20))
                            .build();
  }

  public CompletableFuture<File> download(final URI uri, final String fileName) {
    final var req = HttpRequest.newBuilder()
                               .uri(uri)
                               .timeout(Duration.ofMinutes(2))
                               .GET()
                               .build();

    LOGGER.print("Downloading %s from %s", fileName, uri);
    return Try.of(() -> File.createTempFile("tmp", fileName).getAbsoluteFile().toPath())
              .fold(CompletableFuture::failedFuture,
                    path -> client.sendAsync(req, HttpResponse.BodyHandlers.ofFile(path))
                                  .thenAccept(resp -> handleResponse(resp))
                                  .thenApply(v -> path.toFile())
              );
  }

  private void handleResponse(final HttpResponse<Path> resp) {
    final Path fileName = Paths.get(resp.uri().getPath()).getFileName();
    if (resp.statusCode() < 400) {
      LOGGER.print(ansi().a("Downloaded ").fgBrightBlue().a("%s").fgDefault().toString(), fileName);
    }
    else {
      LOGGER.err("Failed to download %s. Http Status Code: %d", fileName, resp.statusCode());
    }
  }
}
