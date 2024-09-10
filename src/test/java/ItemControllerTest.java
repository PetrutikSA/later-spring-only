import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.item.ItemController;
import ru.practicum.item.ItemService;
import ru.practicum.item.dto.ItemCreateDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemUpdateDto;
import ru.practicum.item.dto.mapper.ItemMapper;
import ru.practicum.item.dto.mapper.ItemMapperImpl;
import ru.practicum.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;
    private MockMvc mvc;
    private final ItemMapper itemMapper = new ItemMapperImpl();
    private final ObjectMapper mapper = new ObjectMapper();
    private ItemCreateDto itemCreate;
    private ItemUpdateDto itemUpdate;
    private ItemDto itemCreatedDto;
    private ItemDto itemUpdatedDto;
    private long userId;

    public ItemControllerTest() {
    }

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();

        userId = 1L;

        itemCreate = new ItemCreateDto();
        itemCreate.setUrl("url");
        itemCreate.setTags(Set.of("Tag1", "Tag2"));

        itemUpdate = new ItemUpdateDto();
        itemUpdate.setRead(true);
        itemUpdate.setReplaceTags(true);
        itemUpdate.setTags(Set.of("Tag3"));

        Item item = new Item();
        item.setUrl(itemCreate.getUrl());
        item.setTags(itemCreate.getTags());
        item.setUserId(userId);

        itemCreatedDto = itemMapper.itemToItemDto(item);

        item.setTags(itemUpdate.getTags());
        item.setUnread(false);

        itemUpdatedDto = itemMapper.itemToItemDto(item);
    }

    /*
    сохранение дополнительных данных о странице;
    поиск ссылок;
    редактирование данных о ссылке;
    удаление данных о ссылке.
    */

    @Test
    void saveItemTest() throws Exception {
        Mockito.when(itemService.saveNewItem(eq(userId), Mockito.any()))
                .thenReturn(itemCreatedDto);

        mvc.perform(post("/items")
                .content(mapper.writeValueAsString(itemCreate))
                .characterEncoding(StandardCharsets.UTF_8)
                .header("X-Later-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.normalUrl", is(itemCreate.getUrl())))
                .andExpect(jsonPath("$.tags", hasSize(2)));
    }

    @Test
    void updateItemTest() throws Exception {
        Mockito.when(itemService.updateItem(eq(userId), Mockito.any()))
                .thenReturn(itemUpdatedDto);

        mvc.perform(patch("/items")
                        .content(mapper.writeValueAsString(itemUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Later-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unread", is(false)))
                .andExpect(jsonPath("$.tags", hasSize(1)));
    }

    @Test
    void deleteItemTest() throws Exception {
        mvc.perform(delete("/items/{itemId}", 1L)
                .header("X-Later-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void itemGetTest() throws Exception {
        Mockito.when(itemService.getItems(Mockito.any()))
                .thenReturn(List.of(itemUpdatedDto));

        mvc.perform(get("/items?state=read")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Later-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].unread", is(false)))
                .andExpect(jsonPath("$[0].tags", hasSize(1)));
    }
}
