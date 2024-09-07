package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> post(long userId, ItemDtoCreate itemDtoCreate) {
        return post("", userId, itemDtoCreate);
    }

    public ResponseEntity<Object> findItemsOfUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findById(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> patch(long itemId, long userId, ItemDtoUpdate itemDtoUpdate) {
        return patch("/" + itemId, userId, itemDtoUpdate);
    }

    public ResponseEntity<Object> delete(long itemId, long userId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchByText(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get(addParamToPath("/search", parameters), null, parameters);
    }

    public ResponseEntity<Object> postComment(long itemId, long userId, CommentDto commentDto) {
        return post(String.format("/%s/comment", itemId), userId, commentDto);
    }
}
