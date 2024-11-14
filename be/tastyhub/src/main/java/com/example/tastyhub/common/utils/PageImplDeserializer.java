package com.example.tastyhub.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageImplDeserializer extends JsonDeserializer<PageImpl<?>> {

  @Override
  public PageImpl<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    JsonNode contentNode = node.get("content");
    JsonNode numberNode = node.get("number");
    JsonNode sizeNode = node.get("size");
    JsonNode totalElementsNode = node.get("totalElements");

    List<Object> content = new ArrayList<>();
    if (contentNode.isArray()) {
      for (JsonNode contentItem : contentNode) {
        Object item = jp.getCodec().treeToValue(contentItem, Object.class);
        content.add(item);
      }
    }

    int number = numberNode.asInt();
    int size = sizeNode.asInt();
    long totalElements = totalElementsNode.asLong();

    return new PageImpl<>(content, PageRequest.of(number, size), totalElements);
  }
}
