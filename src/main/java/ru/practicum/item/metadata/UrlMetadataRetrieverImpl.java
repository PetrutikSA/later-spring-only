package ru.practicum.item.metadata;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import ru.practicum.exception.ItemRetrieverException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

public class UrlMetadataRetrieverImpl implements UrlMetadataRetriever {
    private final HttpClient client;

    // В качестве параметра конструктора, сервис принимает количество секунд в течении которых
    // он будет ожидать ответа от сервера обрабатывающего url-адрес, переданный в качестве
    // ссылки для сохранения. Этот параметр Spring получает из файла настроек и автоматически
    // внедряет в бин. Если в файле настроек таймаут не указан, то по умолчанию он будет равен
    // 120 секундам.
    UrlMetadataRetrieverImpl(@Value("${url-metadata-retriever.read_timeout-sec:120}") int readTimeout) {
        // Для получения метаданных об URL воспользуемся стандартным HttpClient'ом.
        // Для этого создадим его экземпляр с нужными нам настройками
        // Во первых, указываем всегда переходить по новому адресу, если сервер
        // обрабатывающий URL указывает нам на это. Такая ситуация может возникнуть,
        // например если пользователь сохраняет сокращенную ссылку (полученную, например
        // через сервис bitly.com) или по каким-либо другим причинам. Также указываем таймаут
        // ожидания соединения.
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

    @Override
    public UrlMetadata retrieve(String url) {
        final URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new ItemRetrieverException("The URL is malformed: " + url, e);
        }

        HttpResponse<Void> resp = connect(uri, "HEAD", HttpResponse.BodyHandlers.discarding());

        String contentType = resp.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse("*");

        MediaType mediaType = MediaType.parseMediaType(contentType);

        final UrlMetadataImpl result;
        if (mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            result = handleText(resp.uri());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("image/*"))) {
            result = handleImage(resp.uri());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("video/*"))) {
            result = handleVideo(resp.uri());
        } else {
            throw new ItemRetrieverException("The content type [" + mediaType
                    + "] at the specified URL is not supported.");
        }

        return result.toBuilder()
                .normalUrl(url)
                .resolvedUrl(resp.uri().toString())
                .mimeType(mediaType.getType())
                .dateResolved(Instant.now())
                .build();
    }

    private UrlMetadataImpl handleText(URI url) {
        HttpResponse<String> resp = connect(url, "GET", HttpResponse.BodyHandlers.ofString());

        Document doc = Jsoup.parse(resp.body());

        Elements imgElements = doc.getElementsByTag("img");
        Elements videoElements = doc.getElementsByTag("video");

        return UrlMetadataImpl.builder()
                .title(doc.title())
                .hasVideo(videoElements.isEmpty())
                .hasImage(imgElements.isEmpty())
                .build();

    }

    private UrlMetadataImpl handleVideo(URI url) {
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasVideo(true)
                .build();
    }

    private UrlMetadataImpl handleImage(URI url) {
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasImage(true)
                .build();
    }

    private <T> HttpResponse<T> connect(URI url,
                                       String method,
                                       HttpResponse.BodyHandler<T> responseBodyHandler) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();

        final HttpResponse<T> response;
        try {
            response = client.send(request, responseBodyHandler);
        } catch (IOException e) {
            throw new ItemRetrieverException("Cannot retrieve data from the URL: " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Cannot get the metadata for url: " + url
                    + " because the thread was interrupted.", e);
        }

        HttpStatus status = HttpStatus.resolve(response.statusCode());
        if(status == null) {
            throw new ItemRetrieverException("The server returned an unknown status code: " + response.statusCode());
        }

        if(status.equals(HttpStatus.UNAUTHORIZED) || status.equals(HttpStatus.FORBIDDEN)) {
            throw new ItemRetrieverException("There is no access to the resource at the specified URL: " + url);
        }
        if(status.isError()) {
            throw new ItemRetrieverException("Cannot get the data on the item because the server returned an error."
                    + "Response status: " + status);
        }

        return response;
    }
}
