package config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.item.ItemService;

@Configuration
public class ItemTestConfig {
    @Bean
    public ItemService itemService() {
        return Mockito.mock(ItemService.class);
    }
}
