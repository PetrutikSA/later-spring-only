package ru.practicum.item.metadata;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class UrlMetadataImpl implements UrlMetadata {
    String normalUrl;
    String resolvedUrl;
    String mimeType;
    String title;
    boolean hasImage;
    boolean hasVideo;
    Instant dateResolved;
}
